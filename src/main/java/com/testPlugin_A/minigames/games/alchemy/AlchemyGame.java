package com.testPlugin_A.minigames.games.alchemy;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.core.ArcadeManager;
import com.testPlugin_A.minigames.core.GuiToolkit;
import com.testPlugin_A.minigames.data.AlchemyData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

/** 炼金工坊：采集材料、按配方酿造、等待完成后出售药剂。 */
public class AlchemyGame implements GuiGame {
    public static final String TITLE = ArcadeManager.TITLE_PREFIX + "§5炼金工坊";
    private final ArcadeManager arcade;
    private final GuiToolkit gui;
    private final Random random = new Random();

    public AlchemyGame(ArcadeManager arcade) { this.arcade = arcade; gui = arcade.gui(); }
    public String id() { return "alchemy"; }
    public String title() { return TITLE; }
    public String displayName() { return "炼金工坊"; }
    public String description() { return "采集材料，按配方炼制并出售药剂"; }
    public Material icon() { return Material.BREWING_STAND; }

    public void open(Player player) {
        AlchemyData data = arcade.profile(player).alchemy;
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);
        gui.fill(inventory);
        inventory.setItem(4, gui.item(Material.GOLD_NUGGET, "§6金币: §e" + data.coins,
                List.of("§7炼金锅等级: §d" + data.cauldronLevel)));
        inventory.setItem(13, gui.item(Material.CHEST, "§d材料与成品", List.of(
                "§7草药: §a" + data.herbs, "§7魔力水晶: §b" + data.crystals,
                "§7治疗药剂: §c" + data.healingPotions, "§7幸运药剂: §6" + data.luckyPotions)));
        String brewStatus = data.brewingFinishAt == 0 ? "§7炼金锅空闲"
                : System.currentTimeMillis() >= data.brewingFinishAt ? "§a酿造完成，点击领取"
                : "§7酿造剩余 §e" + secondsLeft(data.brewingFinishAt) + " 秒";
        inventory.setItem(22, gui.button(Material.CAULDRON, "§5§l炼金锅", List.of(brewStatus), id(), "collect"));
        inventory.setItem(36, gui.button(Material.FERN, "§a采集材料",
                List.of(data.nextGatherAt <= System.currentTimeMillis() ? "§e点击采集" : "§7冷却 " + secondsLeft(data.nextGatherAt) + " 秒"), id(), "gather"));
        inventory.setItem(38, gui.button(Material.POTION, "§c治疗药剂",
                List.of("§7配方: 草药 x3", "§7售价: 30 金币"), id(), "brew:healing"));
        inventory.setItem(40, gui.button(Material.SPLASH_POTION, "§6幸运药剂",
                List.of("§7配方: 草药 x2 + 水晶 x1", "§7售价: 75 金币"), id(), "brew:lucky"));
        inventory.setItem(42, gui.button(Material.EMERALD, "§a出售全部药剂", List.of(), id(), "sell"));
        inventory.setItem(44, gui.button(Material.BLAZE_POWDER, "§e升级炼金锅",
                List.of("§7花费: §6" + upgradeCost(data), "§7缩短酿造时间"), id(), "upgrade"));
        inventory.setItem(49, gui.button(Material.ARROW, "§f返回大厅", List.of(), id(), "back"));
        player.openInventory(inventory);
    }

    public void handleAction(Player player, String action) {
        if (action.equals("back")) { arcade.openHub(player); return; }
        AlchemyData data = arcade.profile(player).alchemy;
        long now = System.currentTimeMillis();
        if (action.equals("gather")) {
            if (now < data.nextGatherAt) { player.sendMessage("§5[炼金] §c采集点尚未恢复。"); return; }
            data.herbs += 2 + random.nextInt(3);
            if (random.nextInt(100) < 35) data.crystals++;
            data.nextGatherAt = now + 15_000L;
        } else if (action.equals("collect")) {
            if (data.brewingFinishAt == 0) player.sendMessage("§5[炼金] §7炼金锅现在是空的。");
            else if (now < data.brewingFinishAt) player.sendMessage("§5[炼金] §c药剂还没有完成。");
            else {
                if (data.brewingRecipe.equals("healing")) data.healingPotions++;
                if (data.brewingRecipe.equals("lucky")) data.luckyPotions++;
                data.brewingFinishAt = 0; data.brewingRecipe = "";
                player.sendMessage("§5[炼金] §a药剂已收入仓库。");
            }
        } else if (action.startsWith("brew:")) {
            if (data.brewingFinishAt > 0) { player.sendMessage("§5[炼金] §c炼金锅正在使用中。"); return; }
            String recipe = action.substring(5);
            if (recipe.equals("healing")) {
                if (data.herbs < 3) { player.sendMessage("§5[炼金] §c草药不足。"); return; }
                data.herbs -= 3;
            } else if (recipe.equals("lucky")) {
                if (data.herbs < 2 || data.crystals < 1) { player.sendMessage("§5[炼金] §c材料不足。"); return; }
                data.herbs -= 2; data.crystals--;
            } else return;
            data.brewingRecipe = recipe;
            data.brewingFinishAt = now + Math.max(5_000L, 25_000L - data.cauldronLevel * 2_000L);
        } else if (action.equals("sell")) {
            long income = data.healingPotions * 30L + data.luckyPotions * 75L;
            data.coins += income; data.healingPotions = data.luckyPotions = 0;
            player.sendMessage("§5[炼金] §a出售药剂获得 " + income + " 金币。");
        } else if (action.equals("upgrade")) {
            long cost = upgradeCost(data);
            if (data.coins < cost) { player.sendMessage("§5[炼金] §c金币不足。"); return; }
            data.coins -= cost; data.cauldronLevel++;
        }
        open(player);
    }

    private static long upgradeCost(AlchemyData data) { return 150L * data.cauldronLevel * data.cauldronLevel; }
    private static long secondsLeft(long finishAt) { return Math.max(0, (finishAt - System.currentTimeMillis() + 999) / 1000); }
}
