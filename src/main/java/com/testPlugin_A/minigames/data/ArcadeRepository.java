package com.testPlugin_A.minigames.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ArcadeRepository {
    private Map<String, ArcadeProfile> profiles = new LinkedHashMap<>();

    public ArcadeProfile profile(UUID playerId, String playerName) {
        ArcadeProfile profile = profiles.computeIfAbsent(playerId.toString(), ignored -> new ArcadeProfile());
        profile.repair();
        if (playerName != null && !playerName.isBlank()) profile.lastKnownName = playerName;
        return profile;
    }

    public Map<String, ArcadeProfile> snapshot() { return new LinkedHashMap<>(profiles); }
    public void replaceAll(Map<String, ArcadeProfile> loaded) {
        profiles = loaded == null ? new LinkedHashMap<>() : new LinkedHashMap<>(loaded);
        profiles.values().forEach(ArcadeProfile::repair);
    }
}
