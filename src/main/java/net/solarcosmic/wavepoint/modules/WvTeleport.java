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

import java.util.*;

public class WvTeleport {
    public static ArrayList<UUID> currentlyTeleporting = new ArrayList<>();
    private static final Wavepoint plugin = Wavepoint.getInstance();
    public static final HashMap<UUID, Long> tpCooldowns = new HashMap<>();
    private static final long teleportCooldown = plugin.getConfig().getLong("teleport_cooldown", 10) * 1000L;
    public static final HashMap<UUID, Long> recentlyTeleported = new HashMap<>();
    public static final long tpIgnoreDuration = 1000;
    public static final Set<UUID> ignoreNextMove = new HashSet<>();

    /*
    Teleports a player to a waypoint because why not?
     */
    public static void teleport(Player player, Waypoint waypoint) {
        System.out.println(1);
        UUID uuid = player.getUniqueId();
        WvTeleport.setCurrentlyTeleporting(uuid, true);
        long now = System.currentTimeMillis();
        if (tpCooldowns.containsKey(uuid)) {
            long lastTP = tpCooldowns.get(uuid);
            long timeLeft = lastTP + teleportCooldown - now;
            if (timeLeft > 0) {
                sendTeleportMessage(player, WvLanguage.lang("wavepoint.teleport_cooldown").replace("${cooldown}", String.valueOf(timeLeft / 1000)));
                System.out.println(2);
                return;
            }
        }
        System.out.println(3);
        BukkitTask task = new BukkitRunnable() {
            int count = 5;
            public void run() {
                System.out.println(4);
                if (Wavepoint.hasCombatLogXIntegration) {
                    if (WvInCombatLogX.isInCombat(player)) {
                        if (!plugin.getConfig().getBoolean("integrations.combatlogx.combat.teleport", true)) {
                            player.sendMessage(WvLanguage.lang("integrations.combatlogx.in_combat"));
                            System.out.println(5);
                            this.cancel();
                            return;
                        }
                    }
                }
                if (Wavepoint.hasVaultIntegration) {
                    if (!WvTeleport.isCurrentlyTeleporting(player.getUniqueId())) {
                        this.cancel();
                        System.out.println(6);
                        return;
                    }
                    if (!WvInVault.hasRequiredCurrency(player)) {
                        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
                        this.cancel();
                        System.out.println(7);
                        return;
                    }
                }
                if (count == 1) {
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0f, 1.0f);
                    sendTeleportMessage(player, WvLanguage.lang("wavepoint.wait_teleport_single"));
                    System.out.println(8);
                } else {
                    if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    sendTeleportMessage(player, WvLanguage.lang("wavepoint.wait_teleport_plural").replace("${seconds}", String.valueOf(count)));
                    System.out.println(9);
                }
                if (count < 1) {
                    if (Wavepoint.hasVaultIntegration) {
                        Economy economy = WvInVault.getEconomy();
                        double charge_amount = plugin.getConfig().getDouble("integrations.vault.charge_amount", 5.0);
                        EconomyResponse response = economy.withdrawPlayer(player, charge_amount);
                        if (response.transactionSuccess()) { // response.amount
                            sendTeleportMessage(player, WvLanguage.lang("wavepoint.teleport_complete").replace("${amount}", String.valueOf(response.amount)) + " " + WvLanguage.lang("wavepoint.teleport_complete_vault").replace("${amount}", String.valueOf(response.amount)));
                            teleportPhase(player, waypoint);
                            System.out.println(10);
                            this.cancel();
                        } else {
                            sendTeleportMessage(player, Wavepoint.prefix + WvLanguage.lang("wavepoint.integrations.vault.transaction_failed") + response.errorMessage);
                            System.out.println(11);
                            this.cancel();
                        }
                    } else {
                        sendTeleportMessage(player, WvLanguage.lang("wavepoint.teleport_complete"));
                        teleportPhase(player, waypoint);
                        System.out.println(12);
                        this.cancel();
                    }
                }
                count -= 1;
                WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), true);
                System.out.println("stopping any time soon maybe?");
            }
        }.runTaskTimer(Wavepoint.getInstance(), 0L, 20L);
    }

    private static void teleportPhase(Player player, Waypoint waypoint) {
        player.teleport(waypoint.getLocation());
        ignoreNextMove.add(player.getUniqueId());
        setAsRecentlyTeleported(player.getUniqueId());
        WvTeleport.setCurrentlyTeleporting(player.getUniqueId(), false);
        tpCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
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
        System.out.println(13);
    }

    public static boolean isCurrentlyTeleporting(UUID uuid) {
        return currentlyTeleporting.contains(uuid);
    }

    public static void setAsRecentlyTeleported(UUID uuid) {
        recentlyTeleported.put(uuid, System.currentTimeMillis());
    }

    public static boolean hasRecentlyTeleported(UUID uuid) {
        Long t = recentlyTeleported.get(uuid);
        if (t == null) return false;
        if (System.currentTimeMillis() - t < tpIgnoreDuration) return true;
        recentlyTeleported.remove(uuid);
        return false;
    }

    public static void setCurrentlyTeleporting(UUID uuid, boolean decision) {
        if (decision) { currentlyTeleporting.add(uuid); }
        else { currentlyTeleporting.remove(uuid); }
    }

    public static void sendTeleportMessage(Player player, String message) {
        if (message.equals(WvLanguage.lang("wavepoint.waypoint_moved")) && !isCurrentlyTeleporting(player.getUniqueId())) {
            System.out.println("Attempt to print teleport moved message after teleport");
            return;
        }
        String newMessage = WvPlaceholders.doPlaceholder(message, player);
        if (Objects.equals(plugin.getConfig().getString("teleport.action_type", "action"), "action")) {
            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', newMessage));
        } else if (Objects.equals(plugin.getConfig().getString("teleport.action_type", "action"), "message")) {
            player.sendMessage(Wavepoint.prefix + ChatColor.translateAlternateColorCodes('&', newMessage));
        }
        System.out.println(14);
    }
}
