package org.vinerdream.nexoItemDrops;

import org.bukkit.configuration.ConfigurationSection;
import org.vinerdream.nexoItemDrops.enums.SourceType;


public record DropData(String nexoId, SourceType sourceType, String source, double chance,
                       int quantityMin, int quantityMax, boolean dropWithSilkTouch, LootingMode lootingMode,
                       FortuneMode fortuneMode, boolean replaceOriginalDrop
) {

    public DropData(String nexoId, SourceType sourceType, String source, double chance,
                    int quantityMin, int quantityMax, boolean dropWithSilkTouch, LootingMode lootingMode,
                    FortuneMode fortuneMode, boolean replaceOriginalDrop
    ) {
        this.nexoId = nexoId;
        this.sourceType = sourceType;
        this.source = sourceType != SourceType.MYTHIC_MOB ? addDefaultNamespace(source) : source;
        this.chance = chance;
        this.quantityMin = quantityMin;
        this.quantityMax = quantityMax;
        this.dropWithSilkTouch = dropWithSilkTouch;
        this.lootingMode = lootingMode;
        this.fortuneMode = fortuneMode;
        this.replaceOriginalDrop = replaceOriginalDrop;
    }

    private String addDefaultNamespace(String key) {
        if (key == null) return null;
        return key.contains(":") ? key : "minecraft:" + key;
    }

    private static ConfigurationSection dropConfig;

    public static void initializeConfig(ConfigurationSection config) {
        dropConfig = config;
    }

    public static DropData fromConfig(String key) {
        String item = dropConfig.getString(key + ".item");
        SourceType sourceType = SourceType.valueOf(dropConfig.getString(key + ".type").toUpperCase());
        String source = dropConfig.getString(key + ".source");
        double chance = dropConfig.getDouble(key + ".chance");
        int quantityMin = dropConfig.getInt(key + ".quantityMin", 1);
        int quantityMax = dropConfig.getInt(key + ".quantityMax", 1);
        boolean dropWithSilkTouch = dropConfig.getBoolean(key + ".dropWithSilkTouch");

        Object lootingModeObj = dropConfig.get(key + ".lootingMode");
        String lootingModeStr = lootingModeObj != null ? lootingModeObj.toString() : "";
        if (lootingModeStr.isEmpty() || lootingModeStr.equalsIgnoreCase("false")) {

            Object defaultLootingModeObj = dropConfig.get("defaultsettings.lootingMode");
            lootingModeStr = (defaultLootingModeObj != null ? defaultLootingModeObj.toString() : "OFF");
        }
        LootingMode lootingMode = LootingMode.valueOf(lootingModeStr.toUpperCase());

        Object fortuneModeObj = dropConfig.get(key + ".fortuneMode");
        String fortuneModeStr = fortuneModeObj != null ? fortuneModeObj.toString() : "";
        if (fortuneModeStr.isEmpty() || fortuneModeStr.equalsIgnoreCase("false")) {
            Object defaultFortuneModeObj = dropConfig.get("defaultsettings.fortuneMode");
            fortuneModeStr = (defaultFortuneModeObj != null ? defaultFortuneModeObj.toString() : "OFF");
        }
        FortuneMode fortuneMode = FortuneMode.valueOf(fortuneModeStr.toUpperCase());

        boolean replaceOriginalDrop = dropConfig.getBoolean(key + ".replaceOriginalDrop");


        return new DropData(
                item,
                sourceType,
                source,
                chance,
                quantityMin,
                quantityMax,
                dropWithSilkTouch,
                lootingMode,
                fortuneMode,
                replaceOriginalDrop
        );
    }


    public enum LootingMode {
        OFF,
        COMMON,
        RARE
    }

    public enum FortuneMode {
        OFF,
        NORMAL
    }

}