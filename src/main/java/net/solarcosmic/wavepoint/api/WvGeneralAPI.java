package net.solarcosmic.wavepoint.api;

import net.solarcosmic.wavepoint.modules.WvWaypoints;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * WvGeneralAPI is a general API for Wavepoint.
 */
public class WvGeneralAPI {
    public WvGeneralAPI() {}

    /**
     * Creates a waypoint that will be stored in the Waypoint queue.
     *
     * @param player An online player who you want to create the waypoint for.
     * @param location The location of the waypoint that you want to set.
     * @param waypointName The name of the waypoint that you want to set.
     * @return A waypoint object.
     */
    public Waypoint createWaypoint(Player player, Location location, String waypointName) {
        return WvWaypoints.create(player, location, waypointName);
    }

    /**
     * Deletes an already existing waypoint and inserts the deletion request into the Waypoint queue.
     *
     * @param waypoint The waypoint object that you want to delete.
     * @apiNote Waypoints will be removed as soon as this function is called, but it will not be saved until the
     * server stops and the queue is written to `waypoints.yml`.
     */
    public void deleteWaypoint(Waypoint waypoint) {
        WvWaypoints.delete(waypoint);
    }

    /**
     * Retrieves a waypoint from the Waypoint queue as a Waypoint object.
     *
     * @param uuid The UUID of the player in question.
     * @param waypointName The name of the waypoint in which you want to retrieve.
     */
    public Waypoint getWaypoint(UUID uuid, String waypointName) {
        return WvWaypoints.getWaypoint(uuid, waypointName);
    }

    /**
     * Retrieves the list of waypoints of a specific player as an ArrayList.
     *
     * @param player The player as an OfflinePlayer object.
     */
    public ArrayList<String> getWaypointList(OfflinePlayer player) {
        return WvWaypoints.buildList(player.getUniqueId());
    }

    /**
     * Retrieves the list of waypoints of a specific player as an ArrayList.
     *
     * @param playerId The player's unique UUID.
     */
    public ArrayList<String> getWaypointList(UUID playerId) {
        return WvWaypoints.buildList(playerId);
    }
}
