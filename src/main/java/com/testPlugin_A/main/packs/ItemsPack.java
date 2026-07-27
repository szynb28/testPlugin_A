package com.testPlugin_A.main.packs;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemsPack {
    // 主菜单
    static public ItemStack get__item_blackBackGround(){
        ItemStack item = new ItemStack(Material.getMaterial("BLACK_STAINED_GLASS_PANE"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:gold:yellow>只是一株背景菌qwq</gradient>"));
        /*itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("")
        ));*/
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_menuHelpBook(){
        ItemStack item = new ItemStack(Material.getMaterial("BOOK"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:green:dark_green>TP_A插件帮助</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<red>TP_A</red><gold>是天辰的第一个正式测试插件喵！</gold>"),
                MiniMessage.miniMessage().deserialize("<gray>~戳我进入帮助详情页~</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }

    // 游戏- 饼干点击
    static public ItemStack get__item_gameAcookie(double cookieAmount){
        ItemStack item = new ItemStack(Material.getMaterial("COOKIE"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:yellow:gold>曲奇酱~</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>无辜的曲奇酱，要天天被鼠标酱戳...</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我获取曲奇qwq]</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gradient:yellow:gold>当前曲奇数量: " + cookieAmount + " 喵!~</gradient>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_buyError(){
        ItemStack item = new ItemStack(Material.getMaterial("BARRIER"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<red>购买失败喵...</red>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gold>咦，购买失败惹</gold> <white>Σ( ° △ °)</white>"),
                MiniMessage.miniMessage().deserialize("<gold>可能没有足够的资源喵..</gold>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gray>等攒够了资源再来戳我吧~</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAmaomao(int maomaoAmount, double maomaoCost){
        ItemStack item = new ItemStack(Material.getMaterial("COD"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:light_purple:red>大厨神猫猫</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>会帮主人做饭的猫猫哦~</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>不过有个小小的要求，就是要给它买小鱼干!</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + maomaoAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + maomaoCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAmaowo(int maowoAmount, double maowoCost){
        ItemStack item = new ItemStack(Material.getMaterial("BRICKS"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:red:gold>猫窝基建</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>往主人家的大旷野上基建猫窝!~</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>更好的猫窝环境可以提升猫猫的工作效率☆</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + maowoAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + maowoCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
}
