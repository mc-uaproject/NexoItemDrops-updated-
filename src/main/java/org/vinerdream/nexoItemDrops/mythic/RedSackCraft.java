package org.vinerdream.nexoItemDrops.mythic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.vinerdream.nexoItemDrops.NexoItemDrops;

public class RedSackCraft {

    public RedSackCraft(NexoItemDrops plugin) {
        plugin.getLogger().warning("Trying to create bugnet_red recipe");
        ItemStack item = plugin.getMythicBukkit().getItemManager().getItemStack("bugnet_red");
        if (item != null) {
            NamespacedKey key = new NamespacedKey(plugin, "red_sack");
            ShapedRecipe recipe = new ShapedRecipe(key, item);
            recipe.shape(
                    " W ",
                    " S ",
                    " S ");
            recipe.setIngredient('W', Material.RED_WOOL)
                    .setIngredient('S', Material.STICK);

            Bukkit.addRecipe(recipe);
        } else {
            plugin.getLogger().warning("Item not found: " + "bugnet_red");
        }
    }

}
