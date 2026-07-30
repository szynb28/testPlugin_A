package com.testPlugin_A.gameb;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

public final class GameBConfig {
    private final Map<FarmType, Long> unlockCosts = new EnumMap<>(FarmType.class);
    private final Map<FarmType, Integer> yields = new EnumMap<>(FarmType.class);
    private final Map<FarmType, Long> sellPrices = new EnumMap<>(FarmType.class);
    private final Map<FarmAction, Long> actionDurationsMs = new EnumMap<>(FarmAction.class);
    private final Map<FertilizerTier, Long> fertilizerPrices = new EnumMap<>(FertilizerTier.class);
    private final Map<FertilizerTier, Double> growthMultipliers = new EnumMap<>(FertilizerTier.class);
    private long fertilizingDurationMs;
    private long growthDurationMs;
    private long stealCooldownMs;
    private double stealYieldRatio;

    private GameBConfig() { }

    public static GameBConfig from(FileConfiguration config) {
        GameBConfig values = defaults();
        ConfigurationSection root = config.getConfigurationSection("game-b");
        if (root == null) return values;
        values.growthDurationMs = seconds(root, "durations.growing-seconds", values.growthDurationMs);
        values.fertilizingDurationMs = seconds(root, "durations.fertilizing-seconds", values.fertilizingDurationMs);
        values.stealCooldownMs = seconds(root, "stealing.cooldown-seconds", values.stealCooldownMs);
        values.stealYieldRatio = bounded(root.getDouble("stealing.yield-ratio", values.stealYieldRatio), 0.0, 1.0);
        for (FarmAction action : FarmAction.values()) {
            values.actionDurationsMs.put(action, seconds(root, "durations." + action.name().toLowerCase() + "-seconds", values.actionDurationsMs.get(action)));
        }
        for (FarmType farm : FarmType.values()) {
            String path = "farms." + farm.id();
            values.unlockCosts.put(farm, Math.max(0, root.getLong(path + ".unlock-cost", values.unlockCosts.get(farm))));
            values.yields.put(farm, Math.max(1, root.getInt(path + ".yield", values.yields.get(farm))));
            values.sellPrices.put(farm, Math.max(0, root.getLong(path + ".sell-price", values.sellPrices.get(farm))));
        }
        for (FertilizerTier tier : FertilizerTier.values()) {
            if (tier == FertilizerTier.NONE) continue;
            String path = "fertilizers." + tier.id();
            values.fertilizerPrices.put(tier, Math.max(0, root.getLong(path + ".price", values.fertilizerPrices.get(tier))));
            values.growthMultipliers.put(tier, bounded(root.getDouble(path + ".growth-multiplier", values.growthMultipliers.get(tier)), 0.05, 1.0));
        }
        return values;
    }

    public static GameBConfig defaults() {
        GameBConfig values = new GameBConfig();
        values.unlockCosts.put(FarmType.LIFE_TREE, 0L);
        values.unlockCosts.put(FarmType.ORCHARD, 500L);
        values.unlockCosts.put(FarmType.HERB_GARDEN, 2_000L);
        values.yields.put(FarmType.LIFE_TREE, 5);
        values.yields.put(FarmType.ORCHARD, 8);
        values.yields.put(FarmType.HERB_GARDEN, 12);
        values.sellPrices.put(FarmType.LIFE_TREE, 20L);
        values.sellPrices.put(FarmType.ORCHARD, 35L);
        values.sellPrices.put(FarmType.HERB_GARDEN, 60L);
        for (FarmAction action : FarmAction.values()) values.actionDurationsMs.put(action, 60_000L);
        values.fertilizingDurationMs = 60_000L;
        values.growthDurationMs = 20 * 60_000L;
        values.fertilizerPrices.put(FertilizerTier.BASIC, 80L);
        values.fertilizerPrices.put(FertilizerTier.ADVANCED, 220L);
        values.fertilizerPrices.put(FertilizerTier.MASTER, 500L);
        values.growthMultipliers.put(FertilizerTier.NONE, 1.0);
        values.growthMultipliers.put(FertilizerTier.BASIC, 0.85);
        values.growthMultipliers.put(FertilizerTier.ADVANCED, 0.65);
        values.growthMultipliers.put(FertilizerTier.MASTER, 0.45);
        values.stealCooldownMs = 12 * 60 * 60_000L;
        values.stealYieldRatio = 0.20;
        return values;
    }

    private static long seconds(ConfigurationSection root, String path, long fallbackMs) {
        return Math.max(1, root.getLong(path, fallbackMs / 1_000L)) * 1_000L;
    }

    private static double bounded(double value, double min, double max) { return Math.max(min, Math.min(max, value)); }
    public long unlockCost(FarmType farm) { return unlockCosts.get(farm); }
    public int yield(FarmType farm) { return yields.get(farm); }
    public long sellPrice(FarmType farm) { return sellPrices.get(farm); }
    public long actionDuration(FarmAction action) { return actionDurationsMs.get(action); }
    public long fertilizingDuration() { return fertilizingDurationMs; }
    public long growthDuration() { return growthDurationMs; }
    public long fertilizerPrice(FertilizerTier tier) { return fertilizerPrices.get(tier); }
    public double growthMultiplier(FertilizerTier tier) { return growthMultipliers.get(tier); }
    public long stealCooldownMs() { return stealCooldownMs; }
    public double stealYieldRatio() { return stealYieldRatio; }
}
