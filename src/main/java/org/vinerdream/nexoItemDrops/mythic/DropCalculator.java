package org.vinerdream.nexoItemDrops.mythic;

import org.vinerdream.nexoItemDrops.DropData;

public class DropCalculator {
    public static double calculateLootingDropChance(double baseChance, int lootingLevel, DropData.LootingMode lootingMode) {
        if (lootingMode == DropData.LootingMode.OFF) {
            return baseChance;
        }

        double additionalChance = lootingLevel * 0.01;
        double modifiedChance = baseChance + additionalChance;
        return Math.min(modifiedChance, 1.0);
    }

    public static double calculateFortuneDropChance(double baseChance, int fortuneLevel, DropData.FortuneMode fortuneMode) {
        if (fortuneMode == DropData.FortuneMode.OFF) {
            return baseChance;
        }

        double secondAttemptChance = fortuneLevel / (double) (fortuneLevel + 1);


        double additionalChance = 1.0 / (fortuneLevel + 1);

        double modifiedChance = baseChance + secondAttemptChance * additionalChance;
        return Math.min(modifiedChance, 1.0);
    }
}