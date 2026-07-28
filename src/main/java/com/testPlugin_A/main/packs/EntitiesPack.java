package com.testPlugin_A.main.packs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntitiesPack {
    static public BlockDisplay get__blockDisplay(Location loc, Material material, Vector3f offset, Vector3f scale) {
        return loc.getWorld().spawn(loc, BlockDisplay.class, entity ->{
            // 显示一个钻石块
            entity.setBlock(material.createBlockData());

            // 设置变换：向上浮0.5格，缩小到0.5倍
            entity.setTransformation(new Transformation(
                    offset,  // 本地平移(相对spawn位置)
                    new Quaternionf(),                   // 左旋转（不转）
                    scale,   // 缩放
                    new Quaternionf()                    // 右旋转（不转）
            ));

            entity.setViewRange(50f);                    // 可视距离
            entity.setBrightness(new Display.Brightness(15, 15)); // 最大亮度
        });
    }
}
