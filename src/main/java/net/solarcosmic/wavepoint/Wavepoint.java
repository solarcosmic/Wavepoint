package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wavepoint extends JavaPlugin {
    private static Wavepoint instance;
    private final BetterLogger logger = new BetterLogger("&b[Wavepoint]&r");
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger.log("Wavepoint is up!");
        this.saveDefaultConfig();
        new WvConfig().loadData();
    }

    @Override
    public void onDisable() {
        logger.log("Wavepoint is disabling!");
    }

    public static Wavepoint getInstance() {
        return instance;
    }
}
