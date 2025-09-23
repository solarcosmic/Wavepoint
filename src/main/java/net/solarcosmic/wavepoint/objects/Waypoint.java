package net.solarcosmic.wavepoint.objects;

import org.bukkit.Location;

import java.util.UUID;

public class Waypoint {
    private UUID playerId;
    private Location location;
    private String name;

    public Waypoint(UUID playerId, Location location, String name) {
        this.playerId = playerId;
        this.location = location;
        this.name = name;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
