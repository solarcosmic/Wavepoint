package net.solarcosmic.wavepoint.integrations;

import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;

// This uses the CombatLogX API to communicate with the CombatLogX plugin - an optional dependency.
// This is [[required]] in order to add CombatLogX support.
/* Therefore, some code is from the official repository: https://github.com/SirBlobman/CombatLogX */
public class WvInCombatLogX {
    private static Wavepoint plugin = Wavepoint.getInstance();
    private final BetterLogger logger = new BetterLogger("Wavepoint/Integration");

    public WvInCombatLogX() {
        if (Bukkit.getPluginManager().isPluginEnabled("CombatLogX")) {
            Wavepoint.hasCombatLogXIntegration = true;
            logger.log("Detected CombatLogX plugin! Wavepoint integrations for CombatLogX has been unlocked.");
        }
    }

    public static boolean isInCombat(Player player) {
        ICombatLogX plugin = getAPI();
        ICombatManager combatManager = plugin.getCombatManager();
        return combatManager.isInCombat(player);
    }

    public static ICombatLogX getAPI() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin plugin = pluginManager.getPlugin("CombatLogX");
        return (ICombatLogX) plugin;
    }
}
