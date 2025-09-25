package net.solarcosmic.wavepoint.modules;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

public class WvInfo {
    public void showInfo(Player player, Waypoint waypoint) {
        TextComponent base = new TextComponent("\n" + Wavepoint.prefix + "Info for '" + ChatColor.AQUA + waypoint.getName() + ChatColor.RESET + "'\n");
        TextComponent wpName = new TextComponent("Name: " + waypoint.getName() + '\n');
        TextComponent wpTime = new TextComponent("Created: " + new Date(waypoint.getTimestamp() * 1000) + '\n');
        Location loc = waypoint.getLocation();
        String coords = loc.x() + ", " + loc.y() + ", " + loc.z();
        TextComponent wpLoc = new TextComponent("Coordinates: " + ChatColor.UNDERLINE + coords + ChatColor.RESET + '\n');
        wpLoc.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coords));
        wpLoc.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_location_hover")).create()
        ));
        base.addExtra(new TextComponent("\n"));
        base.addExtra(wpName);
        base.addExtra(wpTime);
        base.addExtra(wpLoc);
        player.sendMessage(base);
    }
}
