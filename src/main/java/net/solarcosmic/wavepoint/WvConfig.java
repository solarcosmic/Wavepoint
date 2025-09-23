package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WvConfig {
    private FileConfiguration customFile;
    private File configFile;
    private final Wavepoint plugin = Wavepoint.getInstance();
    private final BetterLogger logger = new BetterLogger("&b[Wavepoint | Configuration]&r");

    public void create() {
        configFile = new File(plugin.getDataFolder(), "waypoints.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration get() {
        return customFile;
    }

    public void save() {
        try {
            customFile.save(configFile);
        } catch (IOException e) {
            logger.log("Failed to save waypoints.yml - custom config file!");
        }
    }

    public void reload() {
        customFile = YamlConfiguration.loadConfiguration(configFile);
    }
}
