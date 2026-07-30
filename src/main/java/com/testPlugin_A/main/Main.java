package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.data.DataStorage;
import com.testPlugin_A.gameb.GameBConfig;
import com.testPlugin_A.gameb.GameBRepository;
import com.testPlugin_A.gameb.GameBService;
import com.testPlugin_A.gameb.GameBStorage;
import com.testPlugin_A.gameb.gui.GameBMenus;
import com.testPlugin_A.main.listenerLogic.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    public static Main main;
    public DataInitiator data;
    private Listener listener;
    private TestCommand testCommand;
    public DataStorage storage;
    public GameBStorage gameBStorage;
    public GameBRepository gameBRepository;
    public GameBService gameBService;
    public GameBMenus gameBMenus;

    @Override
    public void onEnable() {
        main = this; // 先给main赋值

        // 初始化数据
        data = new DataInitiator();
        storage = new DataStorage(this);
        data.storage = storage;

        // 配置先落盘，再让游戏 B 读取可调平衡参数
        saveDefaultConfig();
        gameBRepository = new GameBRepository();
        gameBStorage = new GameBStorage(this);
        gameBStorage.load(gameBRepository);
        gameBService = new GameBService(gameBRepository, GameBConfig.from(getConfig()));
        gameBMenus = new GameBMenus(this, gameBService);

        // 加载已有数据（没有就保持默认）
        storage.load(data);

        // 创建command和listener实例
        listener = new Listener(data, gameBService, gameBMenus);
        testCommand = new TestCommand(data, gameBService, gameBMenus);

        // 传递同一个 dataInitiator 给 Listener 和 TestCommand
        Bukkit.getPluginCommand("testCommand").setExecutor(testCommand);
        Bukkit.getPluginManager().registerEvents(listener, this);

        // 调用随时间流逝持续执行的逻辑
        listener.timerLogic();
        listener.timerLogic_forTest();

        // 载具实验功能依赖 PacketEvents，当前不在正式插件启动流程中注册。

        // 每分钟自动保存一次
        new BukkitRunnable(){
            @Override
            public void run(){
                storage.save(data);
                gameBStorage.save(gameBRepository);
                getLogger().info("小游戏数据已自动保存");
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
            gameBStorage.save(gameBRepository);
            getLogger().info("gameA数据已保存（插件关闭）");
        }

        System.out.println("==================================");
        System.out.println("TestPlugin_A.jar 成功关闭");
        System.out.println("==================================");
    }
}
