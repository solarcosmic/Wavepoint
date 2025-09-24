package net.solarcosmic.wavepoint.modules;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.integrations.WvInCombatLogX;
import net.solarcosmic.wavepoint.integrations.WvInVault;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class WvTeleport {
    public static ArrayList<UUID> currentlyTeleporting = new ArrayList<>();
    private static Wavepoint plugin = Wavepoint.getInstance();

    /*
    Teleports a player to a waypoint because why not?
     */
    public static void teleport(Player player, Waypoint waypoint) {
        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), true);
        BukkitTask task = new BukkitRunnable() {
            int count = 5;
            public void run() {
                if (Wavepoint.hasCombatLogXIntegration) {
                    if (WvInCombatLogX.isInCombat(player)) {
                        if (!plugin.getConfig().getBoolean("integrations.combatlogx.combat.teleport")) {
                            player.sendMessage(ChatColor.RED + "You are currently in combat! Teleport cancelled.");
                            this.cancel();
                            return;
                        }
                    }
                }
                if (Wavepoint.hasVaultIntegration) {
                    if (!WvTeleport.isCurrentlyTeleporting(player.getUniqueId())) {
                        this.cancel();
                        return;
                    }
                    if (!WvInVault.hasRequiredCurrency(player)) {
                        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
                        this.cancel();
                        return;
                    }
                }
                if (count == 1) {
                    player.sendActionBar("Please wait 1 more second!");
                } else {
                    player.sendActionBar("Please wait " + count + " more seconds!");
                }
                if (count < 1) {
                    if (Wavepoint.hasVaultIntegration) {
                        Economy economy = WvInVault.getEconomy();
                        double charge_amount = plugin.getConfig().getDouble("integrations.vault.charge_amount");
                        EconomyResponse response = economy.withdrawPlayer(player, charge_amount);
                        if (response.transactionSuccess()) {
                            player.sendActionBar("Teleported! -$" + response.amount + " has been taken from your account.");
                            player.teleport(waypoint.getLocation());
                            WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
                            this.cancel();
                        } else {
                            player.sendMessage(ChatColor.RED + "Transaction failed - you have not been teleported and your account has not been charged: " + response.errorMessage);
                            this.cancel();
                        }
                    } else {
                        player.sendActionBar("Teleported!");
                        player.teleport(waypoint.getLocation());
                        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
                        this.cancel();
                    }
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
