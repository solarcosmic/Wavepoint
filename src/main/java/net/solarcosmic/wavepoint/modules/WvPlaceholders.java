package net.solarcosmic.wavepoint.modules;

import org.bukkit.entity.Player;

public class WvPlaceholders {
    public static String doPlaceholder(String unfilteredString, Player player) {
        String modifiedString = unfilteredString;
        modifiedString = modifiedString.replace("${player}", player.getName());
        modifiedString = modifiedString.replace("${playerId}", player.getUniqueId().toString());
        return modifiedString;
    }
}