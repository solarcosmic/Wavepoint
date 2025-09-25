package net.solarcosmic.wavepoint;

import com.tchristofferson.configupdater.ConfigUpdater;
import net.solarcosmic.wavepoint.commands.WaypointCommand;
import net.solarcosmic.wavepoint.integrations.WvInCombatLogX;
import net.solarcosmic.wavepoint.integrations.WvInVault;
import net.solarcosmic.wavepoint.modules.WvLanguage;
import net.solarcosmic.wavepoint.modules.WvTeleport;
import net.solarcosmic.wavepoint.modules.WvWaypoints;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public final class Wavepoint extends JavaPlugin implements Listener {
    private static Wavepoint instance;
    private final BetterLogger logger = new BetterLogger("Wavepoint");
    public static HashMap<String, String> storePlayers = new HashMap<>();
    public static boolean isDebugEnabled = false;
    public static boolean hasVaultIntegration = false;
    public static boolean hasCombatLogXIntegration = false;
    public static boolean isSoundOn = false;
    public static String prefix = "";

    @Override
    public void onEnable() {
        long time_start = System.currentTimeMillis();
        instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        saveResource("languages/en_us.yml", false);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
            logger.debug("Updated latest configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new WvLanguage(getConfig().getString("language", "en_us"));
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
        WvConfig.create();
        WvConfig.get().options().copyDefaults(true);
        WvConfig.save();
        getCommand("wp").setExecutor(new WaypointCommand());
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        logger.debug("Retrieving from disk...");
        WvWaypoints.loadAll();
        isDebugEnabled = getConfig().getBoolean("debug");
        if (getConfig().getBoolean("integrations.vault.enabled", true)) {
            new WvInVault();
        }
        if (getConfig().getBoolean("integrations.combatlogx.enabled", true)) {
            new WvInCombatLogX();
        }
        if (getConfig().getBoolean("experiments.sounds", true)) {
            isSoundOn = getConfig().getBoolean("experiments.sounds");
        }
        logger.log(WvLanguage.lang("wavepoint.wavepoint_ready") + " (" + (System.currentTimeMillis() - time_start) + "ms)");
    }

    /*@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getLogger().info("Checking for players");
        //new WvConfigHandler().checkAddPlayer(event.getPlayer());
    }*/

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) { // https://bukkit.org/threads/checking-if-player-moved-entire-block.238567/#post-2292237
        UUID uuid = e.getPlayer().getUniqueId();
        if (!WvTeleport.isCurrentlyTeleporting(uuid)) return;
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
            WvTeleport.setCurrentlyTeleporting(uuid, false);
            e.getPlayer().sendActionBar("You moved!");
        }
    }

    @Override
    public void onDisable() {
        logger.log(WvLanguage.lang("wavepoint.write_to_disk"));
        WvWaypoints.saveFromQueue();
        logger.log(WvLanguage.lang("wavepoint.shutdown_complete"));
    }

    public static Wavepoint getInstance() {
        return instance;
    }
}
