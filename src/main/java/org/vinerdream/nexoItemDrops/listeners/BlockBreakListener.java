package org.vinerdream.nexoItemDrops.listeners;

import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.vinerdream.nexoItemDrops.DropData;
import org.vinerdream.nexoItemDrops.NexoItemDrops;
import org.vinerdream.nexoItemDrops.enums.SourceType;
import org.vinerdream.nexoItemDrops.mythic.DropHandler;

public class BlockBreakListener implements Listener {
    private final NexoItemDrops plugin;
    private final DropHandler dropHandler;

    public BlockBreakListener(NexoItemDrops plugin) {
        this.plugin = plugin;
        this.dropHandler = new DropHandler(plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        for (DropData dropData : plugin.getDropList()) {
            if (dropData.sourceType() != SourceType.BLOCK) continue;

            if (!event.getBlock().getType().getKey().toString().equalsIgnoreCase(dropData.source())) continue;

            if (!dropData.dropWithSilkTouch()) {
                ItemStack tool = player.getInventory().getItemInMainHand();
                if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) continue;
            }

            double baseChance = dropData.chance();
            int fortuneLevel = getFortuneLevel(player);

            if (dropData.replaceOriginalDrop()) {
                event.setDropItems(false);
            }

            dropHandler.processDrop(event.getBlock().getLocation().add(0.5, 0.5, 0.5), dropData, baseChance, fortuneLevel, false);
        }
    }

    public static int getFortuneLevel(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        return tool.containsEnchantment(Enchantment.FORTUNE) ? tool.getEnchantmentLevel(Enchantment.FORTUNE) : 0;
    }
}
