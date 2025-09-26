package net.solarcosmic.wavepoint.modules;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.solarcosmic.wavepoint.Wavepoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WvList {

    public void showList(Player player) {
        StringBuilder waypointString = new StringBuilder();
        ArrayList<String> wpList = WvWaypoints.buildList(player.getUniqueId());
        if (wpList.toArray().length == 0) {
            TextComponent base = new TextComponent(Wavepoint.prefix + WvLanguage.lang("wavepoint.no_waypoints"));
            player.spigot().sendMessage(base);
            if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            try {
                for (String item : Wavepoint.getInstance().getConfig().getStringList("commands.list")) {
                    // index 0 out of bounds for length 0?
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
                }
            } catch (Exception ignored) {}
        } else {
            TextComponent base = new TextComponent(Wavepoint.prefix + WvLanguage.lang("wavepoint.your_waypoints") + " (" + wpList.toArray().length + ")\n" + waypointString.toString());
            for (String item : wpList) {
                TextComponent comp = new TextComponent("  " + ChatColor.UNDERLINE + item + ChatColor.RESET);
                comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp info " + item));
                comp.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_hover_info")).create()
                ));
                base.addExtra(comp);
            }

            player.spigot().sendMessage(base);
            if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            try {
                for (String item : Wavepoint.getInstance().getConfig().getStringList("commands.list")) {
                    // index 0 out of bounds for length 0?
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
                }
            } catch (Exception ignored) {}
        }
    }
}
