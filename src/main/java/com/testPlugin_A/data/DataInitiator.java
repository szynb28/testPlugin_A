package com.testPlugin_A.data;

import java.util.HashMap;
import java.util.UUID;

public class DataInitiator {
    // gameA- 曲奇点击
    public HashMap<UUID, Double> gameA_cookieAmount; // 玩家持有的曲奇数量
    public HashMap<UUID, Double> gameA_cookiePerSecond; // 玩家每秒增加的曲奇数量

    public HashMap<UUID, Integer> gameA_maomaoAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_maomaoCost; // 玩家购买猫猫的费用

    public DataInitiator(){
        // 初始化 gameA- 曲奇点击
        gameA_cookieAmount = new HashMap<>();
        gameA_cookiePerSecond = new HashMap<>();

        gameA_maomaoAmount = new HashMap<>();
        gameA_maomaoCost = new HashMap<>();
    }
}
