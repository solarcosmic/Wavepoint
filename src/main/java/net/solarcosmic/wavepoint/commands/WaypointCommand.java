package net.solarcosmic.wavepoint.commands;

import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.modules.*;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WaypointCommand implements TabExecutor {
    public List<String> arguments = List.of("set", "list", "tp", "delete", "info", "global");
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(WvLanguage.lang("wavepoint.command_not_executable"));
            return false;
        }
        Player player = (Player) commandSender;
        if (args.length < 1) {
            player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
            return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            new WvList().showList(player);
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
                return false;
            }
            WvWaypoints.create(player, player.getLocation(), args[1]);
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (args.length < 2) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
                return false;
            }
            Waypoint wp = WvWaypoints.getWaypoint(player.getUniqueId(), args[1]);
            if (wp == null) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.argument_unexisting"));
            } else {
                WvTeleport.teleport(player, wp);
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
                return false;
            }
            Waypoint wp = WvWaypoints.getWaypoint(player.getUniqueId(), args[1]);
            if (wp == null) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.argument_unexisting"));
            } else {
                WvWaypoints.delete(wp);
                player.sendMessage(Wavepoint.prefix + "Waypoint deleted.");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
                return false;
            }
            Waypoint wp = WvWaypoints.getWaypoint(player.getUniqueId(), args[1]);
            if (wp == null) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.argument_unexisting"));
            } else {
                new WvInfo().showInfo(player, wp);
            }
        } else if (args[0].equalsIgnoreCase("global")) {
            if (args.length < 2) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.command_usage"));
                return false;
            }
            Waypoint wp = WvWaypoints.getGlobalWaypoint(args[1]);
            if (wp == null) {
                player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.argument_unexisting"));
            } else {
                new WvInfo().showInfo(player, wp);
            }
        } else {
            player.sendMessage(Wavepoint.prefix + WvLanguage.lang("wavepoint.argument_unexisting"));
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> validArguments = new ArrayList<>();
        final List<String> validArguments2 = new ArrayList<>();
        Player player = (Player) commandSender;
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], arguments, validArguments);
            return validArguments;
        }
        ArrayList<String> list = WvWaypoints.buildList(player.getUniqueId());
        if (args.length == 2 && (args[0].equals("tp") || args[0].equals("delete") || args[0].equals("info"))) {
            StringUtil.copyPartialMatches(args[1], list, validArguments2);
            return validArguments2;
        }
        return List.of();
    }
}
