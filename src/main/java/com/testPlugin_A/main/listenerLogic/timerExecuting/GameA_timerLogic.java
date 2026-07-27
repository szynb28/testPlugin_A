package com.testPlugin_A.main.listenerLogic.timerExecuting;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class GameA_timerLogic {
    DataInitiator data; // 所附属的数据核心

    public GameA_timerLogic(DataInitiator data){
        this.data = data;
    }

    // 饼干点击游戏- 每秒更新时主逻辑
    public void logic(){
        // 遍历所有在线玩家
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID playerUUID = player.getUniqueId();

        }
    }
}
