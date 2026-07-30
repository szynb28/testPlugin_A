package com.testPlugin_A.main.packs;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import static com.testPlugin_A.main.packs.ConstantPack.*;
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
    // 游戏菜单- 饼干点击
    static public Inventory get__inventory_gameA(double cookieAmount,
                                                 double cookiePerSecond,
                                                 int maomaoAmount,
                                                 double maomaoCost,
                                                 int maowoAmount,
                                                 double maowoCost,
                                                 int zhuangyuanAmount,
                                                 double zhuangyuanCost,
                                                 int luzaoAmount,
                                                 double luzaoCost,
                                                 int kejiAmount,
                                                 double kejiCost,
                                                 int gongchangAmount,
                                                 double gongchangCost,
                                                 int fuwenAmount,
                                                 double fuwenCost,
                                                 int shuijingAmount,
                                                 double shuijingCost,
                                                 int huojianAmount,
                                                 double huojianCost){
        Inventory inv = Bukkit.createInventory(null, 54, GAME_A_MENU_TITLE);
        for (int i = 0; i <= 53; i++){
            inv.setItem(i, get__item_blackBackGround());
        }
        inv.setItem(22, get__item_gameAcookie(cookieAmount, cookiePerSecond));
        inv.setItem(36, get__item_gameAmaomao(maomaoAmount, maomaoCost));
        inv.setItem(37, get__item_gameAmaowo(maowoAmount, maowoCost));
        inv.setItem(38, get__item_gameAzhuangyuan(zhuangyuanAmount, zhuangyuanCost));
        inv.setItem(39, get__item_gameAluzao(luzaoAmount, luzaoCost));
        inv.setItem(40, get__item_gameAkeji(kejiAmount, kejiCost));
        inv.setItem(41, get__item_gameAgongchang(gongchangAmount, gongchangCost));
        inv.setItem(42, get__item_gameAfuwen(fuwenAmount, fuwenCost));
        inv.setItem(43, get__item_gameAshuijing(shuijingAmount, shuijingCost));
        inv.setItem(44, get__item_gameAhuojian(huojianAmount, huojianCost));

        return inv;
    }

}
