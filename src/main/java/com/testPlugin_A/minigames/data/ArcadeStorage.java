package com.testPlugin_A.minigames.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

/** 五个新小游戏共用的独立 JSON 存储，不触碰游戏 A 和生命之树存档。 */
public class ArcadeStorage {
    private final JavaPlugin plugin;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path file;

    public ArcadeStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        file = plugin.getDataFolder().toPath().resolve("arcade_data.json");
    }

    public void load(ArcadeRepository repository) {
        if (!Files.exists(file)) return;
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            SaveData data = gson.fromJson(reader, SaveData.class);
            repository.replaceAll(data == null ? null : data.profiles);
        } catch (Exception exception) {
            plugin.getLogger().severe("加载小游戏大厅数据失败：" + exception.getMessage());
        }
    }

    public void save(ArcadeRepository repository) {
        try {
            Files.createDirectories(file.getParent());
            Path temporary = file.resolveSibling("arcade_data.json.tmp");
            try (Writer writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                gson.toJson(new SaveData(repository.snapshot()), writer);
            }
            try {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            plugin.getLogger().severe("保存小游戏大厅数据失败：" + exception.getMessage());
        }
    }

    private static class SaveData {
        Map<String, ArcadeProfile> profiles = new LinkedHashMap<>();
        SaveData(Map<String, ArcadeProfile> profiles) { this.profiles = profiles; }
    }
}
