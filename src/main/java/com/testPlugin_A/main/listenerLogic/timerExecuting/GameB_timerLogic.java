package com.testPlugin_A.main.listenerLogic.timerExecuting;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.GAME_A_DEFAULT_COOKIE_PER_SECOND;
import static com.testPlugin_A.main.packs.ItemsPack.*;

public class GameB_timerLogic {
    DataInitiator data; // 所附属的数据核心

    public GameB_timerLogic(DataInitiator data){
        this.data = data;
    }

    // 饼干点击游戏- 每秒更新时主逻辑
    public void logic(){
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID playerUUID = player.getUniqueId();
            // ★★等待挖掘树穴的逻辑★★
            double currentWaitingTime = data.gameB_shovelTimer.getOrDefault(playerUUID, 0.00);
            // 只有当玩家有等待挖掘树穴时，再执行增益逻辑。优化性能
            if (currentWaitingTime > 0){
                double calculatedTime = currentWaitingTime - 1; // 每秒减去1s剩余等待时间
                data.gameB_shovelTimer.put(playerUUID, toTwoDecimalPlaces(calculatedTime));
            }
            // 只有在当前玩家页面场景在gameB(生命之树)的时候更新GUI，刷新数字显示等内容。优化性能
            if (data.scene.get(playerUUID).equals("gameB")){
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
        // 图案参数相关计算与判断
        //-// 当前的种植阶段
        String plantStage = data.gameB_plantStage.getOrDefault(playerUUID, "未开荒");
        //-// 是否正在等待挖掘树穴
        boolean isWaitingShovel = false;
        if (data.gameB_shovelTimer.getOrDefault(playerUUID, 0.0) > 0) isWaitingShovel = true;
        //-// 当前挖掘树穴的剩余时间
        double waitingTime;
        waitingTime = data.gameB_shovelTimer.getOrDefault(playerUUID, 0.0);
        // 生命之树图案
        if (plantStage.equals("未开荒")){
            inv.setItem(22, get__item_gameBgrassBlock());
        }
        // 挖掘树穴图案
        if (isWaitingShovel) inv.setItem(37, get__item_gameBwaitingShovel(waitingTime));
        else inv.setItem(37, get__item_gameBshovel());
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
