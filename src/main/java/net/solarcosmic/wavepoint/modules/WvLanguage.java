package net.solarcosmic.wavepoint.modules;

import com.tchristofferson.configupdater.ConfigUpdater;
import net.solarcosmic.wavepoint.Wavepoint;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WvLanguage {
    public static String chosenLang = "en_us";
    public static File langFile;
    public static FileConfiguration configFile;
    private static final Wavepoint plugin = Wavepoint.getInstance();

    public WvLanguage(String lang) {
        langFile = new File(plugin.getDataFolder(), "languages/" + lang + ".yml");
        try {
            if (!langFile.exists()) plugin.saveResource("languages/" + lang + ".yml", false);
            ConfigUpdater.update(plugin, "languages/" + lang + ".yml", langFile);
            configFile = YamlConfiguration.loadConfiguration(langFile);
            chosenLang = lang;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLanguage() {
        return chosenLang;
    }

    public static String lang(String item) {
        assert configFile != null;
        if (configFile.getString(item) == null) return "";
        return ChatColor.translateAlternateColorCodes('&', configFile.getString(item, "(ERR: Failed to load language item)"));
    }
}
