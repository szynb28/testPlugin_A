package com.testPlugin_A.gameb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameBProfile {
    private String lastKnownName = "未知玩家";
    private long coins;
    private long totalEarned;
    private Set<String> unlockedFarmIds = new HashSet<>();
    private String activeFarmId = FarmType.LIFE_TREE.id();
    private Map<String, PlotState> plots = new HashMap<>();
    private Map<String, Integer> warehouse = new HashMap<>();
    private Map<String, Integer> fertilizers = new HashMap<>();
    private long stealCooldownUntil;

    public GameBProfile() {
        unlockedFarmIds.add(FarmType.LIFE_TREE.id());
    }

    public String getLastKnownName() { return lastKnownName; }
    public void setLastKnownName(String lastKnownName) { if (lastKnownName != null && !lastKnownName.isBlank()) this.lastKnownName = lastKnownName; }
    public long getCoins() { return coins; }
    public void setCoins(long coins) { this.coins = Math.max(0, coins); }
    public long getTotalEarned() { return totalEarned; }
    public void setTotalEarned(long totalEarned) { this.totalEarned = Math.max(0, totalEarned); }
    public Set<String> getUnlockedFarmIds() {
        if (unlockedFarmIds == null) unlockedFarmIds = new HashSet<>();
        unlockedFarmIds.add(FarmType.LIFE_TREE.id());
        return unlockedFarmIds;
    }
    public void setUnlockedFarmIds(Set<String> ids) { unlockedFarmIds = ids == null ? new HashSet<>() : ids; getUnlockedFarmIds(); }
    public String getActiveFarmId() { return activeFarmId == null ? FarmType.LIFE_TREE.id() : activeFarmId; }
    public void setActiveFarmId(String activeFarmId) { this.activeFarmId = activeFarmId; }
    public Map<String, PlotState> getPlots() { if (plots == null) plots = new HashMap<>(); return plots; }
    public void setPlots(Map<String, PlotState> plots) { this.plots = plots == null ? new HashMap<>() : plots; }
    public Map<String, Integer> getWarehouse() { if (warehouse == null) warehouse = new HashMap<>(); return warehouse; }
    public void setWarehouse(Map<String, Integer> warehouse) { this.warehouse = warehouse == null ? new HashMap<>() : warehouse; }
    public Map<String, Integer> getFertilizers() { if (fertilizers == null) fertilizers = new HashMap<>(); return fertilizers; }
    public void setFertilizers(Map<String, Integer> fertilizers) { this.fertilizers = fertilizers == null ? new HashMap<>() : fertilizers; }
    public long getStealCooldownUntil() { return stealCooldownUntil; }
    public void setStealCooldownUntil(long stealCooldownUntil) { this.stealCooldownUntil = stealCooldownUntil; }

    public boolean hasFarm(FarmType farm) { return getUnlockedFarmIds().contains(farm.id()); }
    public PlotState plot(FarmType farm) { return getPlots().computeIfAbsent(farm.id(), ignored -> new PlotState()); }
    public int itemCount(String cropId) { return getWarehouse().getOrDefault(cropId, 0); }
    public int fertilizerCount(FertilizerTier tier) { return getFertilizers().getOrDefault(tier.id(), 0); }
}
