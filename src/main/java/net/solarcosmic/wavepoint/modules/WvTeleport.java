package net.solarcosmic.wavepoint.modules;

import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class WvTeleport {
    public static ArrayList<UUID> currentlyTeleporting = new ArrayList<>();

    /*
    Teleports a player to a waypoint because why not?
     */
    public static void teleport(Player player, Waypoint waypoint) {
        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), true);
        BukkitTask task = new BukkitRunnable() {
            int count = 5;
            public void run() {
                if (!WvTeleport.isCurrentlyTeleporting(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (count == 1) {
                    player.sendActionBar("Please wait 1 more second!");
                } else {
                    player.sendActionBar("Please wait " + count + " more seconds!");
                }
                if (count < 1) {
                    player.sendActionBar("Teleported!");
                    player.teleport(waypoint.getLocation());
                    WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
                    this.cancel();
                }
                count -= 1;
            }
        }.runTaskTimer(Wavepoint.getInstance(), 0L, 20L);
    }

    public static boolean isCurrentlyTeleporting(UUID uuid) {
        return currentlyTeleporting.contains(uuid);
    }

    public static void setCurrentlyTeleporting(UUID uuid, boolean decision) {
        if (decision) { currentlyTeleporting.add(uuid); }
        else { currentlyTeleporting.remove(uuid); }
    }
}
