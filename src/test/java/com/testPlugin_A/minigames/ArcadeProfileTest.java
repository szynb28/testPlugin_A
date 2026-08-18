package com.testPlugin_A.minigames;

import com.testPlugin_A.minigames.data.ArcadeProfile;
import com.testPlugin_A.minigames.data.ArcadeRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ArcadeProfileTest {
    @Test
    void repositoryReturnsStableProfileWithFiveGameDataSections() {
        ArcadeRepository repository = new ArcadeRepository();
        UUID playerId = UUID.randomUUID();

        ArcadeProfile first = repository.profile(playerId, "Alice");
        ArcadeProfile second = repository.profile(playerId, "Alice-New");

        assertSame(first, second);
        assertEquals("Alice-New", second.lastKnownName);
        assertNotNull(second.pet);
        assertNotNull(second.snake);
        assertNotNull(second.miner);
        assertNotNull(second.fishing);
        assertNotNull(second.alchemy);
    }

    @Test
    void repairRestoresMissingSectionsFromOlderSave() {
        ArcadeProfile profile = new ArcadeProfile();
        profile.pet = null;
        profile.miner = null;
        profile.repair();

        assertNotNull(profile.pet);
        assertNotNull(profile.miner);
    }
}
