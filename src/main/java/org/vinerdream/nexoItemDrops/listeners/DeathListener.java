package org.vinerdream.nexoItemDrops.listeners;

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
import org.vinerdream.nexoItemDrops.mythic.DropHandler;

import java.util.Optional;

public class DeathListener implements Listener {
    private final NexoItemDrops plugin;
    private final DropHandler dropHandler;

    public DeathListener(NexoItemDrops plugin, DropHandler dropHandler) {
        this.plugin = plugin;
        this.dropHandler = dropHandler;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killerPlayer = event.getEntity().getKiller();
        if (killerPlayer == null) return;

        for (DropData dropData : plugin.getDropList()) {
            boolean matches = false;

            if (dropData.sourceType() == SourceType.MOB) {
                String mobKey = event.getEntity().getType().getKey().toString();
                matches = mobKey.equalsIgnoreCase(dropData.source());
            } else if (dropData.sourceType() == SourceType.MYTHIC_MOB) {
                MythicBukkit mb = MythicBukkit.inst();
                Optional<ActiveMob> activeMobOpt = mb.getMobManager().getActiveMob(event.getEntity().getUniqueId());
                if (activeMobOpt.isPresent()) {
                    String internalName = activeMobOpt.get().getType().getInternalName();
                    matches = internalName.equalsIgnoreCase(dropData.source());
                }
            }

            if (!matches) continue;

            double baseChance = dropData.chance();

            int lootingLevel = getLootingLevel(killerPlayer);

            if (dropData.replaceOriginalDrop()) {
                event.getDrops().clear();
            }

            dropHandler.processDrop(event.getEntity().getLocation(), dropData, baseChance, lootingLevel, true);
        }
    }

    public static int getLootingLevel(Player player) {
        if (player == null) return 0;
        ItemStack tool = player.getInventory().getItemInMainHand();
        return tool.containsEnchantment(Enchantment.LOOTING) ? tool.getEnchantmentLevel(Enchantment.LOOTING) : 0;
    }

}