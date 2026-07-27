package com.testPlugin_A.main.listenerLogic.inventoryClick;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InteractorPack.*;
import static com.testPlugin_A.main.packs.InventoriesPack.*;
import static com.testPlugin_A.main.packs.ItemsPack.*;

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
        // 判断点击猫猫购买按钮
        if (clickedItem.getType() == Material.COD){
            UUID playerUUID = player.getUniqueId();
            buyMaomao(player);
        }
    }
    // 增加曲奇
    public void addCookie(UUID playerUUID, double amount){
        double currentAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        double calculatedAmount = currentAmount + amount;

        data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(calculatedAmount));
    }
    // 购买猫猫逻辑
    public void buyMaomao(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的猫猫花费
            double currentMaomaoCost = data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST);
            currentMaomaoCost *= 1.18;
            data.gameA_maomaoCost.put(playerUUID, toTwoDecimalPlaces(currentMaomaoCost));
            // 计算并应用购买后的猫猫数量
            int currentMaomaoAmount = data.gameA_maomaoAmount.getOrDefault(playerUUID, 0);
            currentMaomaoAmount += 1;
            data.gameA_maomaoAmount.put(playerUUID, currentMaomaoAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += 1;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            data.gameA_isShowMaomaoBuyingError.put(playerUUID, true);
            replaceByBuyingErrorPrompt(player, 36);
        }
    }
    // 更新菜单
    public void updateGUI(Player player){
        UUID playerUUID = player.getUniqueId();
        // 获取玩家当前打开的界面与容器
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        // 更新容器内物品与显示内容
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        int currentMaomaoAmount = data.gameA_maomaoAmount.getOrDefault(playerUUID, 0);
        double currentMaomaoCost = data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST);
        inv.setItem(22, get__item_gameAcookie(currentCookieAmount));
        inv.setItem(36, get__item_gameAmaomao(currentMaomaoAmount, currentMaomaoCost));
    }
    // 将当前槽位替换为购买失败提示
    public void replaceByBuyingErrorPrompt(Player player, int slotNumber){
        // 获取玩家当前打开的界面与容器
        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        // 更新容器内物品与显示内容
        inv.setItem(slotNumber, get__item_buyError());
    }
    // 数值保留两位小数的方法
    public double toTwoDecimalPlaces(double value){
        // 保留两位小数
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        double result = bd.doubleValue();
        return result;
    }
}
