package com.testPlugin_A.gameb;

public enum FertilizerTier {
    NONE("none", "自然生长"),
    BASIC("basic", "普通肥料"),
    ADVANCED("advanced", "优质肥料"),
    MASTER("master", "神奇肥料");

    private final String id;
    private final String displayName;

    FertilizerTier(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }

    public static FertilizerTier fromId(String id) {
        for (FertilizerTier value : values()) {
            if (value.id.equals(id)) return value;
        }
        return NONE;
    }
}
