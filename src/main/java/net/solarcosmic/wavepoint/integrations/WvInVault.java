package net.solarcosmic.wavepoint.integrations;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.solarcosmic.wavepoint.Wavepoint;
import net.solarcosmic.wavepoint.modules.WvLanguage;
import net.solarcosmic.wavepoint.modules.WvTeleport;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

// This uses the Vault API to communicate with the Vault plugin - an optional dependency.
// This is [[required]] in order to add Vault support.
/* Code from the official repository: https://github.com/MilkBowl/VaultAPI */
public class WvInVault {
    private static Economy econ = null;
    private static Wavepoint plugin = Wavepoint.getInstance();
    private final BetterLogger logger = new BetterLogger("Wavepoint/Integration");

    public WvInVault() {
        if (setupEconomy()) {
            Wavepoint.hasVaultIntegration = true;
            logger.log("Detected Vault plugin! Wavepoint integrations for Vault has been unlocked.");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /* https://github.com/MilkBowl/VaultAPI */
    public static Economy getEconomy() {
        return econ;
    }

    public static boolean hasRequiredCurrency(Player player) {
        double charge_amount = plugin.getConfig().getDouble("integrations.vault.charge_amount");
        if (WvInVault.getEconomy().getBalance(player) < charge_amount) {
            return false;
        }
        return true;
    }
}
