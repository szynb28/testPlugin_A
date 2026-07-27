package com.testPlugin_A.main.listenerLogic;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.main.Main;
import com.testPlugin_A.main.listenerLogic.inventoryClick.GameA_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClick.HelpMenu_clickLogic;
import com.testPlugin_A.main.listenerLogic.timerExecuting.GameA_timerLogic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
}
