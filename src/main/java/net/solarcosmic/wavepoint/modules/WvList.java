package net.solarcosmic.wavepoint.modules;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.solarcosmic.wavepoint.Wavepoint;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WvList {

    public void showList(Player player) {
        StringBuilder waypointString = new StringBuilder();
        ArrayList<String> wpList = WvWaypoints.buildList(player.getUniqueId());
        TextComponent base = new TextComponent("\n" + Wavepoint.prefix + "Your waypoints (" + wpList.toArray().length + ")\n" + waypointString.toString());
        for (String item : wpList) {
            TextComponent comp = new TextComponent("  " + ChatColor.UNDERLINE + item + ChatColor.RESET);
            comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp info " + item));
            comp.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_hover_info")).create()
            ));
            base.addExtra(comp);
        }
        player.sendMessage(base);
    }
}
