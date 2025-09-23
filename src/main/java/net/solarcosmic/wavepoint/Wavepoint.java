package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.commands.WaypointCommand;
import net.solarcosmic.wavepoint.modules.WvConfigHandler;
import net.solarcosmic.wavepoint.modules.WvWaypoints;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Wavepoint extends JavaPlugin implements Listener {
    private static Wavepoint instance;
    private final BetterLogger logger = new BetterLogger("&b[Wavepoint]&r");
    public static HashMap<String, String> storePlayers = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger.log("Wavepoint is up!");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        WvConfig.create();
        WvConfig.get().options().copyDefaults(true);
        WvConfig.save();
        getCommand("wp").setExecutor(new WaypointCommand());
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().info("Checking for players");
        new WvConfigHandler().checkAddPlayer(event.getPlayer());
    }

    @Override
    public void onDisable() {
        WvWaypoints.saveFromQueue();
        logger.log("Wavepoint has disabled!");
    }

    public static Wavepoint getInstance() {
        return instance;
    }
}
