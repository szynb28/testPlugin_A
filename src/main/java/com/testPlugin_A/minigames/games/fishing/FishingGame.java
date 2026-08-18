package com.testPlugin_A.minigames.games.fishing;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.core.ArcadeManager;
import com.testPlugin_A.minigames.core.GuiToolkit;
import com.testPlugin_A.minigames.data.FishingData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

/** 钓鱼大师：抛竿后等待真实时间，离线回来也能收竿。 */
public class FishingGame implements GuiGame {
    public static final String TITLE = ArcadeManager.TITLE_PREFIX + "§b钓鱼大师";
    private final ArcadeManager arcade;
    private final GuiToolkit gui;
    private final Random random = new Random();

    public FishingGame(ArcadeManager arcade) { this.arcade = arcade; gui = arcade.gui(); }
    public String id() { return "fishing"; }
    public String title() { return TITLE; }
    public String displayName() { return "钓鱼大师"; }
    public String description() { return "抛竿等待、收集鱼获并升级鱼竿"; }
    public Material icon() { return Material.FISHING_ROD; }

    public void open(Player player) {
        FishingData data = arcade.profile(player).fishing;
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);
        gui.fill(inventory);
        inventory.setItem(4, gui.item(Material.GOLD_NUGGET, "§6金币: §e" + data.coins,
                List.of("§7鱼竿等级: §a" + data.rodLevel)));
        inventory.setItem(13, gui.item(Material.BARREL, "§b鱼篓", List.of(
                "§7普通鱼: §f" + data.commonFish, "§7稀有鱼: §d" + data.rareFish, "§7宝箱: §6" + data.treasure)));
        String status;
        if (data.castFinishAt == 0) status = "§7点击抛竿";
        else if (System.currentTimeMillis() >= data.castFinishAt) status = "§a鱼儿上钩了！点击收竿";
        else status = "§7距离上钩还有 §e" + secondsLeft(data.castFinishAt) + " 秒";
        inventory.setItem(22, gui.button(Material.FISHING_ROD, "§b§l湖心钓点", List.of(status), id(), "fish"));
        inventory.setItem(38, gui.button(Material.EMERALD, "§a出售全部鱼获",
                List.of("§7普通鱼 5 / 稀有鱼 25 / 宝箱 80"), id(), "sell"));
        inventory.setItem(40, gui.button(Material.HEART_OF_THE_SEA, "§e升级鱼竿",
                List.of("§7花费: §6" + upgradeCost(data), "§7缩短等待并提高稀有概率"), id(), "upgrade"));
        inventory.setItem(49, gui.button(Material.ARROW, "§f返回大厅", List.of(), id(), "back"));
        player.openInventory(inventory);
    }

    public void handleAction(Player player, String action) {
        if (action.equals("back")) { arcade.openHub(player); return; }
        FishingData data = arcade.profile(player).fishing;
        long now = System.currentTimeMillis();
        if (action.equals("fish")) {
            if (data.castFinishAt == 0) {
                long wait = Math.max(4_000L, 12_000L - data.rodLevel * 800L);
                data.castFinishAt = now + wait;
                player.sendMessage("§b[钓鱼] §a已经抛竿，稍后回来收取鱼获。");
            } else if (now < data.castFinishAt) {
                player.sendMessage("§b[钓鱼] §c还没有鱼儿上钩。");
            } else {
                int roll = random.nextInt(100);
                int bonus = Math.min(25, data.rodLevel * 2);
                if (roll < 5 + bonus / 3) { data.treasure++; player.sendMessage("§b[钓鱼] §6钓到了沉底宝箱！"); }
                else if (roll < 22 + bonus) { data.rareFish++; player.sendMessage("§b[钓鱼] §d钓到了稀有鱼！"); }
                else { data.commonFish++; player.sendMessage("§b[钓鱼] §a钓到了一条鱼。"); }
                data.castFinishAt = 0;
            }
        } else if (action.equals("sell")) {
            long income = data.commonFish * 5L + data.rareFish * 25L + data.treasure * 80L;
            data.coins += income;
            data.commonFish = data.rareFish = data.treasure = 0;
            player.sendMessage("§b[钓鱼] §a获得 " + income + " 金币。");
        } else if (action.equals("upgrade")) {
            long cost = upgradeCost(data);
            if (data.coins < cost) { player.sendMessage("§b[钓鱼] §c金币不足。"); return; }
            data.coins -= cost; data.rodLevel++;
        }
        open(player);
    }

    private static long upgradeCost(FishingData data) { return 120L * data.rodLevel * data.rodLevel; }
    private static long secondsLeft(long finishAt) { return Math.max(0, (finishAt - System.currentTimeMillis() + 999) / 1000); }
}
