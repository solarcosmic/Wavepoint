package net.solarcosmic.wavepoint.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;

public class BetterLogger {
    public String prefix = "[BetterLogger]";
    public BetterLogger(@Nullable String logPrefix) {
        prefix = ChatColor.translateAlternateColorCodes('&', logPrefix);
    }
    public void log(String message) {
        String chatEncode = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.getConsoleSender().sendMessage(prefix + ": " + chatEncode);
    }
}