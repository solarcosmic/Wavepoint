package net.solarcosmic.wavepoint;

import com.google.gson.*;
import net.solarcosmic.wavepoint.objects.Waypoint;
import net.solarcosmic.wavepoint.util.BetterLogger;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WvConfig {
    private final Wavepoint plugin = Wavepoint.getInstance();
    private File storageFile = new File("plugins/Wavepoint", "storage.json");
    private Gson gson = new GsonBuilder().create();
    private final BetterLogger logger = new BetterLogger("&b[Wavepoint | Logger]&r");

    public void loadData() {
        if (!storageFile.exists()) {
            try {
                if (storageFile.createNewFile()) {
                    logger.log("settings.json created.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Waypoint waypoint = new Waypoint("AxolotlTGT", 88);
        try (Writer writer = new FileWriter(storageFile)) {
            String thing = new String(Files.readAllBytes(storageFile.toPath()));
            System.out.println(thing);
            gson.toJson(waypoint, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log("Written to JSON.");
    }
}
