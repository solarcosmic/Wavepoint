package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.WvConfig;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WvList {

    public void showList(Player player) {
        StringBuilder waypointString = new StringBuilder("You currently own the following waypoints: ");
        for (Map.Entry<UUID, Map<String, Waypoint>> entry : WvWaypoints.waypoints.entrySet()) {
            Map<String, Waypoint> waypoints = entry.getValue();
            UUID loopPlayerId = entry.getKey();
            if (loopPlayerId.equals(player.getUniqueId())) {
                for (Map.Entry<String, Waypoint> entry2 : waypoints.entrySet()) {
                    waypointString.append(" ").append(entry2.getKey()).append(" ");
                }
                player.sendMessage(waypointString.toString());
            }
        }
    }
}
