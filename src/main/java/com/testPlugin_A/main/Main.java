package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.data.DataStorage;
import com.testPlugin_A.main.listenerLogic.Listener;
import com.testPlugin_A.main.listenerLogic.VehicleDriveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    public static Main main;
    public DataInitiator data;
    private Listener listener;
    private TestCommand testCommand;
    public DataStorage storage;

    @Override
    public void onEnable() {
        main = this; // 先给main赋值

        // 初始化数据
        data = new DataInitiator();
        storage = new DataStorage(this);
        data.storage = storage;

        // 加载已有数据（没有就保持默认）
        storage.load(data);

        // 创建command和listener实例
        listener = new Listener(data);
        testCommand = new TestCommand(data);

        // 传递同一个 dataInitiator 给 Listener 和 TestCommand
        Bukkit.getPluginCommand("testCommand").setExecutor(testCommand);
        Bukkit.getPluginManager().registerEvents(listener, this);

        // 调用随时间流逝持续执行的逻辑
        listener.timerLogic();
        listener.timerLogic_forTest();

        // PacketEvents的监听逻辑
        com.github.retrooper.packetevents.PacketEvents.getAPI()
                .getEventManager().registerListener(new VehicleDriveListener(data));

        // 生成配置文件
        saveDefaultConfig();

        // 每分钟自动保存一次
        new BukkitRunnable(){
            @Override
            public void run(){
                storage.save(data);
                getLogger().info("gameA 数据已自动保存");
            }
        }.runTaskTimer(this, 1200L, 1200L);  // 1200 ticks = 60秒 = 1分钟

        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 运行成功");
        System.out.println("==================================");
    }

    @Override
    public void onDisable() {

        // 插件关闭时强制保存一次，防止丢数据
        if (storage != null && data != null){
            storage.save(data);
            getLogger().info("gameA数据已保存（插件关闭）");
        }

        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 成功关闭");
        System.out.println("==================================");
    }
}
