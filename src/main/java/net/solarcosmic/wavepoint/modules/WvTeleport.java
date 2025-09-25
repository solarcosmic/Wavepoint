package net.solarcosmic.wavepoint.modules;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.integrations.WvInCombatLogX;
import net.solarcosmic.wavepoint.integrations.WvInVault;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Objects;
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
                        if (!plugin.getConfig().getBoolean("integrations.combatlogx.combat.teleport", true)) {
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
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0f, 1.0f);
                    sendTeleportMessage(player, "Please wait 1 more second!");
                } else {
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    sendTeleportMessage(player, "Please wait " + count + " more seconds!");
                }
                if (count < 1) {
                    if (Wavepoint.hasVaultIntegration) {
                        Economy economy = WvInVault.getEconomy();
                        double charge_amount = plugin.getConfig().getDouble("integrations.vault.charge_amount", 5.0);
                        EconomyResponse response = economy.withdrawPlayer(player, charge_amount);
                        if (response.transactionSuccess()) { // response.amount
                            sendTeleportMessage(player, WvLanguage.lang("wavepoint.teleport_complete").replace("${amount}", String.valueOf(response.amount)) + " " + WvLanguage.lang("wavepoint.teleport_complete_vault").replace("${amount}", String.valueOf(response.amount)));
                            teleportPhase(player, waypoint);
                            this.cancel();
                        } else {
                            sendTeleportMessage(player, Wavepoint.prefix + ChatColor.RED + WvLanguage.lang("wavepoint.integrations.vault.transaction_failed") + response.errorMessage);
                            this.cancel();
                        }
                    } else {
                        sendTeleportMessage(player, WvLanguage.lang("wavepoint.teleport_complete"));
                        teleportPhase(player, waypoint);
                        this.cancel();
                    }
                }
                count -= 1;
            }
        }.runTaskTimer(Wavepoint.getInstance(), 0L, 20L);
    }

    private static void teleportPhase(Player player, Waypoint waypoint) {
        player.teleport(waypoint.getLocation());
        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
        if (Wavepoint.isSoundOn) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        try {
            for (String item : plugin.getConfig().getStringList("commands.teleport")) {
                // index 0 out of bounds for length 0?
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
            }
        } catch (Exception ignored) {}
    }

    public static boolean isCurrentlyTeleporting(UUID uuid) {
        return currentlyTeleporting.contains(uuid);
    }

    public static void setCurrentlyTeleporting(UUID uuid, boolean decision) {
        if (decision) { currentlyTeleporting.add(uuid); }
        else { currentlyTeleporting.remove(uuid); }
    }

    private static void sendTeleportMessage(Player player, String message) {
        if (Objects.equals(plugin.getConfig().getString("teleport.action_type", "action"), "action")) {
            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', message));
        } else if (Objects.equals(plugin.getConfig().getString("teleport.action_type", "action"), "message")) {
            player.sendMessage(Wavepoint.prefix + ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
