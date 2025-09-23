package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class WvTeleport {
    public static ArrayList<UUID> currentlyTeleporting = new ArrayList<>();

    /*
    Teleports a player to a waypoint because why not?
     */
    public static void teleport(Player player, Waypoint waypoint) {
        player.teleport(waypoint.getLocation());
    }

    public static boolean isCurrentlyTeleporting(UUID uuid) {
        return currentlyTeleporting.contains(uuid);
    }
}
