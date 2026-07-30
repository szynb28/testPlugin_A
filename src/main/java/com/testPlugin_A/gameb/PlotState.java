package com.testPlugin_A.gameb;

public class PlotState {
    private PlotStage stage = PlotStage.WILDERNESS;
    private long finishAt;
    private String fertilizerId = FertilizerTier.NONE.id();

    public PlotStage getStage() { return stage == null ? PlotStage.WILDERNESS : stage; }
    public void setStage(PlotStage stage) { this.stage = stage; }
    public long getFinishAt() { return finishAt; }
    public void setFinishAt(long finishAt) { this.finishAt = finishAt; }
    public String getFertilizerId() { return fertilizerId == null ? FertilizerTier.NONE.id() : fertilizerId; }
    public void setFertilizerId(String fertilizerId) { this.fertilizerId = fertilizerId; }
}
