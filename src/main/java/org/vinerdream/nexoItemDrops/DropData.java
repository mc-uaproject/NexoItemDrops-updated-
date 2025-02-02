package org.vinerdream.nexoItemDrops;

import org.bukkit.configuration.ConfigurationSection;
import org.vinerdream.nexoItemDrops.enums.SourceType;

import java.util.Objects;


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
        return new DropData(
                dropConfig.getString(key + ".item"),
                SourceType.valueOf(dropConfig.getString(key + ".type", "mob").toUpperCase()),
                dropConfig.getString(key + ".source"),
                dropConfig.getDouble(key + ".chance"),
                dropConfig.getInt(key + ".quantityMin", 1),
                dropConfig.getInt(key + ".quantityMax", 1),
                dropConfig.getBoolean(key + ".dropWithSilkTouch"),
                LootingMode.valueOf(Objects.requireNonNull(dropConfig.getString(key + ".lootingMode")).toUpperCase()),
                FortuneMode.valueOf(Objects.requireNonNull(dropConfig.getString(key + ".fortuneMode")).toUpperCase()),
                dropConfig.getBoolean(key + ".replaceOriginalDrop")
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