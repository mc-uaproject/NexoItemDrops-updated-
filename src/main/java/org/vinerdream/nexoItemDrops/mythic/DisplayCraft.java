package org.vinerdream.nexoItemDrops.mythic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.vinerdream.nexoItemDrops.NexoItemDrops;

public class DisplayCraft {

    public DisplayCraft(NexoItemDrops plugin) {
        plugin.getLogger().warning("Trying to create display_empty recipe");
        ItemStack item = plugin.getMythicBukkit().getItemManager().getItemStack("display_empty");
        if (item != null) {
            NamespacedKey key = new NamespacedKey(plugin, "display_empty");
            ShapedRecipe recipe = new ShapedRecipe(key, item);
            recipe.shape(
                    "GDG",
                    "G G",
                    "GGG");
            recipe.setIngredient('G', Material.GLASS)
                    .setIngredient('D', Material.IRON_TRAPDOOR);

            Bukkit.addRecipe(recipe);
        } else {
            plugin.getLogger().warning("Item not bukkit item stack: " + "display_empty");
        }
    }

}
