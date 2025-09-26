package net.solarcosmic.wavepoint.modules;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.objects.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Date;

public class WvInfo {
    public void showInfo(Player player, Waypoint waypoint) {
        if (!player.hasPermission("waypoint.wp.info")) {
            player.sendMessage(WvLanguage.lang("wavepoint.no_permission"));
            return;
        }
        TextComponent base = new TextComponent(Wavepoint.prefix + "Info for '" + ChatColor.AQUA + waypoint.getName() + ChatColor.RESET + "'\n");
        TextComponent wpName = new TextComponent("Name: " + waypoint.getName() + '\n');
        TextComponent wpTime = new TextComponent("Created: " + new Date(waypoint.getTimestamp() * 1000) + '\n');
        Location loc = waypoint.getLocation();
        String coords = Math.floor(loc.getX()) + ", " + Math.floor(loc.getY()) + ", " + Math.floor(loc.getZ());
        TextComponent wpLoc = new TextComponent("Coordinates: " + ChatColor.UNDERLINE + coords + ChatColor.RESET + '\n');
        wpLoc.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coords));
        wpLoc.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_location_hover")).create()
        ));
        TextComponent wpWorld = new TextComponent("World: " + waypoint.getLocation().getWorld().getName() + '\n');
        TextComponent wpClickTP = new TextComponent(ChatColor.UNDERLINE + WvLanguage.lang("wavepoint.waypoint_click_teleport"));
        TextComponent wpPitch = new TextComponent("Pitch: " + waypoint.getLocation().getPitch() + '\n');
        TextComponent wpYaw = new TextComponent("Yaw: " + waypoint.getLocation().getYaw() + '\n');
        wpClickTP.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp tp " + waypoint.getName()));
        wpLoc.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_location_hover")).create()
        ));
        wpClickTP.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.AQUA + WvLanguage.lang("wavepoint.waypoint_click_teleport")).create()
        ));
        base.addExtra(wpName);
        base.addExtra(wpTime);
        base.addExtra(wpLoc);
        base.addExtra(wpWorld);
        base.addExtra(wpPitch);
        base.addExtra(wpYaw);
        base.addExtra(wpClickTP);
        player.spigot().sendMessage(base);
        if (Wavepoint.isSoundOn) player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        try {
            for (String item : Wavepoint.getInstance().getConfig().getStringList("commands.info")) {
                // index 0 out of bounds for length 0?
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), WvPlaceholders.doPlaceholder(item, player));
            }
        } catch (Exception ignored) {}
    }
}
