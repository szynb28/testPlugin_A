package com.testPlugin_A.gameb;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBServiceTest {
    private final GameBRepository repository = new GameBRepository();
    private final GameBConfig config = GameBConfig.defaults();
    private final GameBService service = new GameBService(repository, config);

    @Test
    void offlineFertilizingAndGrowthResolveToMature() {
        UUID playerId = UUID.randomUUID();
        GameBProfile profile = service.profile(playerId, "Alice");
        PlotState plot = profile.plot(FarmType.LIFE_TREE);
        plot.setStage(PlotStage.READY_TO_GROW);
        profile.getFertilizers().put(FertilizerTier.BASIC.id(), 1);

        assertEquals(GameBService.Result.OK, service.startGrowing(profile, FarmType.LIFE_TREE, FertilizerTier.BASIC, 1_000L));
        plot.setFinishAt(0L);
        service.reconcile(profile, config.growthDuration());

        assertEquals(PlotStage.MATURE, plot.getStage());
    }

    @Test
    void harvestSellAndUnlockUseIndependentCoins() {
        GameBProfile profile = service.profile(UUID.randomUUID(), "Bob");
        PlotState plot = profile.plot(FarmType.LIFE_TREE);
        plot.setStage(PlotStage.MATURE);

        assertEquals(GameBService.Result.OK, service.harvest(profile, FarmType.LIFE_TREE, 1_000L));
        assertEquals(config.yield(FarmType.LIFE_TREE), profile.itemCount(FarmType.LIFE_TREE.id()));
        long income = service.sellAll(profile);
        assertEquals(config.yield(FarmType.LIFE_TREE) * config.sellPrice(FarmType.LIFE_TREE), income);
        assertEquals(income, profile.getCoins());
        assertEquals(income, profile.getTotalEarned());

        profile.setCoins(config.unlockCost(FarmType.ORCHARD));
        assertEquals(GameBService.Result.OK, service.unlockOrSelect(profile, FarmType.ORCHARD));
        assertTrue(profile.hasFarm(FarmType.ORCHARD));
        assertEquals(0, profile.getCoins());
    }

    @Test
    void stealingLeavesOwnerCropAndAppliesTargetCooldown() {
        UUID ownerId = UUID.randomUUID();
        UUID thiefId = UUID.randomUUID();
        GameBProfile owner = service.profile(ownerId, "Owner");
        GameBProfile thief = service.profile(thiefId, "Thief");
        owner.plot(FarmType.LIFE_TREE).setStage(PlotStage.MATURE);

        assertEquals(GameBService.Result.OK, service.steal(thief, thiefId, ownerId, FarmType.LIFE_TREE, 10_000L));
        assertEquals(PlotStage.MATURE, owner.plot(FarmType.LIFE_TREE).getStage());
        assertEquals(1, thief.itemCount(FarmType.LIFE_TREE.id()));
        assertEquals(GameBService.Result.COOLDOWN, service.steal(thief, thiefId, ownerId, FarmType.LIFE_TREE, 10_001L));
    }

    @Test
    void leaderboardUsesLifetimeSalesNotCurrentBalance() {
        GameBProfile lowerBalance = service.profile(UUID.randomUUID(), "Alpha");
        lowerBalance.setCoins(0);
        lowerBalance.setTotalEarned(300);
        GameBProfile higherBalance = service.profile(UUID.randomUUID(), "Beta");
        higherBalance.setCoins(999);
        higherBalance.setTotalEarned(100);

        assertEquals("Alpha", service.leaderboard().get(0).getValue().getLastKnownName());
    }
}
