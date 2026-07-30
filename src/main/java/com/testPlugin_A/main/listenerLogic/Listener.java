package com.testPlugin_A.main.listenerLogic;

import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.main.Main;
import com.testPlugin_A.main.listenerLogic.inventoryClick.GameA_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClick.GameB_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClick.HelpMenu_clickLogic;
import com.testPlugin_A.main.listenerLogic.inventoryClose.All_CloseLogic;
import com.testPlugin_A.main.listenerLogic.timerExecuting.GameA_timerLogic;
import com.testPlugin_A.main.listenerLogic.timerExecuting.GameB_timerLogic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.N;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.UUID;

import static com.testPlugin_A.main.packs.ConstantPack.*;
import static com.testPlugin_A.main.packs.InteractorPack.*;

public class Listener implements org.bukkit.event.Listener {

    private final DataInitiator data;

    public Listener(DataInitiator data){
        this.data = data;
    }

    // 玩家进入游戏执行的逻辑
    @EventHandler
    public void playerJoin(PlayerJoinEvent joinEvent){
        Player player = joinEvent.getPlayer();
        String playerName = player.getName();

        String msg = Main.main.getConfig().getString("Msgs.JoinMessage");
        String replaced = msg.replace("%player%", playerName);
        joinEvent.joinMessage(MiniMessage.miniMessage().deserialize(replaced));
    }

    // 容器点击操作时执行的逻辑
    @EventHandler
    public void itemClick(InventoryClickEvent clickEvent){
        // 只处理 GUI 内的点击
        if (clickEvent.getClickedInventory() == null) return;

        if (clickEvent.getClickedInventory() != clickEvent.getInventory()) return;

        String title = clickEvent.getView().getTitle();

        // 初始化容器逻辑
        HelpMenu_clickLogic helpMenuClickLogic = new HelpMenu_clickLogic(clickEvent);
        GameA_clickLogic gameAClickLogic = new GameA_clickLogic(clickEvent, data);
        GameB_clickLogic gameBClickLogic = new GameB_clickLogic(clickEvent, data);

        // 根据点击的容器种类判断执行哪个容器的逻辑
        if (title.equals(HELP_MENU_TITLE)){
            helpMenuClickLogic.logic();
        }
        else if (title.equals(GAME_A_MENU_TITLE)){
            gameAClickLogic.logic();
        }
        else if (title.equals(GAME_B_MENU_TITLE)){
            gameBClickLogic.logic();
        }

    }

    // 随时间流逝持续执行的逻辑
    public void timerLogic(){
        new BukkitRunnable(){
            @Override
            public void run(){
                // 初始化容器逻辑
                GameA_timerLogic gameATimerLogic = new GameA_timerLogic(data);
                GameB_timerLogic gameBTimerLogic = new GameB_timerLogic(data);

                // 执行所有与时间流逝逻辑相关的容器逻辑
                gameATimerLogic.logic();
                gameBTimerLogic.logic();

            }
        }.runTaskTimer(Main.main, 0L, 20L); // 20 tick = 1 秒
    }

