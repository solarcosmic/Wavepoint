package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.WvConfig;
import net.solarcosmic.wavepoint.objects.Waypoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WvWaypoints {
    public static HashMap<UUID, List<Waypoint>> waypoints = new HashMap<>();
    public static FileConfiguration config = WvConfig.get();
    public static ConfigurationSection section = config.getConfigurationSection("waypoints");
    private static final BetterLogger logger = new BetterLogger("&b[Wavepoint]&r");

    public static void create(Player player, String name) {
        // maybe do something like strip /wp list then do new set?
        String newSet = name.toLowerCase().replaceAll(" ", "_");
        Location loc = player.getLocation();
        Waypoint waypoint = new Waypoint(player.getUniqueId(), loc, newSet);
        waypoints.computeIfAbsent(player.getUniqueId(), x -> new ArrayList<>()).add(waypoint);
        player.sendMessage("Waypoint created named '" + newSet + "' at: " + loc.x() + ", " + loc.y() + ", " + loc.z());
    }

    public static void saveFromQueue() {
        waypoints.forEach((uuid, waypointList) -> {
            for (Waypoint waypoint : waypointList) {
                System.out.println(waypoint.getName());
                String base = "waypoints." + uuid + "." + waypoint.getName();
                Location loc = waypoint.getLocation();
                config.set(base + ".timestamp", waypoint.getTimestamp());
                config.set(base + ".world", loc.getWorld().getName());
                config.set(base + ".x", loc.x());
                config.set(base + ".y", loc.y());
                config.set(base + ".z", loc.z());
                config.set(base + ".pitch", loc.getPitch());
                config.set(base + ".yaw", loc.getYaw());
                System.out.println("Waypoint config set: " + waypoint.getName());
            }
        });
        WvConfig.save();
        System.out.println("Saved all waypoints!");
    }

    public static void loadAll() {
        assert section != null;
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
                        (float) wpSection.getDouble("pitch"),
                        (float) wpSection.getDouble("yaw")
                );
                UUID uuid = UUID.fromString(uid);
                Waypoint waypoint = new Waypoint(uuid, newLoc, wp);
                waypoints.computeIfAbsent(uuid, k -> new ArrayList<>());
                waypoints.get(uuid).add(waypoint);
                System.out.println("Loading waypoint success: " + waypoint.getName());
            }
        }
    }
}
