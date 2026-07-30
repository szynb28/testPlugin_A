package com.testPlugin_A.gameb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameBStorage {
    private final JavaPlugin plugin;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path file;

    public GameBStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = plugin.getDataFolder().toPath().resolve("gameB_data.json");
    }

    public void load(GameBRepository repository) {
        if (!Files.exists(file)) return;
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            SaveData data = gson.fromJson(reader, SaveData.class);
            repository.replaceAll(data == null || data.profiles == null ? new LinkedHashMap<>() : data.profiles);
            plugin.getLogger().info("成功加载 gameB 数据。");
        } catch (Exception exception) {
            plugin.getLogger().severe("加载 gameB 数据失败，已保留空档案：" + exception.getMessage());
        }
    }

    public void save(GameBRepository repository) {
        try {
            Files.createDirectories(file.getParent());
            Path temporary = file.resolveSibling(file.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                gson.toJson(new SaveData(repository.snapshot()), writer);
            }
            try {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            plugin.getLogger().severe("保存 gameB 数据失败：" + exception.getMessage());
        }
    }

    private static class SaveData {
        Map<String, GameBProfile> profiles = new LinkedHashMap<>();
        SaveData(Map<String, GameBProfile> profiles) { this.profiles = profiles; }
    }
}
