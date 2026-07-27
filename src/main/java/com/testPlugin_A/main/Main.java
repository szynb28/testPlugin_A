package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.main.listenerLogic.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main main;
    private DataInitiator dataInitiator;
    private Listener listener;
    private TestCommand testCommand;

    @Override
    public void onEnable() {
        main = this; // 先给main赋值

        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 运行成功");
        System.out.println("==================================");

        // 创建唯一的数据核心实例
        dataInitiator = new DataInitiator();

        // 创建command和listener实例
        listener = new Listener(dataInitiator);
        testCommand = new TestCommand(dataInitiator);

        // 传递同一个 dataInitiator 给 Listener 和 TestCommand
        Bukkit.getPluginCommand("testCommand").setExecutor(testCommand);
        Bukkit.getPluginManager().registerEvents(listener, this);

        // 调用随时间流逝持续执行的逻辑
        listener.timerLogic();

        // 生成配置文件
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 成功关闭");
        System.out.println("==================================");
    }
}
