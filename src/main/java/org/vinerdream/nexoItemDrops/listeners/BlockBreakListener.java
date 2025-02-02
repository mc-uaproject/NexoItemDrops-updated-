package org.vinerdream.nexoItemDrops.listeners;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
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
import org.vinerdream.nexoItemDrops.mythic.DropCalculator;

public class BlockBreakListener implements Listener {
    private final NexoItemDrops plugin;

    public BlockBreakListener(NexoItemDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        for (DropData data : plugin.getDropList()) {
            if (data.sourceType() != SourceType.BLOCK) continue;

            double baseChance = plugin.getRandom().nextDouble();
            Player player = event.getPlayer();

            if (event.getBlock().getType().getKey().toString().equals(data.source()) &&
                    baseChance + (DropCalculator.calculateFortuneDropChance(baseChance, getFortuneLevel(player), getFortuneValue())) < data.chance()
            ) {
                if (!data.dropWithSilkTouch()
                        && event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) != 0
                ) continue;
                ItemBuilder builder = NexoItems.itemFromId(data.nexoId());
                if (builder == null) continue;
                if (data.replaceOriginalDrop()) {
                    event.setDropItems(false);
                }
                ItemStack item = builder.build();
                item.setAmount(plugin.getRandom().nextInt(data.quantityMin(), data.quantityMax() + 1));
                event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5, 0.5, 0.5), item);
            }
        }
    }

    public static int getFortuneLevel(Player player) {
        if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.FORTUNE)) {
            return player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FORTUNE);
        }
        return 0;
    }

    public static DropData.FortuneMode getFortuneValue() {
        return DropData.fromConfig("globalSettings.fortuneMode").fortuneMode();
    }

}
