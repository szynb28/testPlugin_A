package com.testPlugin_A.main.listenerLogic;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.main.Main;
import com.testPlugin_A.main.listenerLogic.inventoryClick.GameA_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClick.HelpMenu_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClose.All_CloseLogic;
import com.testPlugin_A.main.listenerLogic.timerExecuting.GameA_timerLogic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.N;

import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InteractorPack.*;

public class Listener implements org.bukkit.event.Listener {

    private final DataInitiator data;

    public Listener(DataInitiator data){
        this.data = data;
    }

    // 玩家进入游戏执行的逻辑
    @EventHandler
    public void playerJoin(PlayerJoinEvent joinEvent){
        Player player = joinEvent.getPlayer();
        String playerName = player.getName();

        String msg = Main.main.getConfig().getString("Msgs.JoinMessage");
        String replaced = msg.replace("%player%", playerName);
        joinEvent.joinMessage(MiniMessage.miniMessage().deserialize(replaced));
    }

    // 容器点击操作时执行的逻辑
    @EventHandler
    public void itemClick(InventoryClickEvent clickEvent){
        // 只处理 GUI 内的点击
        if (clickEvent.getClickedInventory() == null) return;

        if (clickEvent.getClickedInventory() != clickEvent.getInventory()) return;

        String title = clickEvent.getView().getTitle();

        // 初始化容器逻辑
        HelpMenu_clickLogic helpMenuClickLogic = new HelpMenu_clickLogic(clickEvent);
        GameA_clickLogic gameAClickLogic = new GameA_clickLogic(clickEvent, data);

        // 根据点击的容器种类判断执行哪个容器的逻辑
        if (title.equals(HELP_MENU_TITLE)){
            helpMenuClickLogic.logic();
        }
        else if (title.equals(GAME_A_MENU_TITLE)){
            gameAClickLogic.logic();
        }

    }

    // 随时间流逝持续执行的逻辑
    public void timerLogic(){
        new BukkitRunnable(){
            @Override
            public void run(){
                // 初始化容器逻辑
                GameA_timerLogic gameATimerLogic = new GameA_timerLogic(data);

                // 执行所有与时间流逝逻辑相关的容器逻辑
                gameATimerLogic.logic();
            }
        }.runTaskTimer(Main.main, 0L, 20L); // 20 tick = 1 秒
    }

    // 玩家退出容器时执行的逻辑
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent closeEvent){
        // 初始化容器退出逻辑
        All_CloseLogic allCloseLogic = new All_CloseLogic(closeEvent, data);

        // 执行退出容器时的逻辑
        allCloseLogic.logic();
    }

    // 玩家右键点击实体的逻辑
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        // 获取实体的数据容器
        PersistentDataContainer pdc = clicked.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.main, "reward_entity");

        // 检查是否有我们的标签("reward_entity")
        if (!pdc.has(key, PersistentDataType.BYTE)){ // Q:这咋判断的？ <- A:它直接判断有没有BYTE的数据形式。不是判断值
            return;
        }

        // 取消默认交互（防止打开盔甲架装备栏(*?)）
        event.setCancelled(true);

        // 读取这个实体的命令标签
        NamespacedKey cmdKey = new NamespacedKey(Main.main, "execute_command");
        String command = pdc.get(cmdKey, PersistentDataType.STRING);

        // 让玩家执行命令
        if (command != null){
            player.performCommand(command);
        }

        // 给玩家钻石
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        player.sendMessage("§6✦ 你获得了一颗钻石！");

        // 播放粒子效果
        player.getWorld().spawnParticle(
                Particle.HAPPY_VILLAGER,
                clicked.getLocation().add(0, 1, 0),
                10, 0.3, 0.3, 0.3
        );

        // 读取挂着的盔甲架 UUID，找到并删除
        NamespacedKey standKey = new NamespacedKey(Main.main, "linked_stand");
        String standUuidStr = pdc.get(standKey, PersistentDataType.STRING);
        if (standUuidStr != null){
            UUID standUuid = UUID.fromString(standUuidStr);
            Entity stand = Bukkit.getEntity(standUuid);
            if (stand != null && stand.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                stand.remove(); // 删除盔甲架
            }
        }

        // 读取挂着的展示方块实体 UUID，找到并删除
        NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplayKey");
        String blockDisplayUuidStr = pdc.get(blockDisplayKey, PersistentDataType.STRING);
        if (blockDisplayUuidStr != null){
            UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr);
            Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
            if (blockDisplay != null && blockDisplay.isValid()){
                blockDisplay.remove(); // 删除方块展示实体
            }
        }

        // 删除自己 TODO（领完就消失，也可以不删让它一直存在）
        clicked.remove();
    }
}
