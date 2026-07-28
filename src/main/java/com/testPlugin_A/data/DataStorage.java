package com.testPlugin_A.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class DataStorage {

    private final JavaPlugin plugin;
    private final Gson gson;
    private final File dataFile;

    public DataStorage(JavaPlugin plugin){
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.dataFile = new File(plugin.getDataFolder(), "gameA_data.json");

    }

    // ========== 保存 ==========
    public void save(DataInitiator data){
        // 确保文件夹存在
        if (!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdirs();
        }

        // 把所有数据打包成一个对象（UUID 转 String 做 key）
        SaveData saveData = new SaveData();
        saveData.cookieAmount = uuidMapToString(data.gameA_cookieAmount);
        saveData.cookiePerSecond = uuidMapToString(data.gameA_cookiePerSecond);
        saveData.maomaoAmount = uuidMapToString(data.gameA_maomaoAmount);
        saveData.maomaoCost = uuidMapToString(data.gameA_maomaoCost);
        saveData.maowoAmount = uuidMapToString(data.gameA_maowoAmount);
        saveData.maowoCost = uuidMapToString(data.gameA_maowoCost);
        saveData.zhuangyuanAmount = uuidMapToString(data.gameA_zhuangyuanAmount);
        saveData.zhuangyuanCost = uuidMapToString(data.gameA_zhuangyuanCost);
        saveData.luzaoAmount = uuidMapToString(data.gameA_luzaoAmount);
        saveData.luzaoCost = uuidMapToString(data.gameA_luzaoCost);
        saveData.kejiAmount = uuidMapToString(data.gameA_kejiAmount);
        saveData.kejiCost = uuidMapToString(data.gameA_kejiCost);
        saveData.gongchangAmount = uuidMapToString(data.gameA_gongchangAmount);
        saveData.gongchangCost = uuidMapToString(data.gameA_gongchangCost);
        saveData.fuwenAmount = uuidMapToString(data.gameA_fuwenAmount);
        saveData.fuwenCost = uuidMapToString(data.gameA_fuwenCost);
        saveData.shuijingAmount = uuidMapToString(data.gameA_shuijingAmount);
        saveData.shuijingCost = uuidMapToString(data.gameA_shuijingCost);
        saveData.huojianAmount = uuidMapToString(data.gameA_huojianAmount);
        saveData.huojianCost = uuidMapToString(data.gameA_huojianCost);
        saveData.scene = uuidMapToString(data.scene);

        try (Writer writer = new FileWriter(dataFile)){
            gson.toJson(saveData, writer);
        } catch (IOException e){
            plugin.getLogger().severe("保存 gameA 数据失败：" + e.getMessage());
        }
    }

    // ========== 加载 ==========
    public void load(DataInitiator data){
        if (!dataFile.exists()){
            plugin.getLogger().info("未找到数据文件，使用默认初始化");
            return; // 文件不存在，保持默认值
        }

        try (Reader reader = new FileReader(dataFile)){
            SaveData loaded = gson.fromJson(reader, SaveData.class);

            // 转回 UUID key
            data.gameA_cookieAmount.putAll(stringMapToUuid(loaded.cookieAmount));
            data.gameA_cookiePerSecond.putAll(stringMapToUuid(loaded.cookiePerSecond));
            data.gameA_maomaoAmount.putAll(stringMapToUuid(loaded.maomaoAmount));
            data.gameA_maomaoCost.putAll(stringMapToUuid(loaded.maomaoCost));
            data.gameA_maowoAmount.putAll(stringMapToUuid(loaded.maowoAmount));
            data.gameA_maowoCost.putAll(stringMapToUuid(loaded.maowoCost));
            data.gameA_zhuangyuanAmount.putAll(stringMapToUuid(loaded.zhuangyuanAmount));
            data.gameA_zhuangyuanCost.putAll(stringMapToUuid(loaded.zhuangyuanCost));
            data.gameA_luzaoAmount.putAll(stringMapToUuid(loaded.luzaoAmount));
            data.gameA_luzaoCost.putAll(stringMapToUuid(loaded.luzaoCost));
            data.gameA_kejiAmount.putAll(stringMapToUuid(loaded.kejiAmount));
            data.gameA_kejiCost.putAll(stringMapToUuid(loaded.kejiCost));
            data.gameA_gongchangAmount.putAll(stringMapToUuid(loaded.gongchangAmount));
            data.gameA_gongchangCost.putAll(stringMapToUuid(loaded.gongchangCost));
            data.gameA_fuwenAmount.putAll(stringMapToUuid(loaded.fuwenAmount));
            data.gameA_fuwenCost.putAll(stringMapToUuid(loaded.fuwenCost));
            data.gameA_shuijingAmount.putAll(stringMapToUuid(loaded.shuijingAmount));
            data.gameA_shuijingCost.putAll(stringMapToUuid(loaded.shuijingCost));
            data.gameA_huojianAmount.putAll(stringMapToUuid(loaded.huojianAmount));
            data.gameA_huojianCost.putAll(stringMapToUuid(loaded.huojianCost));
            data.scene.putAll(stringMapToUuid(loaded.scene));

            plugin.getLogger().info("成功加载gameA 数据！");
        } catch (IOException e){
            plugin.getLogger().severe("加载 gameA 数据失败：" + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    private <T> Map<String, T> uuidMapToString(Map<UUID, T> map){
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<UUID, T> entry : map.entrySet()){
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    private <T> Map<UUID, T> stringMapToUuid(Map<String, T> map){
        Map<UUID, T> result = new HashMap<>();
        if (map == null) return result;
        for (Map.Entry<String , T> entry : map.entrySet()){
            result.put(UUID.fromString(entry.getKey()), entry.getValue());
        }
        return result;
    }

    // ========== 内部数据类 ==========
    private static class SaveData{
        Map<String, Double> cookieAmount = new HashMap<>();
        Map<String, Double> cookiePerSecond = new HashMap<>();
        Map<String, Integer> maomaoAmount = new HashMap<>();
        Map<String, Double> maomaoCost = new HashMap<>();
        Map<String, Integer> maowoAmount = new HashMap<>();
        Map<String, Double> maowoCost = new HashMap<>();
        Map<String, Integer> zhuangyuanAmount = new HashMap<>();
        Map<String, Double> zhuangyuanCost = new HashMap<>();
        Map<String, Integer> luzaoAmount = new HashMap<>();
        Map<String, Double> luzaoCost = new HashMap<>();
        Map<String, Integer> kejiAmount = new HashMap<>();
        Map<String, Double> kejiCost = new HashMap<>();
        Map<String, Integer> gongchangAmount = new HashMap<>();
        Map<String, Double> gongchangCost = new HashMap<>();
        Map<String, Integer> fuwenAmount = new HashMap<>();
        Map<String, Double> fuwenCost = new HashMap<>();
        Map<String, Integer> shuijingAmount = new HashMap<>();
        Map<String, Double> shuijingCost = new HashMap<>();
        Map<String, Integer> huojianAmount = new HashMap<>();
        Map<String, Double> huojianCost = new HashMap<>();
        Map<String, String> scene = new HashMap<>();
    }
}
