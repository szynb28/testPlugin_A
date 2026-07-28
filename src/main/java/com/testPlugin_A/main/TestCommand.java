package com.testPlugin_A.main;

import com.testPlugin_A.data.DataInitiator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.testPlugin_A.main.packs.ItemsPack.*;
import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InventoriesPack.*;

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

        return false;
    }
}
