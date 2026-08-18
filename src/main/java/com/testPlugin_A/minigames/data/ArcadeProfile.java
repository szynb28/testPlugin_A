package com.testPlugin_A.minigames.data;

/** 一个玩家在五个新小游戏中的全部持久化档案。 */
public class ArcadeProfile {
    public String lastKnownName = "未知玩家";
    public PetData pet = new PetData();
    public SnakeData snake = new SnakeData();
    public MinerData miner = new MinerData();
    public FishingData fishing = new FishingData();
    public AlchemyData alchemy = new AlchemyData();

    public void repair() {
        if (pet == null) pet = new PetData();
        if (snake == null) snake = new SnakeData();
        if (miner == null) miner = new MinerData();
        if (fishing == null) fishing = new FishingData();
        if (alchemy == null) alchemy = new AlchemyData();
    }
}
