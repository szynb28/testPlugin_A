package com.testPlugin_A.main.packs;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import static com.testPlugin_A.main.packs.ConstantPack.GAME_A_MENU_TITLE;
import static com.testPlugin_A.main.packs.ConstantPack.HELP_MENU_TITLE;
import static com.testPlugin_A.main.packs.ItemsPack.*;

public class InventoriesPack {
    // 主菜单
    //-// 帮助菜单
    static public Inventory get__inventory_helpMenu(){
        Inventory inv = Bukkit.createInventory(null, 54, HELP_MENU_TITLE);
        for (int i = 0; i <= 53; i++){
            inv.setItem(i, get__item_blackBackGround());
        }
        inv.setItem(13, get__item_menuHelpBook());

        return inv;
    }
    //-// 游戏菜单- 饼干点击
    static public Inventory get__inventory_gameA(double cookieAmount,
                                                 int maomaoAmount,
                                                 double maomaoCost){
        Inventory inv = Bukkit.createInventory(null, 54, GAME_A_MENU_TITLE);
        for (int i = 0; i <= 53; i++){
            inv.setItem(i, get__item_blackBackGround());
        }
        inv.setItem(22, get__item_gameAcookie(cookieAmount));
        inv.setItem(36, get__item_gameAmaomao(maomaoAmount, maomaoCost));

        return inv;
    }
}
