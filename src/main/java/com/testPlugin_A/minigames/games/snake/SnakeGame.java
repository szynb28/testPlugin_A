package com.testPlugin_A.minigames.games.snake;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.core.ArcadeManager;
import com.testPlugin_A.minigames.core.GuiToolkit;
import com.testPlugin_A.minigames.data.SnakeData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/** 5x9 棋盘式贪吃蛇；方向按钮位于 GUI 最后一行。 */
public class SnakeGame implements GuiGame {
    public static final String TITLE = ArcadeManager.TITLE_PREFIX + "§a贪吃蛇";
    private static final int BOARD_SIZE = 45;
    private final ArcadeManager arcade;
    private final GuiToolkit gui;
    private final Random random = new Random();
    private final Map<UUID, Session> sessions = new HashMap<>();

    public SnakeGame(ArcadeManager arcade) { this.arcade = arcade; this.gui = arcade.gui(); }
    public String id() { return "snake"; }
    public String title() { return TITLE; }
    public String displayName() { return "贪吃蛇"; }
    public String description() { return "在 5×9 GUI 棋盘中控制蛇吃苹果"; }
    public Material icon() { return Material.SLIME_BALL; }

    public void open(Player player) {
        Session session = sessions.computeIfAbsent(player.getUniqueId(), ignored -> newSession());
        Inventory inventory = Bukkit.createInventory(null, 54, TITLE);
        render(inventory, session, player);
        player.openInventory(inventory);
    }

    public void handleAction(Player player, String action) {
        if (action.equals("back")) { sessions.remove(player.getUniqueId()); arcade.openHub(player); return; }
        Session session = sessions.get(player.getUniqueId());
        if (action.equals("restart")) {
            session = newSession();
            sessions.put(player.getUniqueId(), session);
            render(player.getOpenInventory().getTopInventory(), session, player);
            return;
        }
        if (session == null || !session.running) return;
        Direction requested = switch (action) {
            case "up" -> Direction.UP; case "down" -> Direction.DOWN; case "left" -> Direction.LEFT; case "right" -> Direction.RIGHT;
            default -> session.direction;
        };
        if (!requested.opposite(session.direction)) session.nextDirection = requested;
    }

    public void tick() {
        for (Map.Entry<UUID, Session> entry : new ArrayList<>(sessions.entrySet())) {
            Player player = Bukkit.getPlayer(entry.getKey());
            Session session = entry.getValue();
            if (player == null || !player.isOnline() || !player.getOpenInventory().getTitle().equals(TITLE)) continue;
            if (!session.running) continue;
            session.tickCounter++;
            if (session.tickCounter < Math.max(1, 4 - session.score / 5)) continue;
            session.tickCounter = 0;
            move(session, player);
            render(player.getOpenInventory().getTopInventory(), session, player);
        }
    }

    public void onClose(Player player) { sessions.remove(player.getUniqueId()); }

    private void move(Session session, Player player) {
        session.direction = session.nextDirection;
        int head = session.body.peekFirst();
        int row = head / 9;
        int column = head % 9;
        switch (session.direction) {
            case UP -> row--; case DOWN -> row++; case LEFT -> column--; case RIGHT -> column++;
        }
        if (row < 0 || row >= 5 || column < 0 || column >= 9) { gameOver(session, player); return; }
        int next = row * 9 + column;
        if (session.body.contains(next)) { gameOver(session, player); return; }
        session.body.addFirst(next);
        if (next == session.food) {
            session.score++;
            placeFood(session);
        } else session.body.removeLast();
    }

    private void gameOver(Session session, Player player) {
        session.running = false;
        SnakeData data = arcade.profile(player).snake;
        data.gamesPlayed++;
        data.bestScore = Math.max(data.bestScore, session.score);
        player.sendMessage("§a[贪吃蛇] §c游戏结束！得分 " + session.score + "，最高 " + data.bestScore);
    }

    private void render(Inventory inventory, Session session, Player player) {
        for (int slot = 0; slot < BOARD_SIZE; slot++) inventory.setItem(slot, gui.item(Material.GRAY_STAINED_GLASS_PANE, " ", List.of()));
        int index = 0;
        for (int position : session.body) {
            inventory.setItem(position, gui.item(index++ == 0 ? Material.LIME_CONCRETE : Material.GREEN_CONCRETE, "§a蛇", List.of()));
        }
        inventory.setItem(session.food, gui.item(Material.APPLE, "§c苹果", List.of("§7吃掉后得分 +1")));
        inventory.setItem(45, gui.button(Material.ARROW, "§f返回", List.of(), id(), "back"));
        inventory.setItem(46, gui.button(Material.ARROW, "§e↑", List.of(), id(), "up"));
        inventory.setItem(48, gui.button(Material.ARROW, "§e←", List.of(), id(), "left"));
        inventory.setItem(49, gui.item(Material.PAPER, "§6得分: " + session.score,
                List.of("§7最高分: §e" + arcade.profile(player).snake.bestScore)));
        inventory.setItem(50, gui.button(Material.ARROW, "§e→", List.of(), id(), "right"));
        inventory.setItem(52, gui.button(Material.ARROW, "§e↓", List.of(), id(), "down"));
        inventory.setItem(53, gui.button(Material.SLIME_BALL, session.running ? "§a重新开始" : "§c游戏结束 - 点击重开", List.of(), id(), "restart"));
    }

    private Session newSession() {
        Session session = new Session();
        session.body.add(22); session.body.add(21); session.body.add(20);
        placeFood(session);
        return session;
    }

    private void placeFood(Session session) {
        if (session.body.size() >= BOARD_SIZE) { session.running = false; return; }
        do { session.food = random.nextInt(BOARD_SIZE); } while (session.body.contains(session.food));
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
        boolean opposite(Direction other) { return (this == UP && other == DOWN) || (this == DOWN && other == UP) || (this == LEFT && other == RIGHT) || (this == RIGHT && other == LEFT); }
    }

    private static class Session {
        final Deque<Integer> body = new ArrayDeque<>();
        Direction direction = Direction.RIGHT;
        Direction nextDirection = Direction.RIGHT;
        int food;
        int score;
        int tickCounter;
        boolean running = true;
    }
}
