package com.testPlugin_A.main.listenerLogic.inventoryClose;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class All_CloseLogic {
    InventoryCloseEvent closeEvent; // 所附属的容器退出事件
    DataInitiator data; // 所附属的数据核心

    public All_CloseLogic(InventoryCloseEvent closeEvent, DataInitiator data){
        this.closeEvent = closeEvent;
        this.data = data;
    }

    // 退出容器时的逻辑
    public void logic(){
        Player player = (Player) closeEvent.getPlayer();
        UUID playerUUID = player.getUniqueId();
        data.scene.put(playerUUID, "Minecraft");
    }
}
