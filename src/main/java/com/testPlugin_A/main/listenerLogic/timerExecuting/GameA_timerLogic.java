package com.testPlugin_A.main.listenerLogic.timerExecuting;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InventoriesPack.get__inventory_gameA;
import static com.testPlugin_A.main.packs.ItemsPack.*;
import static com.testPlugin_A.main.packs.ItemsPack.get__item_gameAhuojian;

public class GameA_timerLogic {
    DataInitiator data; // 所附属的数据核心

    public GameA_timerLogic(DataInitiator data){
        this.data = data;
    }

    // 饼干点击游戏- 每秒更新时主逻辑
    public void logic(){
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID playerUUID = player.getUniqueId();
            // ★★玩家饼干每秒增加逻辑★★
            double currentAmount = data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00);
            double deltaAmount = data.gameA_cookiePerSecond.getOrDefault(playerUUID, GAME_A_DEFAULT_COOKIE_PER_SECOND);
            // 只有当玩家有每秒增益的时候，再执行增益逻辑。优化性能
            if (deltaAmount > 0){
                double calculatedAmount = currentAmount + deltaAmount;

                // 保留两位小数
                BigDecimal bd = new BigDecimal(calculatedAmount);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                double result = bd.doubleValue();

                data.gameA_cookieAmount.put(playerUUID, result);
            }
            // 只有在当前玩家页面场景在gameA(饼干点击)的时候更新GUI，刷新数字显示等内容。优化性能
            if ("gameA".equals(data.scene.get(playerUUID))){
                updateGUI(player);
            }
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
}
