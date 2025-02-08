package org.vinerdream.nexoItemDrops.mythic;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.vinerdream.nexoItemDrops.DropData;
import org.vinerdream.nexoItemDrops.NexoItemDrops;

public class DropHandler {
    private final NexoItemDrops plugin;

    public DropHandler(NexoItemDrops plugin) {
        this.plugin = plugin;
    }

    public void processDrop(Location location, DropData dropData, double baseChance, int enchantLevel, boolean isMob) {
        double effectiveChance;
        if (isMob) {
            effectiveChance = DropCalculator.applyLootingModifier(baseChance, enchantLevel, dropData.lootingMode());
        } else {
            effectiveChance = DropCalculator.applyFortuneModifier(baseChance, enchantLevel, dropData.fortuneMode());
        }
        double random = plugin.getRandom().nextDouble();
        if (random < effectiveChance) {
            ItemBuilder builder = NexoItems.itemFromId(dropData.nexoId());
            if (builder == null) return;

            int amount = plugin.getRandom().nextInt(dropData.quantityMin(), dropData.quantityMax() + 1);
            ItemStack item = builder.build();
            item.setAmount(amount);
            location.getWorld().dropItem(location, item);
        }
    }

}
