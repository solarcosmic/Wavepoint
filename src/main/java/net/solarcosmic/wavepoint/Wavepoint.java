package net.solarcosmic.wavepoint;

import net.solarcosmic.wavepoint.commands.WaypointCommand;
import net.solarcosmic.wavepoint.modules.WvTeleport;
import net.solarcosmic.wavepoint.modules.WvWaypoints;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Wavepoint extends JavaPlugin implements Listener {
    private static Wavepoint instance;
    private final BetterLogger logger = new BetterLogger("Wavepoint");
    public static HashMap<String, String> storePlayers = new HashMap<>();
    public static boolean isDebugEnabled = false;
    @Override
    public void onEnable() {
        long time_start = System.currentTimeMillis();
        instance = this;
        logger.log("Wavepoint is up!");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        WvConfig.create();
        WvConfig.get().options().copyDefaults(true);
        WvConfig.save();
        getCommand("wp").setExecutor(new WaypointCommand());
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        logger.debug("Retrieving from disk...");
        WvWaypoints.loadAll();
        isDebugEnabled = getConfig().getBoolean("debug");
        logger.log("Wavepoint ready! (" + (System.currentTimeMillis() - time_start) + "ms)");
    }

    /*@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().info("Checking for players");
        //new WvConfigHandler().checkAddPlayer(event.getPlayer());
    }*/

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) { // https://www.spigotmc.org/threads/check-if-the-player-has-moved.442917/#post-3829391
        UUID uuid = e.getPlayer().getUniqueId();
        if (!WvTeleport.isCurrentlyTeleporting(uuid)) return;
        if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
            WvTeleport.setCurrentlyTeleporting(uuid, false);
            e.getPlayer().sendActionBar("You moved!");
        }
    }

    @Override
    public void onDisable() {
        logger.log("Wavepoint will now begin writing to disk, please do not force kill the server as this server may lose waypoints!");
        WvWaypoints.saveFromQueue();
        logger.log("Wavepoint has finished its shutdown process!");
    }

    public static Wavepoint getInstance() {
        return instance;
    }
}
