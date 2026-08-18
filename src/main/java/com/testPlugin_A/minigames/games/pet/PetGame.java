package com.testPlugin_A.minigames.games.pet;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.core.ArcadeManager;
import com.testPlugin_A.minigames.core.GuiToolkit;
import com.testPlugin_A.minigames.data.ArcadeProfile;
import com.testPlugin_A.minigames.data.PetData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/** 宠物养成：照料、训练、购买食物，并支持离线远征奖励。 */
public class PetGame implements GuiGame {
    public static final String TITLE = ArcadeManager.TITLE_PREFIX + "§d宠物养成";
    private final ArcadeManager arcade;
    private final GuiToolkit gui;

    public PetGame(ArcadeManager arcade) {
        this.arcade = arcade;
        this.gui = arcade.gui();
    }

    public String id() { return "pet"; }
    public String title() { return TITLE; }
    public String displayName() { return "宠物养成"; }
    public String description() { return "喂食、玩耍、训练并派宠物远征"; }
    public Material icon() { return Material.WOLF_SPAWN_EGG; }

    public void open(Player player) {
        ArcadeProfile profile = arcade.profile(player);
        reconcile(profile.pet, player);
        PetData pet = profile.pet;
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);
        gui.fill(inventory);
        int required = pet.level * 100;
        inventory.setItem(22, gui.item(Material.WOLF_SPAWN_EGG, "§d§l" + pet.name,
                List.of("§7等级: §e" + pet.level, "§7经验: §a" + pet.experience + "/" + required,
                        "§7饱食: §6" + pet.hunger + "/100", "§7心情: §b" + pet.mood + "/100",
                        "§7金币: §e" + pet.coins, "§7食物: §f" + pet.food)));
        inventory.setItem(37, gui.button(Material.COOKED_BEEF, "§6喂食", List.of("§7消耗 1 食物", "§a饱食 +20，经验 +5"), id(), "feed"));
        inventory.setItem(38, gui.button(Material.SLIME_BALL, "§a陪伴玩耍", List.of("§b心情 +15，经验 +8"), id(), "play"));
        inventory.setItem(39, gui.button(Material.IRON_SWORD, "§c训练", List.of("§a经验 +15", "§7会消耗饱食和心情"), id(), "train"));
        inventory.setItem(40, gui.button(Material.EMERALD, "§a购买食物", List.of("§7花费 10 金币"), id(), "buy_food"));
        String expedition = pet.expeditionFinishAt > 0
                ? "§7剩余: §e" + secondsLeft(pet.expeditionFinishAt) + " 秒"
                : "§7派遣 5 分钟，离线也会完成";
        inventory.setItem(41, gui.button(Material.COMPASS, "§b宠物远征", List.of(expedition), id(), "expedition"));
        inventory.setItem(49, gui.button(Material.ARROW, "§f返回大厅", List.of(), id(), "back"));
        player.openInventory(inventory);
    }

    public void handleAction(Player player, String action) {
        if (action.equals("back")) { arcade.openHub(player); return; }
        PetData pet = arcade.profile(player).pet;
        long now = System.currentTimeMillis();
        reconcile(pet, player);
        if (action.equals("buy_food")) {
            if (pet.coins < 10) message(player, "§c金币不足。");
            else { pet.coins -= 10; pet.food++; message(player, "§a购买了 1 份宠物食物。"); }
            open(player); return;
        }
        if (action.equals("expedition")) {
            if (pet.expeditionFinishAt > 0) message(player, "§c宠物正在远征中。");
            else { pet.expeditionFinishAt = now + 300_000L; message(player, "§a宠物出发远征，5 分钟后回来！"); }
            open(player); return;
        }
        if (now < pet.nextCareAt) {
            message(player, "§c宠物需要休息 " + secondsLeft(pet.nextCareAt) + " 秒。");
            return;
        }
        switch (action) {
            case "feed" -> {
                if (pet.food <= 0) { message(player, "§c没有食物，请先购买。"); return; }
                pet.food--; pet.hunger = Math.min(100, pet.hunger + 20); pet.mood = Math.min(100, pet.mood + 5); addExperience(pet, 5);
            }
            case "play" -> { pet.mood = Math.min(100, pet.mood + 15); pet.hunger = Math.max(0, pet.hunger - 5); addExperience(pet, 8); }
            case "train" -> { pet.hunger = Math.max(0, pet.hunger - 10); pet.mood = Math.max(0, pet.mood - 5); addExperience(pet, 15); }
            default -> { return; }
        }
        pet.nextCareAt = now + 10_000L;
        open(player);
    }

    private void reconcile(PetData pet, Player player) {
        if (pet.expeditionFinishAt > 0 && System.currentTimeMillis() >= pet.expeditionFinishAt) {
            pet.expeditionFinishAt = 0;
            long reward = pet.level * 30L;
            pet.coins += reward;
            addExperience(pet, 25);
            message(player, "§6宠物远征归来，带回 " + reward + " 金币！");
        }
    }

    private void addExperience(PetData pet, int amount) {
        pet.experience += amount;
        while (pet.experience >= pet.level * 100) {
            pet.experience -= pet.level * 100;
            pet.level++;
            pet.coins += 50;
        }
    }

    private static long secondsLeft(long finishAt) { return Math.max(0, (finishAt - System.currentTimeMillis() + 999) / 1000); }
    private static void message(Player player, String message) { player.sendMessage("§d[宠物] " + message); }
}
