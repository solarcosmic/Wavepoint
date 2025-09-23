package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.WvConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WvConfigHandler {

    public void checkAddPlayer(Player player) {
        FileConfiguration config = WvConfig.get();
        ConfigurationSection section = config.getConfigurationSection("waypoints");
        String playerId = player.getUniqueId().toString();
        System.out.println(playerId);
        String foundPath = "waypoints." + playerId;
        System.out.println(foundPath);
        if (!(config.getStringList("waypoints").contains(playerId))) {
            config.set(foundPath, true);
            WvConfig.save();
            System.out.println("added section");
        }
    }
}
