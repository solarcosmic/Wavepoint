package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WvWaypoints {
    public static HashMap<UUID, List<Waypoint>> waypointQueue = new HashMap<>();

    public static void create(Player player, String name) {
        // maybe do something like strip /wp list then do new set?
        String newSet = name.toLowerCase().replaceAll(" ", "_");
        Location loc = player.getLocation();
        Waypoint waypoint = new Waypoint(player.getUniqueId(), loc, newSet);
        waypointQueue.computeIfAbsent(player.getUniqueId(), x -> new ArrayList<>()).add(waypoint);
        player.sendMessage("Waypoint created named '" + newSet + "' at: " + loc.x() + ", " + loc.y() + ", " + loc.z());
    }
}
