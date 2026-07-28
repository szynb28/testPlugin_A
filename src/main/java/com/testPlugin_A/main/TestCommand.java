package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
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

    public TestCommand(DataInitiator data){
        this.data = data;
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
            org.bukkit.entity.Interaction interaction = (org.bukkit.entity.Interaction) player.getWorld().spawnEntity(spawnLoc, EntityType.INTERACTION);
            interaction.setInteractionWidth(1.0f); // 覆盖盔甲架区域
            interaction.setInteractionHeight(2.0f);
            interaction.setResponsive(true);       // 右键有反馈动画

            // 设置方块展示实体
            BlockDisplay blockDisplay = get__blockDisplay(playerLoc,
                    Material.DIAMOND_BLOCK,
                    new Vector3f(0f, .5f, 0f),
                    new Vector3f(.5f, .5f, .5f));

            // 给Interaction打标签（PersistentDataContainer）
            NamespacedKey rewardKey = new NamespacedKey(Main.main, "reward_entity");
            interaction.getPersistentDataContainer().set(rewardKey, PersistentDataType.BYTE, (byte) 1); // (byte) 1 只是一个标记

            // 给Interaction写入要执行的命令的标签
            NamespacedKey cmdKey = new NamespacedKey(Main.main, "execute_command");
            interaction.getPersistentDataContainer().set(cmdKey, PersistentDataType.STRING, "tp_A game A");

            // 把盔甲架的 UUID 存到 Interaction 的 PDC 里（*group ID）
            NamespacedKey standKey = new NamespacedKey(Main.main, "linked_stand");
            interaction.getPersistentDataContainer().set(standKey, PersistentDataType.STRING, stand.getUniqueId().toString());

            // 把展示方块实体的 UUID 存到 Interaction 的 PDC 里（*group ID）
            NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplayKey");
            interaction.getPersistentDataContainer().set(blockDisplayKey, PersistentDataType.STRING, blockDisplay.getUniqueId().toString());

            player.sendMessage("§a奖励实体已生成！右键它领取钻石喵～");
            return true;
        }

        // ./tp_A getspecialitem
        if (args.length == 1 && args[0].equals("getspecialitem")){
            Player player = (Player) sender;

            player.getInventory().addItem(get__item_magicWand(Main.main));
            player.sendMessage("§6✦ 你获得了一根魔杖！");
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
            // 初始化(获取数据核心)

            // 聊天栏显示提示信息
            sender.sendMessage("§6[TestPlugin_§4A§6] §7你进入了 §2♣生♣命♣之♣树♣ §7小游戏喵awa");
            // 打开生命之树小游戏页面
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            // 图案参数相关计算与判断
            //-// 是否正在等待挖掘树穴
            boolean isWaitingShovel = false;
            if (data.gameB_shovelTimer.getOrDefault(playerUUID, 0.0) > 0) isWaitingShovel = true;
            //-// 当前挖掘树穴的剩余时间
            double waitingTime;
            waitingTime = data.gameB_shovelTimer.getOrDefault(playerUUID, 0.0);
            // 应用参数并创建容器
            Inventory gameInv = get__inventory_gameB(data.gameB_plantStage.getOrDefault(playerUUID, "未开荒"),
                    isWaitingShovel,
                    waitingTime); // todo last
            player.openInventory(gameInv);
            // 切换玩家页面状态
            data.scene.putIfAbsent(playerUUID, "Minecraft");
            data.scene.put(playerUUID, "gameB");

            return true;
        }

        return false;
    }
}
