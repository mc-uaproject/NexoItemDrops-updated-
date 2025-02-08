package org.vinerdream.nexoItemDrops.mythic;

import org.vinerdream.nexoItemDrops.DropData;

public class DropCalculator {
    public static double applyLootingModifier(double baseChance, int lootingLevel, DropData.LootingMode lootingMode) {
        if (lootingMode == DropData.LootingMode.OFF) {
            return baseChance;
        }
        double modifier = switch (lootingMode) {
            case COMMON -> lootingLevel * 0.01;
            case RARE -> lootingLevel * 0.02;
            default -> 0;
        };
        return Math.min(baseChance + modifier, 1.0);
    }

    public static double applyFortuneModifier(double baseChance, int fortuneLevel, DropData.FortuneMode fortuneMode) {
        if (fortuneMode == DropData.FortuneMode.OFF) {
            return baseChance;
        }
        double modifier = fortuneLevel * 0.05;
        return Math.min(baseChance + modifier, 1.0);
    }
}