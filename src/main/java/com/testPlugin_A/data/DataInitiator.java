package com.testPlugin_A.data;

import org.bukkit.entity.Player;

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
    public HashMap<UUID, Integer> gameA_maowoAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_maowoCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_zhuangyuanAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_zhuangyuanCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_luzaoAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_luzaoCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_kejiAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_kejiCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_gongchangAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_gongchangCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_fuwenAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_fuwenCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_shuijingAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_shuijingCost; // 玩家购买猫猫的费用
    public HashMap<UUID, Integer> gameA_huojianAmount; // 玩家持有的猫猫数量
    public HashMap<UUID, Double> gameA_huojianCost; // 玩家购买猫猫的费用

    // gameB- 生命之树
    public HashMap<UUID, String> gameB_plantStage; // 生命之树的种植阶段
    public HashMap<UUID, Double> gameB_shovelTimer; // 玩家挖掘树穴计时器
    public HashMap<UUID, Boolean> gameB_isShovelFinished; // 玩家挖掘树穴是否完成

    // 存储器引用
    public DataStorage storage;

    public DataInitiator(){
        // 初始化 全局内容
        scene = new HashMap<>();
        // 初始化 gameA- 曲奇点击
        gameA_cookieAmount = new HashMap<>();
        gameA_cookiePerSecond = new HashMap<>();

        gameA_maomaoAmount = new HashMap<>();
        gameA_maomaoCost = new HashMap<>();
        gameA_maowoAmount = new HashMap<>();
        gameA_maowoCost = new HashMap<>();
        gameA_zhuangyuanAmount = new HashMap<>();
        gameA_zhuangyuanCost = new HashMap<>();
        gameA_luzaoAmount = new HashMap<>();
        gameA_luzaoCost = new HashMap<>();
        gameA_kejiAmount = new HashMap<>();
        gameA_kejiCost = new HashMap<>();
        gameA_gongchangAmount = new HashMap<>();
        gameA_gongchangCost = new HashMap<>();
        gameA_fuwenAmount = new HashMap<>();
        gameA_fuwenCost = new HashMap<>();
        gameA_shuijingAmount = new HashMap<>();
        gameA_shuijingCost = new HashMap<>();
        gameA_huojianAmount = new HashMap<>();
        gameA_huojianCost = new HashMap<>();
        // 初始化 gameB- 生命之树
        gameB_plantStage = new HashMap<>();
        gameB_shovelTimer = new HashMap<>();
        gameB_isShovelFinished = new HashMap<>();
    }

    // 初始化全局内容的值
    public void initGlobal(Player player){

    }
    // 初始化gameA的值
    public void initGameA(Player player){

    }
    // 初始化gameB的值
    public void initGameB(Player player){

    }
}
