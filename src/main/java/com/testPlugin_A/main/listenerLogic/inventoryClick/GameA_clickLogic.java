package com.testPlugin_A.main.listenerLogic.inventoryClick;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InteractorPack.*;
import static com.testPlugin_A.main.packs.InventoriesPack.*;

public class GameA_clickLogic {
    InventoryClickEvent clickEvent; // 所附属的点击事件
    DataInitiator data; // 所附属的数据核心

    public GameA_clickLogic(InventoryClickEvent clickEvent, DataInitiator data){
        this.clickEvent = clickEvent;
        this.data = data;
    }

    // 饼干点击游戏- 点击主逻辑
    public void logic(){
        // 取消点击(防止拿出物品)
        clickEvent.setCancelled(true);

        Player player = (Player) clickEvent.getWhoClicked();
        ItemStack clickedItem = clickEvent.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // 判断点击曲奇按钮
        if (clickedItem.getType() == Material.COOKIE){
            UUID playerUUID = player.getUniqueId();
            addCookie(playerUUID, 1);
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
    }
    // 增加曲奇
    public void addCookie(UUID playerUUID, double amount){
        double currentAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        double calculatedAmount = currentAmount + amount;

        // 保留两位小数
        BigDecimal bd = new BigDecimal(calculatedAmount);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double result = bd.doubleValue();

        data.gameA_cookieAmount.put(playerUUID, result);
    }
    // 更新菜单
    public void updateGUI(Player player){
        UUID playerUUID = player.getUniqueId();
        Inventory gameInv = get__inventory_gameA(data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00),
                data.gameA_maomaoAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_AMOUNT),
                data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST));
        player.openInventory(gameInv);
    }
}
