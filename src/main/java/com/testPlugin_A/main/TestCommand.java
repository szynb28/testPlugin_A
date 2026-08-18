package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.gameb.GameBService;
import com.testPlugin_A.gameb.gui.GameBMenus;
import com.testPlugin_A.minigames.core.ArcadeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.testPlugin_A.main.packs.ItemsPack.*;
import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InventoriesPack.*;
import static com.testPlugin_A.main.packs.EntitiesPack.*;

public class TestCommand implements CommandExecutor {

    private final DataInitiator data;
    private final GameBMenus gameBMenus;
    private final ArcadeManager arcadeManager;

    public TestCommand(DataInitiator data, GameBService gameBService, GameBMenus gameBMenus, ArcadeManager arcadeManager){
        this.data = data;
        this.gameBMenus = gameBMenus;
        this.arcadeManager = arcadeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        // ./tp_A help
        if (args.length == 1 && args[0].equals("help")){
            // 聊天栏显示提示信息
            sender.sendMessage("§6[TestPlugin_§4A§6] §7你打开了一个菜单qwq");
            // 打开一个帮助菜单容器
            Inventory inv_main = get__inventory_helpMenu();
            Player player = (Player) sender;
            player.openInventory(inv_main);

            return true;
        }

        // ./tp_A spawnreward
        if (args.length == 1 && args[0].equals("spawnreward")){
            if (!(sender instanceof Player player)){
                sender.sendMessage("只有玩家能用这个指令喵！");
                return true;
            }

            // 获取玩家位置和朝向
            Location playerLoc = player.getLocation();

            // 在玩家前方 2 格、上方 0.5 格生成（相对位置）
            Vector offset = playerLoc.getDirection().normalize().multiply(2); // 前方2格
            Location spawnLoc = playerLoc.clone().add(offset);
            spawnLoc.setY(playerLoc.getY());

            // 生成盔甲架
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(spawnLoc, EntityType.ARMOR_STAND);

            // 设置盔甲架属性
            // stand.setVisible(false);  // 隐形
            stand.setGravity(false);     // 无重力
            stand.setInvulnerable(true); // 无敌
            stand.setMarker(true);       // 无碰撞箱，只占一个很小的点
            stand.setCustomNameVisible(true); // 名字可见
            stand.customName(net.kyori.adventure.text.Component.text("§e§l[右键领取钻石]"));

            // 设置 Interaction 实体（负责捕获右键，完全隐形）
            org.bukkit.entity.Interaction interaction = get__rewardInteraction(spawnLoc, player);

            // 设置方块展示实体
            BlockDisplay blockDisplay = get__blockDisplay(playerLoc,
                    Material.DIAMOND_BLOCK,
                    new Vector3f(0f, .5f, 0f),
                    new Vector3f(.5f, .5f, .5f));

            // 把盔甲架的 UUID 存到 Interaction 的 PDC 里（*group ID）
            NamespacedKey standKey = new NamespacedKey(Main.main, "linked_stand");
            interaction.getPersistentDataContainer().set(standKey, PersistentDataType.STRING, stand.getUniqueId().toString());

            // 把展示方块实体的 UUID 存到 Interaction 的 PDC 里（*group ID）
            NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplayKey");
            interaction.getPersistentDataContainer().set(blockDisplayKey, PersistentDataType.STRING, blockDisplay.getUniqueId().toString());

            player.sendMessage("§a奖励实体已生成！右键它领取钻石喵～");
            return true;
        }

        // ./tp_A getspecialitem1
        if (args.length == 1 && args[0].equals("getspecialitem")){
            Player player = (Player) sender;

            player.getInventory().addItem(get__item_magicWand(Main.main));
            player.sendMessage("§6✦ 你获得了一根魔杖！");
        }

        // ./tp_A getspecialitem2
        if (args.length == 1 && args[0].equals("getspecialitem2")){
            Player player = (Player) sender;

            player.getInventory().addItem(get__item_summoner(Main.main));
            player.sendMessage("§a✦ 你获得了一个召唤器！");
        }

        // ./tp_A sit
        if (args.length == 1 && args[0].equals("sitandmove")){
            if (!(sender instanceof Player player)) return true;

            // 如果已经坐下，则站起来
            if (player.isInsideVehicle()){
                player.leaveVehicle();
                return true;
            }

            Location loc = player.getLocation();

            // ✦✦✦ 加入第一个载具 ========================================================================================

            // 生成隐形可移动座位 1
            ArmorStand seat_1 = player.getWorld().spawn(loc, ArmorStand.class, stand -> {
                stand.setVisible(false);       // 隐形
                stand.setGravity(false);       // 不掉下去
                // stand.setMarker(true);      可移动载具用marker不稳定！这行先注释掉
                stand.setSmall(true);          // 小尺寸
                stand.setInvulnerable(true);   // 无敌
                stand.setCustomNameVisible(false);
                stand.setRemoveWhenFarAway(false); // 不会因为跑远了被刷掉

                // 打标签，方便识别是“座位”
                NamespacedKey key = new NamespacedKey(Main.main, "seat_entity_1");
                stand.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            });

            // 设置方块展示实体 1
            BlockDisplay blockDisplay_1 = get__blockDisplay(loc,
                    Material.DIAMOND_BLOCK,
                    new Vector3f(0f, 0f, 0f), // 生成的方块展示实体保持中央和玩家对齐
                    new Vector3f(1f, 1f, 1f));

            // 把方块的视觉中心拉回实体位置！
            blockDisplay_1.setTransformation(new org.bukkit.util.Transformation(
                    new org.joml.Vector3f(-.5f, 0f, -.5f), // ← 关键！把中心往回拉(y可能也偏了，不过偏的刚刚好啊，正好看起来就像玩家坐在上面一样)
                    new org.joml.Quaternionf(0, 0, 0, 1),
                    new org.joml.Vector3f(1f, 1f, 1f),
                    new org.joml.Quaternionf(0, 0, 0, 1)
            ));

            // 把展示方块实体的 UUID 存到 盔甲架 的 PDC 里（*group ID）
            NamespacedKey blockDisplay_1_Key = new NamespacedKey(Main.main, "linked_blockDisplay_1");
            seat_1.getPersistentDataContainer().set(blockDisplay_1_Key, PersistentDataType.STRING, blockDisplay_1.getUniqueId().toString());

            //=- 玩家骑上去盔甲架
            seat_1.addPassenger(player);
            player.sendMessage("§a已坐下～ 按Shift站起来喵");

            // ✦✦✦ 加入第二个载具 ========================================================================================

            // 生成隐形可移动座位 2

            //-// 设置新座位的偏移
            Location loc_2 = loc.clone();
            loc_2.set(loc.getX(), loc.getY(), loc.getZ() - 3);

            ArmorStand seat_2 = player.getWorld().spawn(loc_2, ArmorStand.class, stand -> {
                stand.setVisible(false);       // 隐形
                stand.setGravity(false);       // 不掉下去
                // stand.setMarker(true);      可移动载具用marker不稳定！这行先注释掉
                stand.setSmall(true);          // 小尺寸
                stand.setInvulnerable(true);   // 无敌
                stand.setCustomNameVisible(false);
                stand.setRemoveWhenFarAway(false); // 不会因为跑远了被刷掉

                // 打标签，方便识别是“座位”
                NamespacedKey key = new NamespacedKey(Main.main, "seat_entity_2");
                stand.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            });

            // 生成完seat_2后，在seat_1里存seat_2的关联标签(UUID)
            NamespacedKey linkedSeat2Key = new NamespacedKey(Main.main, "linked_seat_2");
            seat_1.getPersistentDataContainer().set(
                    linkedSeat2Key,
                    PersistentDataType.STRING,
                    seat_2.getUniqueId().toString()
            );

            // 设置方块展示实体 2
            BlockDisplay blockDisplay_2 = get__blockDisplay(loc_2,
                    Material.DIAMOND_BLOCK,
                    new Vector3f(0f, 0f, 0f),
                    new Vector3f(1f, 1f, 1f));

            // 把方块的视觉中心拉回实体位置！
            blockDisplay_2.setTransformation(new org.bukkit.util.Transformation(
                    new org.joml.Vector3f(-.5f, 0f, -.5f), // ← 关键！把中心往回拉(y可能也偏了，不过偏的刚刚好啊，正好看起来就像玩家坐在上面一样)
                    new org.joml.Quaternionf(0, 0, 0, 1),
                    new org.joml.Vector3f(1f, 1f, 1f),
                    new org.joml.Quaternionf(0, 0, 0, 1)
            ));

            // 把展示方块实体的 UUID 存到 盔甲架 的 PDC 里（*group ID）
            NamespacedKey blockDisplay_2_Key = new NamespacedKey(Main.main, "linked_blockDisplay_2");
            seat_2.getPersistentDataContainer().set(blockDisplay_2_Key, PersistentDataType.STRING, blockDisplay_2.getUniqueId().toString());

            return true;
        }

        // ./tp_A game A
        if (args.length == 2 && args[0].equals("game") && args[1].equals("A")){
            // 初始化(获取数据核心)

            // 聊天栏显示提示信息
            sender.sendMessage("§6[TestPlugin_§4A§6] §7你进入了 §6★饼★干★点★击★ §7小游戏喵awa");
            // 打开饼干点击小游戏页面
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            Inventory gameInv = get__inventory_gameA(data.gameA_cookieAmount.getOrDefault(playerUUID, 0.00),
                                                    data.gameA_cookiePerSecond.getOrDefault(playerUUID, 0.00),
                                                    data.gameA_maomaoAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_AMOUNT),
                                                    data.gameA_maomaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOMAO_COST),
                                                    data.gameA_maowoAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_AMOUNT),
                                                    data.gameA_maowoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_MAOWO_COST),
                                                    data.gameA_zhuangyuanAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_AMOUNT),
                                                    data.gameA_zhuangyuanCost.getOrDefault(playerUUID, GAME_A_DEFAULT_ZHUANGYUAN_COST),
                                                    data.gameA_luzaoAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_AMOUNT),
                                                    data.gameA_luzaoCost.getOrDefault(playerUUID, GAME_A_DEFAULT_LUZAO_COST),
                                                    data.gameA_kejiAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_AMOUNT),
                                                    data.gameA_kejiCost.getOrDefault(playerUUID, GAME_A_DEFAULT_KEJI_COST),
                                                    data.gameA_gongchangAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_AMOUNT),
                                                    data.gameA_gongchangCost.getOrDefault(playerUUID, GAME_A_DEFAULT_GONGCHANG_COST),
                                                    data.gameA_fuwenAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_AMOUNT),
                                                    data.gameA_fuwenCost.getOrDefault(playerUUID, GAME_A_DEFAULT_FUWEN_COST),
                                                    data.gameA_shuijingAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_AMOUNT),
                                                    data.gameA_shuijingCost.getOrDefault(playerUUID, GAME_A_DEFAULT_SHUIJING_COST),
                                                    data.gameA_huojianAmount.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_AMOUNT),
                                                    data.gameA_huojianCost.getOrDefault(playerUUID, GAME_A_DEFAULT_HUOJIAN_COST));
            player.openInventory(gameInv);
            // 切换玩家页面状态
            data.scene.putIfAbsent(playerUUID, "Minecraft");
            data.scene.put(playerUUID, "gameA");

            return true;
        }

        // ./tp_A game B
        if (args.length == 2 && args[0].equals("game") && args[1].equals("B")){
            if (!(sender instanceof Player player)) {
                sender.sendMessage("只有玩家能打开生命之树菜单。");
                return true;
            }
            sender.sendMessage("§6[TestPlugin_§4A§6] §7你进入了 §2♣生♣命♣之♣树♣ §7小游戏喵awa");
            gameBMenus.openMain(player);
            data.scene.put(player.getUniqueId(), "gameB");

            return true;
        }

        // ./tp_A arcade：打开五个新 GUI 小游戏的统一大厅
        if (args.length == 1 && args[0].equalsIgnoreCase("arcade")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("只有玩家能打开小游戏大厅。");
                return true;
            }
            arcadeManager.openHub(player);
            return true;
        }

        // 也允许直接进入：/tp_A game pet|snake|miner|fishing|alchemy
        if (args.length == 2 && args[0].equalsIgnoreCase("game")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("只有玩家能打开小游戏。");
                return true;
            }
            if (arcadeManager.openGame(player, args[1])) return true;
        }

        return false;
    }
}
