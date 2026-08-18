package com.testPlugin_A.minigames.games.miner;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.core.ArcadeManager;
import com.testPlugin_A.minigames.core.GuiToolkit;
import com.testPlugin_A.minigames.data.MinerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

/** 矿工挖矿：消耗自动恢复的体力，挖取矿石、出售并升级镐子。 */
public class MinerGame implements GuiGame {
    public static final String TITLE = ArcadeManager.TITLE_PREFIX + "§6矿工挖矿";
    private final ArcadeManager arcade;
    private final GuiToolkit gui;
    private final Random random = new Random();

    public MinerGame(ArcadeManager arcade) { this.arcade = arcade; gui = arcade.gui(); }
    public String id() { return "miner"; }
    public String title() { return TITLE; }
    public String displayName() { return "矿工挖矿"; }
    public String description() { return "点击矿脉获得矿石，出售后升级镐子"; }
    public Material icon() { return Material.DIAMOND_PICKAXE; }

    public void open(Player player) {
        MinerData data = arcade.profile(player).miner;
        restoreEnergy(data);
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);
        gui.fill(inventory);
        inventory.setItem(4, gui.item(Material.GOLD_NUGGET, "§6金币: §e" + data.coins,
                List.of("§7镐子等级: §a" + data.pickaxeLevel, "§7累计挖掘: §f" + data.totalMined)));
        inventory.setItem(13, gui.item(Material.CHEST, "§e矿石背包", List.of(
                "§7石头: §f" + data.stone, "§7煤矿: §8" + data.coal, "§7铁矿: §f" + data.iron, "§7钻石: §b" + data.diamond)));
        inventory.setItem(22, gui.button(Material.DEEPSLATE_DIAMOND_ORE, "§b§l挖掘矿脉",
                List.of("§7每次消耗 1 体力", "§7体力每 10 秒恢复 1 点", "§6当前体力: " + data.energy + "/" + maxEnergy(data)), id(), "mine"));
        inventory.setItem(38, gui.button(Material.EMERALD, "§a出售全部矿石",
                List.of("§7石头 1 / 煤 4 / 铁 10 / 钻石 50"), id(), "sell"));
        long upgrade = upgradeCost(data);
        inventory.setItem(40, gui.button(Material.ANVIL, "§e升级镐子",
                List.of("§7花费: §6" + upgrade, "§7提高稀有矿概率与体力上限"), id(), "upgrade"));
        inventory.setItem(49, gui.button(Material.ARROW, "§f返回大厅", List.of(), id(), "back"));
        player.openInventory(inventory);
    }

    public void handleAction(Player player, String action) {
        if (action.equals("back")) { arcade.openHub(player); return; }
        MinerData data = arcade.profile(player).miner;
        restoreEnergy(data);
        if (action.equals("mine")) {
            if (data.energy <= 0) { player.sendMessage("§6[矿工] §c体力不足，请稍后再来。"); return; }
            data.energy--; data.totalMined++;
            int roll = random.nextInt(1000);
            int levelBonus = Math.min(150, (data.pickaxeLevel - 1) * 15);
            if (roll < 8 + levelBonus / 10) { data.diamond++; reward(player, "§b钻石"); }
            else if (roll < 90 + levelBonus) { data.iron++; reward(player, "§f铁矿"); }
            else if (roll < 320 + levelBonus) { data.coal++; reward(player, "§8煤矿"); }
            else { data.stone++; reward(player, "§7石头"); }
        } else if (action.equals("sell")) {
            long income = data.stone + data.coal * 4L + data.iron * 10L + data.diamond * 50L;
            data.coins += income;
            data.stone = data.coal = data.iron = data.diamond = 0;
            player.sendMessage("§6[矿工] §a卖出矿石获得 " + income + " 金币。");
        } else if (action.equals("upgrade")) {
            long cost = upgradeCost(data);
            if (data.coins < cost) { player.sendMessage("§6[矿工] §c金币不足。"); return; }
            data.coins -= cost; data.pickaxeLevel++; data.energy = Math.min(maxEnergy(data), data.energy + 5);
        }
        open(player);
    }

    private void restoreEnergy(MinerData data) {
        long now = System.currentTimeMillis();
        if (data.energyUpdatedAt <= 0) data.energyUpdatedAt = now;
        long restored = (now - data.energyUpdatedAt) / 10_000L;
        if (restored <= 0) return;
        data.energy = (int) Math.min(maxEnergy(data), data.energy + restored);
        data.energyUpdatedAt += restored * 10_000L;
    }

    private static int maxEnergy(MinerData data) { return 20 + (data.pickaxeLevel - 1) * 5; }
    private static long upgradeCost(MinerData data) { return 100L * data.pickaxeLevel * data.pickaxeLevel; }
    private static void reward(Player player, String ore) { player.sendMessage("§6[矿工] §a挖到了 " + ore + "§a！"); }
}
