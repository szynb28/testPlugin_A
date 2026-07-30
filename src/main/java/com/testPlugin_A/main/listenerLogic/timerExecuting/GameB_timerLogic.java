package com.testPlugin_A.main.listenerLogic.timerExecuting;

import com.testPlugin_A.gameb.GameBService;
import com.testPlugin_A.gameb.gui.GameBMenus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameB_timerLogic {
    private final GameBService service;
    private final GameBMenus menus;

    public GameB_timerLogic(GameBService service, GameBMenus menus) {
        this.service = service;
        this.menus = menus;
    }

    public void logic() {
        service.reconcileAll(System.currentTimeMillis());
        for (Player player : Bukkit.getOnlinePlayers()) menus.refreshMain(player);
    }
}
