package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.main.listenerLogic.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main main;
    private DataInitiator dataInitiator;

    @Override
    public void onEnable() {
        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 运行成功");
        System.out.println("==================================");

        // 创建唯一的数据核心实例
        dataInitiator = new DataInitiator();

        // 传递同一个 dataInitiator 给 Listener 和 TestCommand
        Bukkit.getPluginCommand("testCommand").setExecutor(new TestCommand(dataInitiator));
        Bukkit.getPluginManager().registerEvents(new Listener(dataInitiator), this);

        // 生成配置文件
        saveDefaultConfig();

        main = this;
    }

    @Override
    public void onDisable() {
        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 成功关闭");
        System.out.println("==================================");
    }
}
