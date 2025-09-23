package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WvConfig {
    private static FileConfiguration customFile;
    private static File configFile;
    private static final Wavepoint plugin = Wavepoint.getInstance();
    private static final BetterLogger logger = new BetterLogger("&b[Wavepoint | Configuration]&r");

    public static void create() {
        configFile = new File(plugin.getDataFolder(), "waypoints.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(configFile);
        System.out.println("Process finished: " + customFile);
    }

    public static FileConfiguration get() {
        System.out.println("Fetching: " + customFile);
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(configFile);
        } catch (IOException e) {
            logger.log("Failed to save waypoints.yml - custom config file!");
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(configFile);
    }
}
