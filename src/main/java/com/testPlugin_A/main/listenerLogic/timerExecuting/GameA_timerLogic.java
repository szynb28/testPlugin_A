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
import static com.testPlugin_A.main.packs.ItemsPack.get__item_gameAcookie;
import static com.testPlugin_A.main.packs.ItemsPack.get__item_gameAmaomao;

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
            if (data.scene.get(playerUUID).equals("gameA")){
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
        inv.setItem(22, get__item_gameAcookie(currentCookieAmount));
        inv.setItem(36, get__item_gameAmaomao(data.gameA_maomaoAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_AMOUNT),
                                                    data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST)));
    }
}
