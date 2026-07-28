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

import static com.testPlugin_A.main.packs.ItemsPack.*;

public class GameB_clickLogic {
    InventoryClickEvent clickEvent; // 所附属的点击事件
    DataInitiator data; // 所附属的数据核心

    public GameB_clickLogic(InventoryClickEvent clickEvent, DataInitiator data){
        this.clickEvent = clickEvent;
        this.data = data;
    }

    // 饼干点击游戏- 点击主逻辑
    public void logic() {
        // 取消点击(防止拿出物品)
        clickEvent.setCancelled(true);

        Player player = (Player) clickEvent.getWhoClicked();
        ItemStack clickedItem = clickEvent.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // 判断点击挖掘树穴按钮
        /*
        点击挖掘树穴按钮后，该槽位会变成等待按钮，此时无法触发这个
        当倒计时结束后，updateGUI逻辑方法中会自动将该槽位物品换成铁锹，所以不用再去判断是否倒计时结束再换成铁锹
         */
        if (clickedItem.getType() == Material.IRON_SHOVEL){
            UUID playerUUID = player.getUniqueId();
            // 更新挖掘树穴倒计时逻辑
            updateShovelTimer(player);
            // 更新GUI，刷新数字显示等内容
            updateGUI(player);
        }
    }
    // 更新挖掘树穴倒计时逻辑
    public void updateShovelTimer(Player player){
        UUID playerUUID = player.getUniqueId();
        data.gameB_shovelTimer.put(playerUUID, 10.00); // todo 挖掘时间的逻辑要迭代下
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
