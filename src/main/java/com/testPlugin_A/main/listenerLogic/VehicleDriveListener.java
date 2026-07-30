package com.testPlugin_A.main.listenerLogic;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import com.testPlugin_A.data.DataInitiator;
import com.testPlugin_A.data.DataStorage;
import com.testPlugin_A.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class VehicleDriveListener extends PacketListenerAbstract {
    DataInitiator data; // 数据核心

    public VehicleDriveListener(DataInitiator data){
        super(PacketListenerPriority.HIGH);
        this.data = data;
    }

    // 接到包发送信号时的逻辑
    @Override
    public void onPacketReceive(PacketReceiveEvent event){
        // 如果接到的信号不是“玩家输入(按键)”的信号，则不处理逻辑
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_INPUT) return;

        Player player = (Player) event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Entity vehicle = player.getVehicle();

        // 如果玩家乘坐的载具不是盔甲架的话，则不处理逻辑
        if (!(vehicle instanceof ArmorStand stand)) return;

        // 检查是不是我们的座位
        //-// 以"seat_entity"为标签的key
        NamespacedKey key = new NamespacedKey(Main.main, "seat_entity");
        //-// 如果这个载具以key为标签的pdc没有BYTE类型的话（玩家载具盔甲架的"seat_entity"标签有个(byte) 1标记），则不处理逻辑
        if (!stand.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;

        WrapperPlayClientPlayerInput input = new WrapperPlayClientPlayerInput(event);

        // 按Shift下车交给原版，不处理逻辑
        if (input.isShift()) return;

        // 阻止原版载具控制
        event.setCancelled(true);

        // (按下与)松开W的逻辑

        data.isPlayerPressKeyW.putIfAbsent(playerUUID, false); // 先给该玩家UUID对应的isPlayerPressKeyW赋一个值，防止isPlayerPressKeyW是null导致报错

        if (input.isForward()){
            data.isPlayerPressKeyW.put(playerUUID, true);
            System.out.println(player.getName() + "按下了W键！");
        }
        else{
            data.isPlayerPressKeyW.put(playerUUID, false);
            System.out.println(player.getName() + "松开了W键！");
        }
    }
}
