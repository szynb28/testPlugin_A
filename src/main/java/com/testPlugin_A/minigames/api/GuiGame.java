package com.testPlugin_A.minigames.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/** 所有 GUI 小游戏都实现同一接口，由 ArcadeManager 统一路由。 */
public interface GuiGame {
    String id();
    String title();
    String displayName();
    String description();
    Material icon();
    void open(Player player);
    void handleAction(Player player, String action);
    default void tick() { }
    default void onClose(Player player) { }
}
