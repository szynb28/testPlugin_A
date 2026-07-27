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
    static public ItemStack get__item_gameAzhuangyuan(int zhuangyuanAmount, double zhuangyuanCost){
        ItemStack item = new ItemStack(Material.getMaterial("BELL"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:yellow:gold>猫猫庄园</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>一个独属于猫猫大厨的庄园喵~</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>庄园里不仅有大厨房、大炉灶...还有一群勤劳的猫猫!</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + zhuangyuanAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + zhuangyuanCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAluzao(int luzaoAmount, double luzaoCost){
        ItemStack item = new ItemStack(Material.getMaterial("FURNACE_MINECART"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>移动炉灶基地</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>一个可以让厨房跑来跑去的移动基地☆</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>从此猫猫大厨可以一边旅游一边制作曲奇饼干啦</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + luzaoAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + luzaoCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAkeji(int kejiAmount, double kejiCost){
        ItemStack item = new ItemStack(Material.getMaterial("REDSTONE"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:aqua:blue>饼干科技</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>猫猫博士研究出的饼干流水线工程!</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>猫猫大厨们提供的秘方在机械加持下如虎添翼~</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + kejiAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + kejiCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAgongchang(int gongchangAmount, double gongchangCost){
        ItemStack item = new ItemStack(Material.getMaterial("WAXED_CHISELED_COPPER"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:yellow:light_purple>全自动饼干工厂</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>一个只有机械臂总控的大工厂☆</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>猫猫们只要在后台看着流水线自己造化就行啦~</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + gongchangAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + gongchangCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAfuwen(int fuwenAmount, double fuwenCost){
        ItemStack item = new ItemStack(Material.getMaterial("FLOW_POTTERY_SHERD"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:gold:gray>饼干符文</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>猫猫们在地层下发掘出了可以让饼干变得更好吃的符文阵法۞</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>有了这种神秘力量存在，饼干的销量被指数级提升了!</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + fuwenAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + fuwenCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAshuijing(int shuijingAmount, double shuijingCost){
        ItemStack item = new ItemStack(Material.getMaterial("AMETHYST_SHARD"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:light_purple:dark_purple>水晶饼干碎片</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>在远处的水晶山上，据说有珍贵的“水晶饼干碎片”</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>猫猫们辛苦开采的零星饼干碎片就能极大地提高饼干的含金量!</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + shuijingAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + shuijingCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
    static public ItemStack get__item_gameAhuojian(int huojianAmount, double huojianCost){
        ItemStack item = new ItemStack(Material.getMaterial("FIREWORK_ROCKET"), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.customName(MiniMessage.miniMessage().deserialize("<gradient:red:gold>喵星登月计划</gradient>"));
        itemMeta.lore(List.of(
                MiniMessage.miniMessage().deserialize("<gray>据说遥远的月球上有着比曲奇好吃的东西 —— 月饼!</gray>"),
                MiniMessage.miniMessage().deserialize("<gray>猫猫们乘着火箭离开喵星，去挖掘月球上的月饼酱 ☽</gray>"),
                MiniMessage.miniMessage().deserialize(""),
                MiniMessage.miniMessage().deserialize("<gold><b>当前拥有: </gold><yellow>" + huojianAmount + "</yellow>"),
                MiniMessage.miniMessage().deserialize("<green><b>购买花费: </green><light_purple>" + huojianCost + "</light_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[戳我购买!]</gray>")
        ));
        item.setItemMeta(itemMeta);
        return item;
    }
}
