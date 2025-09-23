package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.commands.WaypointCommand;
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
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        WvConfig waypointFile = new WvConfig();
        waypointFile.create();
        waypointFile.get().options().copyDefaults(true);
        waypointFile.save();
        getCommand("wp").setExecutor(new WaypointCommand());
    }

    @Override
    public void onDisable() {
        logger.log("Wavepoint is disabling!");
    }

    public static Wavepoint getInstance() {
        return instance;
    }
}
