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
        // 判断点击猫窝购买按钮
        else if (clickedItem.getType() == Material.BRICKS){
            UUID playerUUID = player.getUniqueId();
            buyMaowo(player);
        }
        // 判断点击庄园购买按钮
        else if (clickedItem.getType() == Material.BELL){
            UUID playerUUID = player.getUniqueId();
            buyZhuangyuan(player);
        }
        // 判断点击炉灶购买按钮
        else if (clickedItem.getType() == Material.FURNACE_MINECART){
            UUID playerUUID = player.getUniqueId();
            buyLuzao(player);
        }
        // 判断点击科技购买按钮
        else if (clickedItem.getType() == Material.REDSTONE){
            UUID playerUUID = player.getUniqueId();
            buyKeji(player);
        }
        // 判断点击工厂购买按钮
        else if (clickedItem.getType() == Material.WAXED_CHISELED_COPPER){
            UUID playerUUID = player.getUniqueId();
            buyGongchang(player);
        }
        // 判断点击符文购买按钮
        else if (clickedItem.getType() == Material.FLOW_POTTERY_SHERD){
            UUID playerUUID = player.getUniqueId();
            buyFuwen(player);
        }
        // 判断点击水晶购买按钮
        else if (clickedItem.getType() == Material.AMETHYST_SHARD){
            UUID playerUUID = player.getUniqueId();
            buyShuijing(player);
        }
        // 判断点击火箭购买按钮
        else if (clickedItem.getType() == Material.FIREWORK_ROCKET){
            UUID playerUUID = player.getUniqueId();
            buyHuojian(player);
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
            currentMaomaoCost *= GAME_A_DEFAULT_MAOMAO_PIM;
            data.gameA_maomaoCost.put(playerUUID, toTwoDecimalPlaces(currentMaomaoCost));
            // 计算并应用购买后的猫猫数量
            int currentMaomaoAmount = data.gameA_maomaoAmount.getOrDefault(playerUUID, 0);
            currentMaomaoAmount += 1;
            data.gameA_maomaoAmount.put(playerUUID, currentMaomaoAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_MAOMAO_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 36);
        }
    }
    // 购买猫窝逻辑
    public void buyMaowo(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_maowoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_maowoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的猫窝花费
            double currentMaowoCost = data.gameA_maowoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_COST);
            currentMaowoCost *= GAME_A_DEFAULT_MAOWO_PIM;
            data.gameA_maowoCost.put(playerUUID, toTwoDecimalPlaces(currentMaowoCost));
            // 计算并应用购买后的猫窝数量
            int currentMaowoAmount = data.gameA_maowoAmount.getOrDefault(playerUUID, 0);
            currentMaowoAmount += 1;
            data.gameA_maowoAmount.put(playerUUID, currentMaowoAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_MAOWO_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 37);
        }
    }
    // 购买庄园逻辑
    public void buyZhuangyuan(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_zhuangyuanCost.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_zhuangyuanCost.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的庄园花费
            double currentZhuangyuanCost = data.gameA_zhuangyuanCost.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_COST);
            currentZhuangyuanCost *= GAME_A_DEFAULT_ZHUANGYUAN_PIM;
            data.gameA_zhuangyuanCost.put(playerUUID, toTwoDecimalPlaces(currentZhuangyuanCost));
            // 计算并应用购买后的庄园数量
            int currentZhuangyuanAmount = data.gameA_zhuangyuanAmount.getOrDefault(playerUUID, 0);
            currentZhuangyuanAmount += 1;
            data.gameA_zhuangyuanAmount.put(playerUUID, currentZhuangyuanAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_ZHUANGYUAN_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 38);
        }
    }
    // 购买炉灶逻辑
    public void buyLuzao(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_luzaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_luzaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的炉灶花费
            double currentLuzaoCost = data.gameA_luzaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_COST);
            currentLuzaoCost *= GAME_A_DEFAULT_LUZAO_PIM;
            data.gameA_luzaoCost.put(playerUUID, toTwoDecimalPlaces(currentLuzaoCost));
            // 计算并应用购买后的炉灶数量
            int currentLuzaoAmount = data.gameA_luzaoAmount.getOrDefault(playerUUID, 0);
            currentLuzaoAmount += 1;
            data.gameA_luzaoAmount.put(playerUUID, currentLuzaoAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_LUZAO_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 39);
        }
    }
    // 购买科技逻辑
    public void buyKeji(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_kejiCost.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_kejiCost.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的科技花费
            double currentKejiCost = data.gameA_kejiCost.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_COST);
            currentKejiCost *= GAME_A_DEFAULT_KEJI_PIM;
            data.gameA_kejiCost.put(playerUUID, toTwoDecimalPlaces(currentKejiCost));
            // 计算并应用购买后的科技数量
            int currentKejiAmount = data.gameA_kejiAmount.getOrDefault(playerUUID, 0);
            currentKejiAmount += 1;
            data.gameA_kejiAmount.put(playerUUID, currentKejiAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_KEJI_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 40);
        }
    }
    // 购买工厂逻辑
    public void buyGongchang(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_gongchangCost.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_gongchangCost.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的工厂花费
            double currentGongchangCost = data.gameA_gongchangCost.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_COST);
            currentGongchangCost *= GAME_A_DEFAULT_GONGCHANG_PIM;
            data.gameA_gongchangCost.put(playerUUID, toTwoDecimalPlaces(currentGongchangCost));
            // 计算并应用购买后的工厂数量
            int currentGongchangAmount = data.gameA_gongchangAmount.getOrDefault(playerUUID, 0);
            currentGongchangAmount += 1;
            data.gameA_gongchangAmount.put(playerUUID, currentGongchangAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_GONGCHANG_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 41);
        }
    }
    // 购买符文逻辑
    public void buyFuwen(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_fuwenCost.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_fuwenCost.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的符文花费
            double currentFuwenCost = data.gameA_fuwenCost.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_COST);
            currentFuwenCost *= GAME_A_DEFAULT_FUWEN_PIM;
            data.gameA_fuwenCost.put(playerUUID, toTwoDecimalPlaces(currentFuwenCost));
            // 计算并应用购买后的符文数量
            int currentFuwenAmount = data.gameA_fuwenAmount.getOrDefault(playerUUID, 0);
            currentFuwenAmount += 1;
            data.gameA_fuwenAmount.put(playerUUID, currentFuwenAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_FUWEN_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 42);
        }
    }
    // 购买水晶逻辑
    public void buyShuijing(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_shuijingCost.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_shuijingCost.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的水晶花费
            double currentShuijingCost = data.gameA_shuijingCost.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_COST);
            currentShuijingCost *= GAME_A_DEFAULT_SHUIJING_PIM;
            data.gameA_shuijingCost.put(playerUUID, toTwoDecimalPlaces(currentShuijingCost));
            // 计算并应用购买后的水晶数量
            int currentShuijingAmount = data.gameA_shuijingAmount.getOrDefault(playerUUID, 0);
            currentShuijingAmount += 1;
            data.gameA_shuijingAmount.put(playerUUID, currentShuijingAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_SHUIJING_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 43);
        }
    }
    // 购买火箭逻辑
    public void buyHuojian(Player player){
        UUID playerUUID = player.getUniqueId();
        double currentCookieAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
        // 当资源足够的时候，允许购买
        if (currentCookieAmount >= data.gameA_huojianCost.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_COST)){
            // 计算并应用购买后的余额饼干
            currentCookieAmount -= data.gameA_huojianCost.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_COST);
            data.gameA_cookieAmount.put(playerUUID, toTwoDecimalPlaces(currentCookieAmount));
            // 计算并应用购买后的火箭花费
            double currentHuojianCost = data.gameA_huojianCost.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_COST);
            currentHuojianCost *= GAME_A_DEFAULT_HUOJIAN_PIM;
            data.gameA_huojianCost.put(playerUUID, toTwoDecimalPlaces(currentHuojianCost));
            // 计算并应用购买后的火箭数量
            int currentHuojianAmount = data.gameA_huojianAmount.getOrDefault(playerUUID, 0);
            currentHuojianAmount += 1;
            data.gameA_huojianAmount.put(playerUUID, currentHuojianAmount);
            // 计算并应用购买后的每秒饼干增益
            double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            currentCookiePerSecond += GAME_A_DEFAULT_HUOJIAN_OPS;
            data.gameA_cookiePerSecond.put(playerUUID, toTwoDecimalPlaces(currentCookiePerSecond));
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
        // 否则，不允许购买，显示错误提示
        else {
            replaceByBuyingErrorPrompt(player, 44);
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
        double currentCookiePerSecond = data.gameA_cookiePerSecond.getOrDefault(playerUUID, 0.00);
        int currentMaomaoAmount = data.gameA_maomaoAmount.getOrDefault(playerUUID, 0);
        double currentMaomaoCost = data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST);
        int currentMaowoAmount = data.gameA_maowoAmount.getOrDefault(playerUUID, 0);
        double currentMaowoCost = data.gameA_maowoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_COST);
        int currentZhuangyuanAmount = data.gameA_zhuangyuanAmount.getOrDefault(playerUUID, 0);
        double currentZhuangyuanCost = data.gameA_zhuangyuanCost.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_COST);
        int currentLuzaoAmount = data.gameA_luzaoAmount.getOrDefault(playerUUID, 0);
        double currentLuzaoCost = data.gameA_luzaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_COST);
        int currentKejiAmount = data.gameA_kejiAmount.getOrDefault(playerUUID, 0);
        double currentKejiCost = data.gameA_kejiCost.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_COST);
        int currentGongchangAmount = data.gameA_gongchangAmount.getOrDefault(playerUUID, 0);
        double currentGongchangCost = data.gameA_gongchangCost.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_COST);
        int currentFuwenAmount = data.gameA_fuwenAmount.getOrDefault(playerUUID, 0);
        double currentFuwenCost = data.gameA_fuwenCost.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_COST);
        int currentShuijingAmount = data.gameA_shuijingAmount.getOrDefault(playerUUID, 0);
        double currentShuijingCost = data.gameA_shuijingCost.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_COST);
        int currentHuojianAmount = data.gameA_huojianAmount.getOrDefault(playerUUID, 0);
        double currentHuojianCost = data.gameA_huojianCost.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_COST);
        inv.setItem(22, get__item_gameAcookie(currentCookieAmount, currentCookiePerSecond));
        inv.setItem(36, get__item_gameAmaomao(currentMaomaoAmount, currentMaomaoCost));
        inv.setItem(37, get__item_gameAmaowo(currentMaowoAmount, currentMaowoCost));
        inv.setItem(38, get__item_gameAzhuangyuan(currentZhuangyuanAmount, currentZhuangyuanCost));
        inv.setItem(39, get__item_gameAluzao(currentLuzaoAmount, currentLuzaoCost));
        inv.setItem(40, get__item_gameAkeji(currentKejiAmount, currentKejiCost));
        inv.setItem(41, get__item_gameAgongchang(currentGongchangAmount, currentGongchangCost));
        inv.setItem(42, get__item_gameAfuwen(currentFuwenAmount, currentFuwenCost));
        inv.setItem(43, get__item_gameAshuijing(currentShuijingAmount, currentShuijingCost));
        inv.setItem(44, get__item_gameAhuojian(currentHuojianAmount, currentHuojianCost));
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
