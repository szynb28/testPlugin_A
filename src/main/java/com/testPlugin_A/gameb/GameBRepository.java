package com.testPlugin_A.gameb;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GameBRepository {
    private Map<String, GameBProfile> profiles = new LinkedHashMap<>();

    public GameBProfile profile(UUID playerId) {
        return profiles.computeIfAbsent(playerId.toString(), ignored -> new GameBProfile());
    }

    public GameBProfile find(UUID playerId) { return profiles.get(playerId.toString()); }
    public Collection<Map.Entry<String, GameBProfile>> entries() { return profiles.entrySet(); }
    public Map<String, GameBProfile> snapshot() { return new LinkedHashMap<>(profiles); }
    public void replaceAll(Map<String, GameBProfile> loaded) { profiles = loaded == null ? new LinkedHashMap<>() : new LinkedHashMap<>(loaded); }
}
