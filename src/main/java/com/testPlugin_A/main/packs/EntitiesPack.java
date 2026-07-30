package com.testPlugin_A.main.packs;

import com.testPlugin_A.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntitiesPack {
    // 任意正方块展示实体
    static public BlockDisplay get__blockDisplay(Location loc, Material material, Vector3f worldOffset, Vector3f scale) {
        // 把世界坐标偏移直接算进 spawn 位置里
        Location spawnLoc = loc.clone().add(worldOffset.x, worldOffset.y, worldOffset.z);

        // 固定朝向为 0，让本地坐标系和世界坐标系完全对齐
        spawnLoc.setYaw(0);
        spawnLoc.setPitch(0);

        BlockDisplay display =  loc.getWorld().spawn(spawnLoc, BlockDisplay.class, entity ->{
            // 显示一个钻石块
            entity.setBlock(material.createBlockData());

            // 设置变换：向上浮0.5格，缩小到0.5倍
            entity.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),  // 本地偏移归零
                    new Quaternionf(0, 0, 0, 1), // 左旋转（不转） *显式指定单位四元数（绝对无旋转）
                    scale,   // 缩放
                    new Quaternionf(0, 0, 0, 1)  // 右旋转（不转） *显式指定单位四元数（绝对无旋转）
            ));

            entity.setViewRange(50f);                    // 可视距离
            entity.setBrightness(new Display.Brightness(15, 15)); // 最大亮度
        });

        // 生成后再强制锁一次实体朝向（防御性编程）
        display.setRotation(0, 0);

        return display;
    }
    // 右键获得钻石的 interaction 实体
    static public Interaction get__rewardInteraction(Location loc, Player player){
        org.bukkit.entity.Interaction interaction = (org.bukkit.entity.Interaction) player.getWorld().spawnEntity(loc, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.0f); // 覆盖盔甲架区域
        interaction.setInteractionHeight(2.0f);
        interaction.setResponsive(true);       // 右键有反馈动画

        // 给Interaction打标签（PersistentDataContainer）
        NamespacedKey rewardKey = new NamespacedKey(Main.main, "reward_entity");
        interaction.getPersistentDataContainer().set(rewardKey, PersistentDataType.BYTE, (byte) 1); // (byte) 1 只是一个标记

        // 给Interaction写入要执行的命令的标签
        NamespacedKey cmdKey = new NamespacedKey(Main.main, "execute_command");
        interaction.getPersistentDataContainer().set(cmdKey, PersistentDataType.STRING, "tp_A game A");

        return interaction;
    }
}
