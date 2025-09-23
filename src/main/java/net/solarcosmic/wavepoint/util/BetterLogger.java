package net.solarcosmic.wavepoint.util;

import net.solarcosmic.wavepoint.Wavepoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;

public class BetterLogger {
    public String prefix;
    public BetterLogger(@Nullable String logPrefix) {
        prefix = ChatColor.translateAlternateColorCodes('&', logPrefix);
    }
    public void log(String message) {
        String newMessage = "&b[" + prefix + "]:&r " + message;
        String chatEncode = ChatColor.translateAlternateColorCodes('&', newMessage);
        Bukkit.getConsoleSender().sendMessage(chatEncode);
    }
    public void debug(String message) {
        if (Wavepoint.isDebugEnabled) {
            String newMessage = "[" + prefix + "/Debug]:&r " + message;
            String chatEncode = ChatColor.translateAlternateColorCodes('&', newMessage);
            Bukkit.getConsoleSender().sendMessage(chatEncode);
        }
    }
}