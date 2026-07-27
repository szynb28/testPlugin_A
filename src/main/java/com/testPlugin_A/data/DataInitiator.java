package com.testPlugin_A.data;

import java.util.HashMap;
import java.util.UUID;

public class DataInitiator {
    // 全局内容
    public HashMap<UUID, String> scene; // 当前玩家打开页面的场景状态

    // gameA- 曲奇点击
    public HashMap<UUID, Double> gameA_cookieAmount; // 玩家持有的曲奇数量
    public HashMap<UUID, Double> gameA_cookiePerSecond; // 玩家每秒增加的曲奇数量

    public HashMap<UUID, Integer> gameA_maomaoAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_maomaoCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Boolean> gameA_isShowMaomaoBuyingError; // 是否显示猫猫购买失败的提示

    public DataInitiator(){
        // 初始化 全局内容
        scene = new HashMap<>();
        // 初始化 gameA- 曲奇点击
        gameA_cookieAmount = new HashMap<>();
        gameA_cookiePerSecond = new HashMap<>();

        gameA_maomaoAmount = new HashMap<>();
        gameA_maomaoCost = new HashMap<>();
        gameA_isShowMaomaoBuyingError = new HashMap<>();
    }
}
