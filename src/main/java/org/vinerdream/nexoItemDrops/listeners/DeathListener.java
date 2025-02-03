package org.vinerdream.nexoItemDrops.listeners;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.vinerdream.nexoItemDrops.DropData;
import org.vinerdream.nexoItemDrops.NexoItemDrops;
import org.vinerdream.nexoItemDrops.enums.SourceType;
import org.vinerdream.nexoItemDrops.mythic.DropCalculator;

import java.util.Optional;

public class DeathListener implements Listener {
    private final NexoItemDrops plugin;

    public DeathListener(NexoItemDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killerPlayer = event.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }

        for (DropData data : plugin.getDropList()) {
            final boolean entityMatches;
            if (data.sourceType() == SourceType.MOB) {

                entityMatches = event.getEntity().getType().getKey().toString().equals(data.source());
            } else if (data.sourceType() == SourceType.MYTHIC_MOB) {

                MythicBukkit mb = MythicBukkit.inst();
                Optional<ActiveMob> mythicMobOptional = mb.getMobManager().getActiveMob(event.getEntity().getUniqueId());
                entityMatches = mythicMobOptional.map(activeMob -> activeMob.getType().getInternalName().equals(data.source())).orElse(false);
            } else continue;

            double RandomChance = plugin.getRandom().nextDouble();

            String dropName = data.nexoId();
            if (data.nexoId() == null) return;

            if (entityMatches && DropData.isDropPresent(dropName) && DropCalculator.calculateLootingDropChance(RandomChance, getLootingLevel(killerPlayer), getLootingValue(dropName)) < data.chance()) {

                ItemBuilder builder = NexoItems.itemFromId(data.nexoId());
                if (builder == null) continue;

                ItemStack item = builder.build();
                item.setAmount(plugin.getRandom().nextInt(data.quantityMin(), data.quantityMax() + 1));

                if (data.replaceOriginalDrop()) {
                    event.getDrops().clear();
                }
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), item);
            }
        }
    }

    public static int getLootingLevel(Player player) {
        if (player == null) {
            return 0;
        }
        if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOTING)) {
            return player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING);
        }
        return 0;
    }

    public static DropData.LootingMode getLootingValue(String dropName) {
        if (DropData.fromConfig(dropName + ".lootingMode").lootingMode() == null) {
            return DropData.LootingMode.OFF;
        }
        return DropData.fromConfig(dropName + ".lootingMode").lootingMode();
    }

}