    // （测试用的）随时间流逝持续执行的逻辑
    public void timerLogic_forTest(){
        new BukkitRunnable(){
            @Override
            public void run(){
                for (Player player : Bukkit.getOnlinePlayers()) {

                    UUID playerUUID = player.getUniqueId();

                    // 当玩家按下W键时，执行移动
                    if (data.isPlayerPressKeyW.getOrDefault(playerUUID, false)){

                        // ✦✦✦ 移动第一个载具的逻辑 ========================================================================

                        // UUID playerUUID = player.getUniqueId();
                        Entity vehicle_1 = player.getVehicle();
                        if (vehicle_1 == null || !vehicle_1.isValid()){
                            continue;
                        }
                        // 获取载具的位置，为下一个载具做运算使用
                        Location vehicle_loc1 = vehicle_1.getLocation();

                        // 获取玩家水平朝向(yaw)，忽略上下俯仰
                        float playerYaw = player.getLocation().getYaw();

                        // 将 Yaw 转换为水平方向向量
                        // yaw=0 时朝北(Z-)，yaw=90 时朝东(X+)
                        double yawRad = Math.toRadians(playerYaw);
                        double dx = -Math.sin(yawRad);
                        double dz = Math.cos(yawRad);

                        double speed = 0.2; // 每 tick 移动速度，可调

                        // 定义第一个载具的速度矢量
                        Vector3f v1 = new Vector3f(
                                (float) (dx * speed),
                                (float) (0 * speed),
                                (float) (dz * speed)
                        );

                        // 移动 第一个载具 ArmorStand（朝玩家面向方向）
                        Location newVehicleLoc_1 = vehicle_1.getLocation().clone();
                        newVehicleLoc_1.add(v1.x, v1.y, v1.z);
                        newVehicleLoc_1.setYaw(playerYaw); // todo 载具也面朝旋转方向(但是载具隐形了，朝没朝向都一样的)
                        newVehicleLoc_1.setPitch(0);
                        vehicle_1.teleport(newVehicleLoc_1);

                        // 获取实体的数据容器
                        PersistentDataContainer pdc_1 = vehicle_1.getPersistentDataContainer();

                        // 读取挂着的展示方块实体 UUID，找到并处理逻辑
                        NamespacedKey blockDisplay_1_Key = new NamespacedKey(Main.main, "linked_blockDisplay_1");
                        String blockDisplayUuidStr_1 = pdc_1.get(blockDisplay_1_Key, PersistentDataType.STRING);
                        if (blockDisplayUuidStr_1 != null){
                            UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr_1);
                            Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
                            if (blockDisplay != null && blockDisplay.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                                // 让方块展示实体跟着移动
                                Location newDisplayLoc = blockDisplay.getLocation().clone().add(dx * speed, 0, dz * speed);
                                newDisplayLoc.setYaw(playerYaw); // 方块朝向玩家方向
                                newDisplayLoc.setPitch(0);
                                blockDisplay.teleport(newDisplayLoc);
                            }
                        }

                        // ✦✦✦ 移动第二个载具的逻辑 ========================================================================

                        // ★ 直接从 第一个盔甲架 的 PDC 读 第二个盔甲架 的 UUID，获取 第二个盔甲架
                        NamespacedKey linkedSeat2Key = new NamespacedKey(Main.main, "linked_seat_2");
                        String seat2UuidStr = pdc_1.get(linkedSeat2Key, PersistentDataType.STRING);

                        Entity vehicle_2 = null;
                        if (seat2UuidStr != null){
                            vehicle_2 = Bukkit.getEntity((UUID.fromString(seat2UuidStr)));
                        }

                        if (vehicle_2 == null || !vehicle_2.isValid()){
                            System.out.println("seat_2 丢失或无效！");
                            continue;
                        }

                        // 获取载具的位置，为做运算使用
                        Location vehicle_loc2 = vehicle_2.getLocation();

                        // 根据递推关系式计算速度矢量
                        //-// 获取前后两载具空间质点坐标
                        Vector3f vehicle_P1 = new Vector3f(
                                (float) vehicle_loc1.getX(),
                                (float) vehicle_loc1.getY(),
                                (float) vehicle_loc1.getZ()
                        );
                        Vector3f vehicle_P2 = new Vector3f(
                                (float) vehicle_loc2.getX(),
                                (float) vehicle_loc2.getY(),
                                (float) vehicle_loc2.getZ()
                        );
                        //-// 获取前后两载具位移向量
                        Vector3f d_P2_to_P1 = new Vector3f(
                                (float) (vehicle_P1.x - vehicle_P2.x),
                                (float) (vehicle_P1.y - vehicle_P2.y),
                                (float) (vehicle_P1.z - vehicle_P2.z)
                        );
                        //-// 开始套递推公式计算：
                        //-//-// 获取位移向量和速度向量点积
                        float dot_product = (d_P2_to_P1.x * v1.x) + (d_P2_to_P1.y * v1.y) + (d_P2_to_P1.z * v1.z);
                        //-//-// 获取位移向量和速度向量模之积
                        float modulus_product = (float) (Math.pow(Math.pow(d_P2_to_P1.x, 2) + Math.pow(d_P2_to_P1.y, 2) + Math.pow(d_P2_to_P1.z, 2), 0.5)
                                * Math.pow(Math.pow(v1.x, 2) + Math.pow(v1.y, 2) + Math.pow(v1.z, 2), 0.5));
                        //-//-// 计算系数
                        float k = dot_product / modulus_product;
                        //-//-// 计算前后载具位移向量的单位向量
                        //-//-//-// 计算前后载具位移模长
                        float modulus_of_d = (float) Math.pow(Math.pow(d_P2_to_P1.x, 2) + Math.pow(d_P2_to_P1.y, 2) + Math.pow(d_P2_to_P1.z, 2), 0.5);
                        //-//-//-// 计算单位向量
                        Vector3f d_P2_to_P1_unit = new Vector3f(
                                d_P2_to_P1.x / modulus_of_d,
                                d_P2_to_P1.y / modulus_of_d,
                                d_P2_to_P1.z / modulus_of_d
                        );

                        //-//-// 计算后一个载具速度矢量(k是cos，speed就是v1的大小，speed * cos就是这个速度在该方向的分量了awa)
                        Vector3f v2 = new Vector3f(
                                (float) (d_P2_to_P1_unit.x * k * speed),
                                (float) (d_P2_to_P1_unit.y * k * speed),
                                (float) (d_P2_to_P1_unit.z * k * speed)
                        );

                        // 移动 第一个载具 ArmorStand（朝玩家面向方向）
                        Location newVehicleLoc_2 = vehicle_2.getLocation().clone();
                        newVehicleLoc_2.add(v2.x, v2.y, v2.z);
                        newVehicleLoc_2.setYaw(playerYaw); // todo 载具也面朝旋转方向(但是载具隐形了，朝没朝向都一样的)
                        newVehicleLoc_2.setPitch(0);
                        vehicle_2.teleport(newVehicleLoc_2);

                        // 获取实体的数据容器
                        PersistentDataContainer pdc_2 = vehicle_2.getPersistentDataContainer();

                        // 读取挂着的展示方块实体 UUID，找到并处理逻辑
                        NamespacedKey blockDisplay_2_Key = new NamespacedKey(Main.main, "linked_blockDisplay_2");
                        String blockDisplayUuidStr_2 = pdc_2.get(blockDisplay_2_Key, PersistentDataType.STRING);
                        if (blockDisplayUuidStr_2 != null){
                            UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr_2);
                            Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
                            if (blockDisplay != null && blockDisplay.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                                // 让方块展示实体跟着移动
                                // Location newDisplayLoc = blockDisplay.getLocation().clone().add(v2.x, v2.y, v2.z);
                                // newDisplayLoc.setYaw(playerYaw); // todo 方块朝向前一个载具(这个朝向逻辑后面要实现！)
                                // newDisplayLoc.setPitch(0);
                                blockDisplay.teleport(newVehicleLoc_2);
                            }
                        }

                        // 调试用
                        System.out.println("vehicle_P1: " + vehicle_P1);
                        System.out.println("vehicle_P2: " + vehicle_P2);
                        System.out.println("newVehicleLoc_2: " + newVehicleLoc_2);
                        System.out.println("d_P2_to_P1: " + d_P2_to_P1);
                        System.out.println("dot_product: " + dot_product);
                        System.out.println("modulus_product: " + modulus_product);
                        System.out.println("k: " + k);
                        System.out.println("v2: " + v2);
                    }
                }
            }
        }.runTaskTimer(Main.main, 0L, 1L); // 1 tick = 1/20 秒
    }

    // 玩家退出容器时执行的逻辑
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent closeEvent){
        // 初始化容器退出逻辑
        All_CloseLogic allCloseLogic = new All_CloseLogic(closeEvent, data);

        // 执行退出容器时的逻辑
        allCloseLogic.logic();
    }

    // 玩家右键点击实体的逻辑
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        // 获取实体的数据容器
        PersistentDataContainer pdc = clicked.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.main, "reward_entity");

        // 检查是否有我们的标签("reward_entity")
        if (!pdc.has(key, PersistentDataType.BYTE)){ // Q:这咋判断的？ <- A:它直接判断有没有BYTE的数据形式。不是判断值
            return;
        }

        // 取消默认交互（防止打开盔甲架装备栏(*?)）
        event.setCancelled(true);

        // 读取这个实体的命令标签
        NamespacedKey cmdKey = new NamespacedKey(Main.main, "execute_command");
        String command = pdc.get(cmdKey, PersistentDataType.STRING);

        // 让玩家执行命令
        if (command != null){
            player.performCommand(command);
        }

        // 给玩家钻石
        player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        player.sendMessage("§6✦ 你获得了一颗钻石！");

        // 播放粒子效果
        player.getWorld().spawnParticle(
                Particle.HAPPY_VILLAGER,
                clicked.getLocation().add(0, 1, 0),
                10, 0.3, 0.3, 0.3
        );

        // 读取挂着的盔甲架 UUID，找到并删除
        NamespacedKey standKey = new NamespacedKey(Main.main, "linked_stand");
        String standUuidStr = pdc.get(standKey, PersistentDataType.STRING);
        if (standUuidStr != null){
            UUID standUuid = UUID.fromString(standUuidStr);
            Entity stand = Bukkit.getEntity(standUuid);
            if (stand != null && stand.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                stand.remove(); // 删除盔甲架
            }
        }

        // 读取挂着的展示方块实体 UUID，找到并删除
        NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplayKey");
        String blockDisplayUuidStr = pdc.get(blockDisplayKey, PersistentDataType.STRING);
        if (blockDisplayUuidStr != null){
            UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr);
            Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
            if (blockDisplay != null && blockDisplay.isValid()){
                blockDisplay.remove(); // 删除方块展示实体
            }
        }

        // 删除自己（领完就消失，也可以不删让它一直存在）
        clicked.remove();
    }

    // 玩家交互逻辑
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        // 只处理右键（空气或方块）
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK){
            return; // 不管左键的交互
        }

        // 获取主手物品
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // 读取物品PDC
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.main, "special_item");

        if (!pdc.has(key, PersistentDataType.STRING)) return; // 不是带标记的目标特殊物品

        String type = pdc.get(key, PersistentDataType.STRING);
        Player player = event.getPlayer();

        // 取消默认交互（防止手持方块时右键放地上、吃东西等）
        event.setCancelled(true);

        // 更具类型执行不同操作
        switch (type){
            case "magic_wand" -> {
                player.sendMessage("§d✦ 魔法发动！");
                player.performCommand("tp_A game A");
            }
            case "summoner" -> {
                player.sendMessage("§a✦ 召唤发动！");
                removePDCItem(player, Main.main, "summoner", "special_item", 1);
            }
        }
    }

    // 玩家离开载具逻辑
    @EventHandler
    public void onDismount(EntityDismountEvent event){
        // ✦✦✦ 删除第一个座位 ============================================================================================

        Entity vehicle = event.getDismounted();
        if (!(vehicle instanceof ArmorStand stand)) return;

        // 检查是不是我们的座位
        NamespacedKey key_1 = new NamespacedKey(Main.main, "seat_entity_1");
        if (!stand.getPersistentDataContainer().has(key_1, PersistentDataType.BYTE)) return;

        // 延迟 1 tick 删除，避免事件期间删实体出问题
        Bukkit.getScheduler().runTask(Main.main, () -> {

            // 获取实体的数据容器
            PersistentDataContainer pdc = stand.getPersistentDataContainer();

            // 读取挂着的展示方块实体 UUID，找到并删除
            NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplay_1");
            String blockDisplayUuidStr = pdc.get(blockDisplayKey, PersistentDataType.STRING);
            if (blockDisplayUuidStr != null){
                UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr);
                Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
                if (blockDisplay != null && blockDisplay.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                    blockDisplay.remove();
                }
            }

            if (stand.isValid()) stand.remove();
        });

        // ✦✦✦ 删除第二个座位 ============================================================================================

        // ★ 直接从 第一个盔甲架 的 PDC 读 第二个盔甲架 的 UUID，获取 第二个盔甲架
        PersistentDataContainer pdc_1 = stand.getPersistentDataContainer();

        NamespacedKey linkedSeat2Key = new NamespacedKey(Main.main, "linked_seat_2");
        String seat2UuidStr = pdc_1.get(linkedSeat2Key, PersistentDataType.STRING);

        Entity vehicle_2 = null;
        if (seat2UuidStr != null){
            vehicle_2 = Bukkit.getEntity((UUID.fromString(seat2UuidStr)));
        }

        if (vehicle_2 == null || !vehicle_2.isValid()){
            System.out.println("seat_2 丢失或无效！");
        }

        // 检查是不是我们的座位
        NamespacedKey key_2 = new NamespacedKey(Main.main, "seat_entity_2");
        if (!vehicle_2.getPersistentDataContainer().has(key_2, PersistentDataType.BYTE)) return;

        // 延迟 1 tick 删除，避免事件期间删实体出问题
        Bukkit.getScheduler().runTask(Main.main, () -> {

            // 获取实体的数据容器
            PersistentDataContainer pdc = vehicle_2.getPersistentDataContainer();

            // 读取挂着的展示方块实体 UUID，找到并删除
            NamespacedKey blockDisplayKey = new NamespacedKey(Main.main, "linked_blockDisplay_2");
            String blockDisplayUuidStr = pdc.get(blockDisplayKey, PersistentDataType.STRING);
            if (blockDisplayUuidStr != null){
                UUID blockDisplayUuid = UUID.fromString(blockDisplayUuidStr);
                Entity blockDisplay = Bukkit.getEntity(blockDisplayUuid);
                if (blockDisplay != null && blockDisplay.isValid()){ // Q:isValid()是啥意思？-> A:实体还活着，在世界中，可以操作
                    blockDisplay.remove();
                }
            }

            if (vehicle_2.isValid()) vehicle_2.remove();
        });
    }

    // 移除有STRING PDC标签的特定物品
    public void removePDCItem(Player player, JavaPlugin plugin, String PDC, String keyName, int removeAmount){
        NamespacedKey key = new NamespacedKey(plugin, keyName);

        for (int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack item = player.getInventory().getItem(i);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            // 检查PDC标签
            String type = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (PDC.equals(type)){
                // 遍历到指定物品！减少
                if (item.getAmount() > removeAmount){
                    item.setAmount(item.getAmount() - removeAmount);
                } else {
                    player.getInventory().setItem(i, null); // 只剩removeAmount个(或更少)，直接全删
                }
            }
        }
    }
}
