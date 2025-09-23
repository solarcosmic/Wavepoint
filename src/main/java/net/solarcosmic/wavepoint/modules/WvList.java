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
        for (Map.Entry<UUID, List<Waypoint>> entry : WvWaypoints.waypoints.entrySet()) {
            UUID loopPlayerId = entry.getKey();
            if (loopPlayerId.equals(player.getUniqueId())) {
                List<Waypoint> waypoints = entry.getValue();
                StringBuilder waypointString = new StringBuilder("You currently own the following waypoints:");
                for (Waypoint wp : waypoints) {
                    System.out.println(wp);
                    waypointString.append(" ").append(wp.getName()).append(" ");
                };
                player.sendMessage(waypointString.toString());
            }
        }
    }
}
