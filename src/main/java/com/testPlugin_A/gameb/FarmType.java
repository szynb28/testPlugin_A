package com.testPlugin_A.gameb;

public enum FarmType {
    LIFE_TREE("life-tree", "生命树场", "生命果"),
    ORCHARD("orchard", "果园", "金苹果"),
    HERB_GARDEN("herb-garden", "药圃", "灵草");

    private final String id;
    private final String displayName;
    private final String cropName;

    FarmType(String id, String displayName, String cropName) {
        this.id = id;
        this.displayName = displayName;
        this.cropName = cropName;
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String cropName() { return cropName; }

    public static FarmType fromId(String id) {
        for (FarmType value : values()) {
            if (value.id.equals(id)) return value;
        }
        return LIFE_TREE;
    }
}
