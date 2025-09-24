package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.WvConfig;
import net.solarcosmic.wavepoint.integrations.WvInCombatLogX;
import net.solarcosmic.wavepoint.objects.Waypoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class WvWaypoints {
    public static HashMap<UUID, Map<String, Waypoint>> waypoints = new HashMap<>();
    public static FileConfiguration config = WvConfig.get();
    public static ConfigurationSection section = config.getConfigurationSection("waypoints");
    private static final BetterLogger logger = new BetterLogger("&bWavepoint&r");
    private static Wavepoint plugin = Wavepoint.getInstance();

    public static Waypoint create(Player player, Location loc, String name) {
        // maybe do something like strip /wp list then do new set?
        String newSet = name.toLowerCase().replaceAll(" ", "_");
        Waypoint waypoint = new Waypoint(player.getUniqueId(), loc, newSet);
        if (Wavepoint.hasCombatLogXIntegration) {
            if (WvInCombatLogX.isInCombat(player)) {
                if (!plugin.getConfig().getBoolean("integrations.combatlogx.combat.set")) {
                    player.sendMessage(ChatColor.RED + "You are currently in combat and cannot set a waypoint.");
                    return null;
                }
            }
        }
        Map<String, Waypoint> playerMap = waypoints.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        playerMap.put(newSet, waypoint);
        player.sendMessage("Waypoint created named '" + newSet + "' at: " + loc.x() + ", " + loc.y() + ", " + loc.z());
        return waypoint;
    }

    public static void delete(Waypoint waypoint) {
        Map<String, Waypoint> playerMap = waypoints.get(waypoint.getPlayerId());
        if (playerMap != null) {
            for (Map.Entry<String, Waypoint> entry2 : playerMap.entrySet()) {
                String wpName = entry2.getKey();
                if (wpName.equalsIgnoreCase(waypoint.getName())) {
                    playerMap.remove(wpName);
                }
            }
        }
    }

    public static void saveFromQueue() {
        waypoints.forEach((uuid, waypointList) -> {
            Map<String, Waypoint> playerMap = waypoints.get(uuid);
            for (Waypoint waypoint : playerMap.values()) {
                logger.debug(waypoint.getName());
                String base = "waypoints." + uuid + "." + waypoint.getName();
                Location loc = waypoint.getLocation();
                config.set(base + ".timestamp", waypoint.getTimestamp());
                config.set(base + ".world", loc.getWorld().getName());
                config.set(base + ".x", loc.x());
                config.set(base + ".y", loc.y());
                config.set(base + ".z", loc.z());
                config.set(base + ".pitch", loc.getPitch());
                config.set(base + ".yaw", loc.getYaw());
                logger.debug("Waypoint config set: " + waypoint.getName());
            }
        });
        WvConfig.save();
        logger.debug("Saved all waypoints!");
    }

    public static void loadAll() {
        if (section != null && !section.getKeys(false).isEmpty()) {
            for (String uid : section.getKeys(false)) {
                logger.log("Player id: " + uid);
                ConfigurationSection newSection = section.getConfigurationSection(uid);
                assert newSection != null;
                for (String wp : newSection.getKeys(false)) {
                    logger.log("uid: " + uid + " | item: " + wp);
                    ConfigurationSection wpSection = newSection.getConfigurationSection(wp);
                    assert wpSection != null;
                    String world = wpSection.getString("world");
                    assert world != null;
                    Location newLoc = new Location(
                            Bukkit.getServer().getWorld(world),
                            wpSection.getDouble("x"),
                            wpSection.getDouble("y"),
                            wpSection.getDouble("z"),
                            Float.parseFloat(Objects.requireNonNull(wpSection.getString("pitch"))),
                            Float.parseFloat(Objects.requireNonNull(wpSection.getString("yaw")))
                    );
                    UUID uuid = UUID.fromString(uid);
                    Waypoint waypoint = new Waypoint(uuid, newLoc, wp);
                    waypoints.computeIfAbsent(uuid, k -> new HashMap<>());
                    waypoints.get(uuid).put(waypoint.getName(), waypoint);
                    logger.debug("Loading waypoint success: " + waypoint.getName());
                }
            }
        }
    }

    public static Waypoint getGlobalWaypoint(String name) {
        for (Map.Entry<UUID, Map<String, Waypoint>> entry : waypoints.entrySet()) {
            Map<String, Waypoint> playerMap = waypoints.get(entry.getKey());
            for (Map.Entry<String, Waypoint> entry2 : playerMap.entrySet()) {
                String wpName = entry2.getKey();
                Waypoint waypoint = entry2.getValue();
                if (wpName.equalsIgnoreCase(name)) {
                    return waypoint;
                }
            }
        }
        return null;
    }

    public static Waypoint getWaypoint(UUID uuid, String name) {
        Map<String, Waypoint> playerMap = waypoints.get(uuid);
        if (playerMap == null) return null;
        return playerMap.get(name.toLowerCase());
    }

    public static ArrayList<String> buildList(UUID uuid) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<UUID, Map<String, Waypoint>> entry : WvWaypoints.waypoints.entrySet()) {
            Map<String, Waypoint> waypoints = entry.getValue();
            UUID loopPlayerId = entry.getKey();
            if (loopPlayerId.equals(uuid)) {
                for (Map.Entry<String, Waypoint> entry2 : waypoints.entrySet()) {
                    list.add(entry2.getValue().getName());
                }
            }
        }
        return list;
    }
}
