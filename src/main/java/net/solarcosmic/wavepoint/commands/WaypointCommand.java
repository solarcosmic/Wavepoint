package net.solarcosmic.wavepoint.commands;

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
    public List<String> arguments = List.of("set", "list", "teleport");
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You can only execute this command as a player!");
            return false;
        }
        Player player = (Player) commandSender;
        if (args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("You currently have no waypoints.");
        } else if (args[0].equalsIgnoreCase("set")) {
            commandSender.sendMessage("TODO: add set functionality");
        } else if (args[0].equalsIgnoreCase("teleport")) {
            commandSender.sendMessage("TODO: add teleport functionality");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> validArguments = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], arguments, validArguments);
            return validArguments;
        }/* else if (args.length == 2) {
            return List.of("set", "list", "teleport");
        }*/
        return List.of();
    }
}
