package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.WvConfig;
import net.solarcosmic.wavepoint.api.WvGeneralAPI;
import net.solarcosmic.wavepoint.integrations.WvInCombatLogX;
import net.solarcosmic.wavepoint.objects.Waypoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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
        if (!player.hasPermission("waypoint.wp.create")) {
            player.sendMessage("&cYou do not have permission to execute this!");
            return null;
        }
        int wpAmount = new WvGeneralAPI().getWaypointAmount(player.getUniqueId());
        int cap = plugin.getConfig().getInt("max_cap", 5);
        if (Integer.signum(cap) == 1 && wpAmount >= cap) { // if positive and over cap limit
            player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.max_waypoints_limit").replace("${limit}", String.valueOf(cap)));
            return null;
        }
        // maybe do something like strip /wp list then do new set?
        int limit = plugin.getConfig().getInt("max_characters", 10);
        String newSet = name.toLowerCase().replaceAll(" ", "_");
        if (name.length() > limit) {
            newSet = newSet.substring(0, limit);
            player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.max_characters_limit").replace("${limit}", String.valueOf(limit)));
        }
        Waypoint waypoint = new Waypoint(player.getUniqueId(), loc, newSet);
        if (Wavepoint.hasCombatLogXIntegration) {
            if (WvInCombatLogX.isInCombat(player)) {
                if (!plugin.getConfig().getBoolean("integrations.combatlogx.combat.set")) {
                    player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.integrations.combatlogx.cannot_set_waypoint"));
                    return null;
                }
            }
        }
        Map<String, Waypoint> playerMap = waypoints.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        playerMap.put(newSet, waypoint);
        player.sendMessage(Wavepoint.prefix + "Waypoint created named '" + newSet + "' at: " + Math.floor(loc.getX()) + ", " + Math.floor(loc.getY()) + ", " + Math.floor(loc.getZ()));
        try {
            for (String item : plugin.getConfig().getStringList("commands.set")) {
                // index 0 out of bounds for length 0?
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
            }
        } catch (Exception ignored) {}
        if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        return waypoint;
    }

    public static void delete(Waypoint waypoint) {
        Player player = Bukkit.getPlayer(waypoint.getPlayerId());
        assert player != null;
        if (!player.hasPermission("waypoint.wp.delete")) {
            player.sendMessage("&cYou do not have permission to execute this!");
            return;
        }
        Map<String, Waypoint> playerMap = waypoints.get(waypoint.getPlayerId());
        if (playerMap != null) {
            playerMap.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase(waypoint.getName()));
            try {
                for (String item : plugin.getConfig().getStringList("commands.delete")) {
                    // index 0 out of bounds for length 0?
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
                }
                if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            } catch (Exception ignored) {}
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
                config.set(base + ".x", loc.getX());
                config.set(base + ".y", loc.getY());
                config.set(base + ".z", loc.getZ());
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
                ConfigurationSection newSection = section.getConfigurationSection(uid);
                assert newSection != null;
                for (String wp : newSection.getKeys(false)) {
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
