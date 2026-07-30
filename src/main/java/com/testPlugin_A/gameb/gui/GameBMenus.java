package com.testPlugin_A.gameb.gui;

import com.testPlugin_A.gameb.FarmType;
import com.testPlugin_A.gameb.FertilizerTier;
import com.testPlugin_A.gameb.GameBProfile;
import com.testPlugin_A.gameb.GameBService;
import com.testPlugin_A.gameb.PlotStage;
import com.testPlugin_A.gameb.PlotState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameBMenus {
    public static final String MAIN_TITLE = "§2§l◀§a生命之树§2▶ §r§d喵~";
    public static final String FARM_TITLE = "§2生命之树 - 农场";
    public static final String SHOP_TITLE = "§2生命之树 - 肥料商店";
    public static final String WAREHOUSE_TITLE = "§2生命之树 - 仓库";
    public static final String RANK_TITLE = "§2生命之树 - 排行榜";
    public static final String VISITORS_TITLE = "§2生命之树 - 访客农场";
    public static final String VISIT_TITLE = "§2生命之树 - 访问农场";
    public static final String FERTILIZER_TITLE = "§2生命之树 - 选择肥料";

    private final JavaPlugin plugin;
    private final GameBService service;
    private final NamespacedKey actionKey;
    private final NamespacedKey targetKey;
    private final NamespacedKey farmKey;

    public GameBMenus(JavaPlugin plugin, GameBService service) {
        this.plugin = plugin;
        this.service = service;
        // 所有按钮用 PDC 保存动作和目标，而不是依赖物品材质或显示名称判断点击。
        this.actionKey = new NamespacedKey(plugin, "gameb_action");
        this.targetKey = new NamespacedKey(plugin, "gameb_target");
        this.farmKey = new NamespacedKey(plugin, "gameb_farm");
    }

    public boolean isGameBMenu(String title) {
        return title.equals(MAIN_TITLE) || title.equals(FARM_TITLE) || title.equals(SHOP_TITLE)
                || title.equals(WAREHOUSE_TITLE) || title.equals(RANK_TITLE) || title.equals(VISITORS_TITLE)
                || title.equals(VISIT_TITLE) || title.equals(FERTILIZER_TITLE);
    }

    public String action(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);
    }

    public UUID target(ItemStack item) {
        String raw = item.getItemMeta().getPersistentDataContainer().get(targetKey, PersistentDataType.STRING);
        try { return raw == null ? null : UUID.fromString(raw); } catch (IllegalArgumentException ignored) { return null; }
    }

    public FarmType farm(ItemStack item) {
        String raw = item.getItemMeta().getPersistentDataContainer().get(farmKey, PersistentDataType.STRING);
        return raw == null ? FarmType.LIFE_TREE : FarmType.fromId(raw);
    }

    public void openMain(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, MAIN_TITLE);
        renderMain(inventory, service.profile(player.getUniqueId(), player.getName()));
        player.openInventory(inventory);
    }

    public void refreshMain(Player player) {
        if (player.getOpenInventory().getTitle().equals(MAIN_TITLE)) {
            // 只刷新生命之树主菜单，绝不能向工作台、箱子等原版容器写入槽位。
            renderMain(player.getOpenInventory().getTopInventory(), service.profile(player.getUniqueId(), player.getName()));
        }
    }

    private void renderMain(Inventory inventory, GameBProfile profile) {
        fill(inventory);
        FarmType farm = FarmType.fromId(profile.getActiveFarmId());
        PlotState plot = profile.plot(farm);
        inventory.setItem(4, item(Material.SUNFLOWER, "§6金币: §e" + profile.getCoins() + "  §7累计收入: §a" + profile.getTotalEarned(), List.of("§7当前农场: §a" + farm.displayName()), null));
        inventory.setItem(22, item(stageMaterial(plot.getStage()), "§a" + farm.displayName(), List.of("§7作物: §f" + farm.cropName(), "§7阶段: §e" + stageName(plot.getStage()), progressLine(plot)), null));
        inventory.setItem(36, actionItem(Material.IRON_SHOVEL, "§6挖掘树穴", List.of("§7开荒后开始种植"), "dig"));
        inventory.setItem(37, actionItem(Material.WHEAT_SEEDS, "§a栽种", List.of("§7把种子种入树穴"), "plant"));
        inventory.setItem(38, actionItem(Material.WATER_BUCKET, "§b浇灌", List.of("§7为幼苗补充水分"), "water"));
        inventory.setItem(39, actionItem(Material.WOODEN_HOE, "§e栽培", List.of("§7耐心照料作物"), "cultivate"));
        inventory.setItem(40, actionItem(Material.BONE_MEAL, "§d施肥 / 开始生长", List.of("§7选择肥料，或自然生长"), "fertilizer"));
        inventory.setItem(41, actionItem(Material.CHEST, "§6收获", List.of("§7成熟后收入仓库"), "harvest"));
        inventory.setItem(45, actionItem(Material.COMPASS, "§a农场", List.of("§7选择或解锁农场"), "farms"));
        inventory.setItem(46, actionItem(Material.BARREL, "§6仓库", List.of("§7查看作物并全部出售"), "warehouse"));
        inventory.setItem(47, actionItem(Material.EMERALD, "§a肥料商店", List.of("§7购买成长加速肥料"), "shop"));
        inventory.setItem(48, actionItem(Material.GOLD_INGOT, "§e全服排行榜", List.of("§7按累计售卖收入排名"), "rank"));
        inventory.setItem(49, actionItem(Material.SPYGLASS, "§d访客农场", List.of("§7寻找成熟作物"), "visitors"));
        inventory.setItem(53, actionItem(Material.BARRIER, "§c关闭", List.of(), "close"));
    }

    public void openFarmMenu(Player player) {
        GameBProfile profile = service.profile(player.getUniqueId(), player.getName());
        Inventory inventory = base(FARM_TITLE);
        int slot = 20;
        for (FarmType farm : FarmType.values()) {
            boolean owned = profile.hasFarm(farm);
            long cost = service.config().unlockCost(farm);
            inventory.setItem(slot++, farmItem(farm, owned, cost, "farm"));
        }
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openWarehouse(Player player) {
        GameBProfile profile = service.profile(player.getUniqueId(), player.getName());
        Inventory inventory = base(WAREHOUSE_TITLE);
        int slot = 20;
        for (FarmType farm : FarmType.values()) {
            int amount = profile.itemCount(farm.id());
            inventory.setItem(slot++, item(Material.CHEST, "§a" + farm.cropName() + " §fx" + amount,
                    List.of("§7单价: §e" + service.config().sellPrice(farm), "§7出售价值: §6" + (amount * service.config().sellPrice(farm))), null));
        }
        inventory.setItem(47, actionItem(Material.GOLD_INGOT, "§6全部出售", List.of("§7卖出仓库全部作物"), "sell"));
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openShop(Player player) {
        GameBProfile profile = service.profile(player.getUniqueId(), player.getName());
        Inventory inventory = base(SHOP_TITLE);
        int slot = 20;
        for (FertilizerTier tier : new FertilizerTier[]{FertilizerTier.BASIC, FertilizerTier.ADVANCED, FertilizerTier.MASTER}) {
            inventory.setItem(slot++, actionItem(Material.BONE_MEAL, "§a" + tier.displayName(), List.of("§7价格: §e" + service.config().fertilizerPrice(tier), "§7库存: §f" + profile.fertilizerCount(tier), "§7缩短生长至 §e" + Math.round(service.config().growthMultiplier(tier) * 100) + "%"), "buy:" + tier.id()));
        }
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openFertilizer(Player player) {
        GameBProfile profile = service.profile(player.getUniqueId(), player.getName());
        Inventory inventory = base(FERTILIZER_TITLE);
        inventory.setItem(19, actionItem(Material.WHEAT, "§f自然生长", List.of("§7不消耗肥料，立即开始生长"), "grow:none"));
        int slot = 21;
        for (FertilizerTier tier : new FertilizerTier[]{FertilizerTier.BASIC, FertilizerTier.ADVANCED, FertilizerTier.MASTER}) {
            inventory.setItem(slot++, actionItem(Material.BONE_MEAL, "§a" + tier.displayName(), List.of("§7库存: §f" + profile.fertilizerCount(tier), "§7施肥后开始生长"), "grow:" + tier.id()));
        }
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openLeaderboard(Player player) {
        Inventory inventory = base(RANK_TITLE);
        List<Map.Entry<String, GameBProfile>> ranking = service.leaderboard();
        for (int index = 0; index < Math.min(10, ranking.size()); index++) {
            GameBProfile profile = ranking.get(index).getValue();
            inventory.setItem(10 + index, item(Material.GOLD_INGOT, "§e#" + (index + 1) + " §f" + profile.getLastKnownName(), List.of("§7累计收入: §6" + profile.getTotalEarned()), null));
        }
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openVisitors(Player player) {
        Inventory inventory = base(VISITORS_TITLE);
        int slot = 10;
        for (Map.Entry<String, GameBProfile> entry : service.matureVisitors()) {
            if (slot >= 44) break;
            try {
                UUID target = UUID.fromString(entry.getKey());
                if (target.equals(player.getUniqueId())) continue;
                FarmType matureFarm = firstMatureFarm(entry.getValue());
                ItemStack visitor = actionItem(Material.PLAYER_HEAD, "§d访问 " + entry.getValue().getLastKnownName(), List.of("§7成熟作物: §a" + matureFarm.cropName(), "§7点击查看农场"), "visit");
                tag(visitor, target, matureFarm);
                inventory.setItem(slot++, visitor);
            } catch (IllegalArgumentException ignored) { }
        }
        if (slot == 10) inventory.setItem(22, item(Material.BARRIER, "§7暂无可访问的成熟农场", List.of(), null));
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "main"));
        player.openInventory(inventory);
    }

    public void openVisit(Player player, UUID targetId, FarmType farm) {
        GameBProfile target = service.find(targetId);
        if (target == null) { openVisitors(player); return; }
        Inventory inventory = base(VISIT_TITLE);
        PlotState plot = target.plot(farm);
        inventory.setItem(22, item(stageMaterial(plot.getStage()), "§a" + target.getLastKnownName() + " 的" + farm.displayName(), List.of("§7作物: §f" + farm.cropName(), "§7阶段: §e" + stageName(plot.getStage()), progressLine(plot)), null));
        ItemStack steal = actionItem(Material.SHEARS, "§d偷取成熟作物", List.of("§7成功获得正常产量的 20%", "§7目标每 12 小时只能被偷一次"), "steal");
        tag(steal, targetId, farm);
        inventory.setItem(40, steal);
        inventory.setItem(49, actionItem(Material.ARROW, "§f返回", List.of(), "visitors"));
        player.openInventory(inventory);
    }

    private Inventory base(String title) { Inventory inventory = Bukkit.createInventory(null, 54, title); fill(inventory); return inventory; }
    private void fill(Inventory inventory) { for (int index = 0; index < inventory.getSize(); index++) inventory.setItem(index, item(Material.BLACK_STAINED_GLASS_PANE, " ", List.of(), null)); }

    private ItemStack farmItem(FarmType farm, boolean owned, long cost, String action) {
        List<String> lore = owned ? List.of("§a已解锁", "§7点击切换到此农场") : List.of("§7解锁花费: §e" + cost + " 金币", "§7点击解锁并切换");
        ItemStack item = actionItem(owned ? Material.GRASS_BLOCK : Material.RED_STAINED_GLASS, "§a" + farm.displayName(), lore, action);
        item.getItemMeta().getPersistentDataContainer();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(farmKey, PersistentDataType.STRING, farm.id());
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack actionItem(Material material, String name, List<String> lore, String action) { return item(material, name, lore, action); }
    private ItemStack item(Material material, String name, List<String> lore, String action) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if (action != null) meta.getPersistentDataContainer().set(actionKey, PersistentDataType.STRING, action);
        stack.setItemMeta(meta);
        return stack;
    }
    private void tag(ItemStack item, UUID target, FarmType farm) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(targetKey, PersistentDataType.STRING, target.toString());
        meta.getPersistentDataContainer().set(farmKey, PersistentDataType.STRING, farm.id());
        item.setItemMeta(meta);
    }

    private static FarmType firstMatureFarm(GameBProfile profile) {
        for (FarmType farm : FarmType.values()) if (profile.hasFarm(farm) && profile.plot(farm).getStage() == PlotStage.MATURE) return farm;
        return FarmType.LIFE_TREE;
    }
    private static Material stageMaterial(PlotStage stage) { return stage == PlotStage.MATURE ? Material.GLOW_BERRIES : (stage == PlotStage.WILDERNESS ? Material.GRASS_BLOCK : Material.OAK_SAPLING); }
    private static String stageName(PlotStage stage) {
        return switch (stage) {
            case WILDERNESS -> "未开荒"; case DIGGING -> "挖掘树穴"; case READY_TO_PLANT -> "等待栽种"; case PLANTING -> "栽种中";
            case READY_TO_WATER -> "等待浇灌"; case WATERING -> "浇灌中"; case READY_TO_CULTIVATE -> "等待栽培"; case CULTIVATING -> "栽培中";
            case READY_TO_GROW -> "等待施肥"; case FERTILIZING -> "施肥中"; case GROWING -> "成长中"; case MATURE -> "已成熟";
        };
    }
    private static String progressLine(PlotState plot) {
        if (plot.getFinishAt() <= 0) return "§7可进行下一步操作";
        long remaining = Math.max(0, plot.getFinishAt() - System.currentTimeMillis());
        return "§7剩余: §e" + format(remaining);
    }
    private static String format(long milliseconds) {
        Duration duration = Duration.ofMillis(milliseconds);
        long hours = duration.toHours(); long minutes = duration.toMinutesPart(); long seconds = duration.toSecondsPart();
        return hours > 0 ? hours + "时" + minutes + "分" : minutes > 0 ? minutes + "分" + seconds + "秒" : seconds + "秒";
    }
}
