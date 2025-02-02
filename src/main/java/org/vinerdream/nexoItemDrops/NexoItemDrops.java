package org.vinerdream.nexoItemDrops;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.vinerdream.nexoItemDrops.listeners.BlockBreakListener;
import org.vinerdream.nexoItemDrops.listeners.DeathListener;
import org.vinerdream.nexoItemDrops.mythic.DisplayCraft;
import org.vinerdream.nexoItemDrops.mythic.RedSackCraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public final class NexoItemDrops extends JavaPlugin {

    private MythicBukkit mythicBukkit;
    private final List<DropData> dropList = new ArrayList<>();
    private final Random random = new Random();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        this.mythicBukkit = MythicBukkit.inst();

        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);

        new DisplayCraft(this);
        new RedSackCraft(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("NexoItemDrops has been disabled");
    }

    private void loadConfig() {
        dropList.clear();
        ConfigurationSection drops = getConfig().getConfigurationSection("drops");
        if (drops == null) {
            getLogger().warning("ConfigurationSection drops is null, unable to load config");
            return;
        }
        DropData.initializeConfig(drops);
        for (String key : drops.getKeys(false)) {
            dropList.add(DropData.fromConfig(key));
        }
    }
}
