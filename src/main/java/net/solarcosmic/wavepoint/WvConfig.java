package net.solarcosmic.wavepoint;

import com.google.gson.*;
import net.solarcosmic.wavepoint.objects.Waypoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class WvConfig {
    private final Wavepoint plugin = Wavepoint.getInstance();
    private File storageFile = new File("plugins/Wavepoint", "storage.json");
    private final BetterLogger logger = new BetterLogger("&b[Wavepoint | Logger]&r");
    private HashMap<String, Object> defaults = new HashMap<>();

    private JSONObject json;
    private JSONParser parser = new JSONParser();

    public void loadData() {
        if (!storageFile.exists()) {
            try (PrintWriter pw = new PrintWriter(storageFile, StandardCharsets.UTF_8)) {
                pw.print("{");
                pw.print("}");
                pw.flush();
                json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(storageFile), StandardCharsets.UTF_8));
                JSONObject anObject = new JSONObject();
                anObject.put("Example", "example");
                anObject.put("Example2", "example");
                defaults.put("MyObject", anObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // The following functions are from https://www.spigotmc.org/threads/tutorial-a-guide-on-json-configurations.181365/
    // It would be pointless to write them out, so I copy and pasted them here.

    public String getRawData(String key) {
        return json.containsKey(key) ? json.get(key).toString() : (defaults.containsKey(key) ? defaults.get(key).toString() : key);
    }

    public String getString(String key) {
        return ChatColor.translateAlternateColorCodes('&', getRawData(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(getRawData(key));
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(getRawData(key));
        } catch (Exception ex) { }
        return -1;
    }

    public double getInteger(String key) {
        try {
            return Integer.parseInt(getRawData(key));
        } catch (Exception ex) { }
        return -1;
    }

    public JSONObject getObject(String key) {
        return json.containsKey(key) ? (JSONObject) json.get(key)
                : (defaults.containsKey(key) ? (JSONObject) defaults.get(key) : new JSONObject());
    }

    public JSONArray getArray(String key) {
        return json.containsKey(key) ? (JSONArray) json.get(key)
                : (defaults.containsKey(key) ? (JSONArray) defaults.get(key) : new JSONArray());
    }

    public boolean save() {
        try {
            JSONObject saveThing = new JSONObject();
            for (String s : defaults.keySet()) {
                Object o = defaults.get(s);
                if (o instanceof String) {
                    saveThing.put(s, getString(s));
                } else if (o instanceof Double) {
                    saveThing.put(s, getDouble(s));
                } else if (o instanceof Integer) {
                    saveThing.put(s, getInteger(s));
                } else if (o instanceof JSONObject) {
                    saveThing.put(s, getObject(s));
                } else if (o instanceof JSONArray) {
                    saveThing.put(s, getArray(s));
                }
            }
            TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
            treeMap.putAll(saveThing);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String toTransform = gson.toJson(treeMap);
            FileWriter fw = new FileWriter(storageFile);
            fw.write(toTransform);
            fw.flush();
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
