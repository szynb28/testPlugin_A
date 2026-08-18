package com.testPlugin_A.minigames.core;

import com.testPlugin_A.minigames.api.GuiGame;
import com.testPlugin_A.minigames.data.ArcadeProfile;
import com.testPlugin_A.minigames.data.ArcadeRepository;
import com.testPlugin_A.minigames.data.ArcadeStorage;
import com.testPlugin_A.minigames.games.alchemy.AlchemyGame;
import com.testPlugin_A.minigames.games.fishing.FishingGame;
import com.testPlugin_A.minigames.games.miner.MinerGame;
import com.testPlugin_A.minigames.games.pet.PetGame;
import com.testPlugin_A.minigames.games.snake.SnakeGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 五个 GUI 小游戏的总入口，负责注册、菜单路由、定时刷新和统一存档。
 * 新增游戏时只需实现 GuiGame 并在构造器中 register，无需修改总 Listener。
 */
public class ArcadeManager implements Listener {
    public static final String TITLE_PREFIX = "§8[小游戏] §r";
    public static final String HUB_TITLE = TITLE_PREFIX + "§6游乐大厅";

    private final JavaPlugin plugin;
    private final GuiToolkit gui;
    private final ArcadeRepository repository = new ArcadeRepository();
    private final ArcadeStorage storage;
    private final Map<String, GuiGame> games = new LinkedHashMap<>();

    public ArcadeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        gui = new GuiToolkit(plugin);
        storage = new ArcadeStorage(plugin);
        storage.load(repository);
        register(new PetGame(this));
        register(new SnakeGame(this));
        register(new MinerGame(this));
        register(new FishingGame(this));
        register(new AlchemyGame(this));
    }

    public void start() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> games.values().forEach(GuiGame::tick), 5L, 5L);
    }

    public GuiToolkit gui() { return gui; }
    public ArcadeProfile profile(Player player) { return repository.profile(player.getUniqueId(), player.getName()); }
    public void save() { storage.save(repository); }

    public boolean openGame(Player player, String gameId) {
        GuiGame game = games.get(gameId.toLowerCase());
        if (game == null) return false;
        game.open(player);
        return true;
    }

    public void openHub(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, HUB_TITLE);
        gui.fill(inventory);
        int[] slots = {11, 13, 15, 29, 33};
        int index = 0;
        for (GuiGame game : games.values()) {
            inventory.setItem(slots[index++], gui.button(game.icon(), "§a§l" + game.displayName(),
                    List.of("§7" + game.description(), "", "§e点击进入游戏"), "hub", "open:" + game.id()));
        }
        inventory.setItem(49, gui.item(Material.BOOK, "§6GUI 小游戏大厅",
                List.of("§7宠物养成 · 贪吃蛇 · 矿工挖矿", "§7钓鱼大师 · 炼金工坊")));
        player.openInventory(inventory);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        profile(event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(TITLE_PREFIX)) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) return;
        ItemStack clicked = event.getCurrentItem();
        String gameId = gui.game(clicked);
        String action = gui.action(clicked);
        if (gameId == null || action == null) return;
        Player player = (Player) event.getWhoClicked();
        if (gameId.equals("hub") && action.startsWith("open:")) {
            openGame(player, action.substring(5));
            return;
        }
        GuiGame game = games.get(gameId);
        if (game != null) game.handleAction(player, action);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().startsWith(TITLE_PREFIX)) return;
        Player player = (Player) event.getPlayer();
        for (GuiGame game : games.values()) {
            if (game.title().equals(event.getView().getTitle())) game.onClose(player);
        }
    }

    private void register(GuiGame game) {
        games.put(game.id(), game);
    }
}
