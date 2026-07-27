package com.testPlugin_A.main.listenerLogic.inventoryClick;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.testPlugin_A.main.packs.InteractorPack.get__book_menuHelp;

public class HelpMenu_clickLogic {
    InventoryClickEvent clickEvent; // 所附属的点击事件

    public HelpMenu_clickLogic(InventoryClickEvent clickEvent){
        this.clickEvent = clickEvent;
    }

    public void logic(){
        // 取消点击(防止拿出物品)
        clickEvent.setCancelled(true);

        Player player = (Player) clickEvent.getWhoClicked();
        ItemStack clickedItem = clickEvent.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // 判断是不是书本帮助按钮
        if (clickedItem.getType() == Material.BOOK){
            // 关闭当前GUI
            player.closeInventory();
            // 打开书页面
            player.openBook(get__book_menuHelp());
        }
    }
}
