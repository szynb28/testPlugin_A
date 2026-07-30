package com.testPlugin_A.gameb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameBService {
    public enum Result { OK, INVALID_STAGE, NOT_UNLOCKED, NOT_ENOUGH_COINS, NOT_ENOUGH_FERTILIZER, NOT_MATURE, COOLDOWN, SELF_TARGET }

    private final GameBRepository repository;
    private final GameBConfig config;

    public GameBService(GameBRepository repository, GameBConfig config) {
        this.repository = repository;
        this.config = config;
    }

    public GameBProfile profile(UUID playerId, String playerName) {
        GameBProfile profile = repository.profile(playerId);
        profile.setLastKnownName(playerName);
        reconcile(profile, System.currentTimeMillis());
        return profile;
    }

    public GameBProfile find(UUID playerId) { return repository.find(playerId); }
    public GameBConfig config() { return config; }

    public void reconcileAll(long now) {
        for (Map.Entry<String, GameBProfile> entry : repository.entries()) reconcile(entry.getValue(), now);
    }

    public void reconcile(GameBProfile profile, long now) {
        for (FarmType farm : FarmType.values()) {
            if (!profile.hasFarm(farm)) continue;
            PlotState plot = profile.plot(farm);
            while (isTimed(plot.getStage()) && now >= plot.getFinishAt()) {
                long completedAt = plot.getFinishAt();
                switch (plot.getStage()) {
                    case DIGGING -> ready(plot, PlotStage.READY_TO_PLANT);
                    case PLANTING -> ready(plot, PlotStage.READY_TO_WATER);
                    case WATERING -> ready(plot, PlotStage.READY_TO_CULTIVATE);
                    case CULTIVATING -> ready(plot, PlotStage.READY_TO_GROW);
                    case FERTILIZING -> beginGrowth(plot, completedAt, FertilizerTier.fromId(plot.getFertilizerId()));
                    case GROWING -> ready(plot, PlotStage.MATURE);
                    default -> { return; }
                }
            }
        }
    }

    public Result startAction(GameBProfile profile, FarmType farm, FarmAction action, long now) {
        reconcile(profile, now);
        if (!profile.hasFarm(farm)) return Result.NOT_UNLOCKED;
        PlotState plot = profile.plot(farm);
        PlotStage expected = switch (action) {
            case DIG -> PlotStage.WILDERNESS;
            case PLANT -> PlotStage.READY_TO_PLANT;
            case WATER -> PlotStage.READY_TO_WATER;
            case CULTIVATE -> PlotStage.READY_TO_CULTIVATE;
        };
        if (plot.getStage() != expected) return Result.INVALID_STAGE;
        PlotStage running = switch (action) {
            case DIG -> PlotStage.DIGGING;
            case PLANT -> PlotStage.PLANTING;
            case WATER -> PlotStage.WATERING;
            case CULTIVATE -> PlotStage.CULTIVATING;
        };
        plot.setStage(running);
        plot.setFinishAt(now + config.actionDuration(action));
        return Result.OK;
    }

    public Result startGrowing(GameBProfile profile, FarmType farm, FertilizerTier fertilizer, long now) {
        reconcile(profile, now);
        if (!profile.hasFarm(farm)) return Result.NOT_UNLOCKED;
        PlotState plot = profile.plot(farm);
        if (plot.getStage() != PlotStage.READY_TO_GROW) return Result.INVALID_STAGE;
        if (fertilizer != FertilizerTier.NONE && profile.fertilizerCount(fertilizer) < 1) return Result.NOT_ENOUGH_FERTILIZER;
        plot.setFertilizerId(fertilizer.id());
        if (fertilizer == FertilizerTier.NONE) beginGrowth(plot, now, fertilizer);
        else {
            profile.getFertilizers().put(fertilizer.id(), profile.fertilizerCount(fertilizer) - 1);
            plot.setStage(PlotStage.FERTILIZING);
            plot.setFinishAt(now + config.fertilizingDuration());
        }
        return Result.OK;
    }

    public Result harvest(GameBProfile profile, FarmType farm, long now) {
        reconcile(profile, now);
        PlotState plot = profile.plot(farm);
        if (plot.getStage() != PlotStage.MATURE) return Result.NOT_MATURE;
        addCrop(profile, farm, config.yield(farm));
        ready(plot, PlotStage.READY_TO_PLANT);
        plot.setFertilizerId(FertilizerTier.NONE.id());
        return Result.OK;
    }

    public Result unlockOrSelect(GameBProfile profile, FarmType farm) {
        if (!profile.hasFarm(farm)) {
            long cost = config.unlockCost(farm);
            if (profile.getCoins() < cost) return Result.NOT_ENOUGH_COINS;
            profile.setCoins(profile.getCoins() - cost);
            profile.getUnlockedFarmIds().add(farm.id());
            profile.plot(farm);
        }
        profile.setActiveFarmId(farm.id());
        return Result.OK;
    }

    public Result buyFertilizer(GameBProfile profile, FertilizerTier tier) {
        if (tier == FertilizerTier.NONE) return Result.INVALID_STAGE;
        long price = config.fertilizerPrice(tier);
        if (profile.getCoins() < price) return Result.NOT_ENOUGH_COINS;
        profile.setCoins(profile.getCoins() - price);
        profile.getFertilizers().put(tier.id(), profile.fertilizerCount(tier) + 1);
        return Result.OK;
    }

    public long sellAll(GameBProfile profile) {
        long income = 0;
        for (FarmType farm : FarmType.values()) {
            int quantity = profile.itemCount(farm.id());
            if (quantity > 0) {
                income += Math.multiplyExact((long) quantity, config.sellPrice(farm));
                profile.getWarehouse().remove(farm.id());
            }
        }
        profile.setCoins(profile.getCoins() + income);
        profile.setTotalEarned(profile.getTotalEarned() + income);
        return income;
    }

    public Result steal(GameBProfile thief, UUID thiefId, UUID ownerId, FarmType farm, long now) {
        if (thiefId.equals(ownerId)) return Result.SELF_TARGET;
        GameBProfile owner = repository.find(ownerId);
        if (owner == null) return Result.NOT_MATURE;
        reconcile(owner, now);
        if (owner.getStealCooldownUntil() > now) return Result.COOLDOWN;
        if (owner.plot(farm).getStage() != PlotStage.MATURE) return Result.NOT_MATURE;
        int amount = Math.max(1, (int) Math.floor(config.yield(farm) * config.stealYieldRatio()));
        addCrop(thief, farm, amount);
        owner.setStealCooldownUntil(now + config.stealCooldownMs());
        return Result.OK;
    }

    public List<Map.Entry<String, GameBProfile>> leaderboard() {
        List<Map.Entry<String, GameBProfile>> entries = new ArrayList<>(repository.entries());
        entries.sort(Comparator.<Map.Entry<String, GameBProfile>>comparingLong(entry -> entry.getValue().getTotalEarned())
                .reversed().thenComparing(entry -> entry.getValue().getLastKnownName()));
        return entries;
    }

    public List<Map.Entry<String, GameBProfile>> matureVisitors() {
        List<Map.Entry<String, GameBProfile>> eligible = new ArrayList<>();
        for (Map.Entry<String, GameBProfile> entry : repository.entries()) {
            GameBProfile profile = entry.getValue();
            reconcile(profile, System.currentTimeMillis());
            for (FarmType farm : FarmType.values()) {
                if (profile.hasFarm(farm) && profile.plot(farm).getStage() == PlotStage.MATURE) {
                    eligible.add(entry);
                    break;
                }
            }
        }
        eligible.sort(Comparator.comparing(entry -> entry.getValue().getLastKnownName()));
        return eligible;
    }

    private void addCrop(GameBProfile profile, FarmType farm, int amount) {
        profile.getWarehouse().put(farm.id(), profile.itemCount(farm.id()) + amount);
    }

    private void beginGrowth(PlotState plot, long startAt, FertilizerTier fertilizer) {
        plot.setStage(PlotStage.GROWING);
        plot.setFinishAt(startAt + Math.max(1, Math.round(config.growthDuration() * config.growthMultiplier(fertilizer))));
    }

    private static void ready(PlotState plot, PlotStage stage) {
        plot.setStage(stage);
        plot.setFinishAt(0);
    }

    private static boolean isTimed(PlotStage stage) {
        return stage == PlotStage.DIGGING || stage == PlotStage.PLANTING || stage == PlotStage.WATERING
                || stage == PlotStage.CULTIVATING || stage == PlotStage.FERTILIZING || stage == PlotStage.GROWING;
    }
}
