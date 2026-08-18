package com.testPlugin_A.minigames.core;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/** GUI 物品工厂：用 PDC 保存游戏和动作，避免依赖物品名称判断点击。 */
public class GuiToolkit {
    private final NamespacedKey gameKey;
    private final NamespacedKey actionKey;

    public GuiToolkit(JavaPlugin plugin) {
        gameKey = new NamespacedKey(plugin, "arcade_game");
        actionKey = new NamespacedKey(plugin, "arcade_action");
    }

    public ItemStack item(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack button(Material material, String name, List<String> lore, String gameId, String action) {
        ItemStack item = item(material, name, lore);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(gameKey, PersistentDataType.STRING, gameId);
        meta.getPersistentDataContainer().set(actionKey, PersistentDataType.STRING, action);
        item.setItemMeta(meta);
        return item;
    }

    public String game(ItemStack item) {
        return value(item, gameKey);
    }

    public String action(ItemStack item) {
        return value(item, actionKey);
    }

    public void fill(Inventory inventory) {
        ItemStack background = item(Material.BLACK_STAINED_GLASS_PANE, " ", List.of());
        for (int slot = 0; slot < inventory.getSize(); slot++) inventory.setItem(slot, background);
    }

    private String value(ItemStack item, NamespacedKey key) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
}
