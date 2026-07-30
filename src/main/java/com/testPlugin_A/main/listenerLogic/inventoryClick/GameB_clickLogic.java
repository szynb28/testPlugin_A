package com.testPlugin_A.main.listenerLogic.inventoryClick;

import com.testPlugin_A.gameb.FarmAction;
import com.testPlugin_A.gameb.FarmType;
import com.testPlugin_A.gameb.FertilizerTier;
import com.testPlugin_A.gameb.GameBProfile;
import com.testPlugin_A.gameb.GameBService;
import com.testPlugin_A.gameb.gui.GameBMenus;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GameB_clickLogic {
    private final InventoryClickEvent clickEvent;
    private final GameBService service;
    private final GameBMenus menus;

    public GameB_clickLogic(InventoryClickEvent clickEvent, GameBService service, GameBMenus menus) {
        this.clickEvent = clickEvent;
        this.service = service;
        this.menus = menus;
    }

    public void logic() {
        clickEvent.setCancelled(true);
        ItemStack clicked = clickEvent.getCurrentItem();
        if (clicked == null) return;
        String action = menus.action(clicked);
        if (action == null) return;
        Player player = (Player) clickEvent.getWhoClicked();
        GameBProfile profile = service.profile(player.getUniqueId(), player.getName());
        FarmType activeFarm = FarmType.fromId(profile.getActiveFarmId());
        long now = System.currentTimeMillis();

        switch (action) {
            case "dig" -> apply(player, service.startAction(profile, activeFarm, FarmAction.DIG, now), "开始挖掘树穴。", true);
            case "plant" -> apply(player, service.startAction(profile, activeFarm, FarmAction.PLANT, now), "开始栽种。", true);
            case "water" -> apply(player, service.startAction(profile, activeFarm, FarmAction.WATER, now), "开始浇灌。", true);
            case "cultivate" -> apply(player, service.startAction(profile, activeFarm, FarmAction.CULTIVATE, now), "开始栽培。", true);
            case "fertilizer" -> menus.openFertilizer(player);
            case "harvest" -> apply(player, service.harvest(profile, activeFarm, now), "收获已放入仓库。", true);
            case "farms" -> menus.openFarmMenu(player);
            case "warehouse" -> menus.openWarehouse(player);
            case "shop" -> menus.openShop(player);
            case "rank" -> menus.openLeaderboard(player);
            case "visitors" -> menus.openVisitors(player);
            case "main" -> menus.openMain(player);
            case "close" -> player.closeInventory();
            case "farm" -> apply(player, service.unlockOrSelect(profile, menus.farm(clicked)), "农场已切换。", true);
            case "sell" -> {
                long income = service.sellAll(profile);
                player.sendMessage(income > 0 ? "§6出售完成，获得 " + income + " 金币。" : "§7仓库中没有可出售的作物。");
                menus.openWarehouse(player);
            }
            case "visit" -> {
                UUID target = menus.target(clicked);
                if (target != null) menus.openVisit(player, target, menus.farm(clicked));
            }
            case "steal" -> {
                UUID target = menus.target(clicked);
                if (target != null) apply(player, service.steal(profile, player.getUniqueId(), target, menus.farm(clicked), now), "偷取成功，作物已放入仓库。", false);
            }
            default -> {
                if (action.startsWith("buy:")) {
                    apply(player, service.buyFertilizer(profile, FertilizerTier.fromId(action.substring(4))), "购买成功。", false);
                    menus.openShop(player);
                } else if (action.startsWith("grow:")) {
                    apply(player, service.startGrowing(profile, activeFarm, FertilizerTier.fromId(action.substring(5)), now), "作物开始生长。", true);
                }
            }
        }
    }

    private void apply(Player player, GameBService.Result result, String success, boolean openMain) {
        if (result == GameBService.Result.OK) {
            player.sendMessage("§a" + success);
            if (openMain) menus.openMain(player);
            return;
        }
        String message = switch (result) {
            case INVALID_STAGE -> "当前阶段不能进行这个操作。";
            case NOT_UNLOCKED -> "该农场尚未解锁。";
            case NOT_ENOUGH_COINS -> "金币不足。";
            case NOT_ENOUGH_FERTILIZER -> "没有足够的这种肥料。";
            case NOT_MATURE -> "作物尚未成熟。";
            case COOLDOWN -> "这个玩家的农场刚被偷过，请稍后再来。";
            case SELF_TARGET -> "不能偷取自己的农场。";
            case OK -> success;
        };
        player.sendMessage("§c" + message);
    }
}
