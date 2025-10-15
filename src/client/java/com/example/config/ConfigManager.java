/* Handles configuration loading and saving for the OPMod HUD */
package com.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/opmod_hud.json");

    /** Repräsentiert die gesamte HUD-Konfiguration */
    public static class HUDConfig {
        public int hudX = 10;
        public int hudY = 10;

        public boolean showHUD = true;
        public boolean showJob = true;
        public boolean showLevel = true;
        public boolean showFortschritt = true;
        public boolean showTrackingZeit = true;
        public boolean showTimeUntilLevelUp = false;
        public boolean showGeld = true;
        public boolean showGeldProStunde = true;
        public boolean showXP = true;
        public boolean showXPProStunde = true;
        public boolean showYaw = false;
    }

    private static HUDConfig config = new HUDConfig();



    /** Lädt die Config aus der JSON-Datei oder erstellt die Standardwerte, falls sie noch nicht existiert */
    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save(); // Standardwerte speichern
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            HUDConfig loaded = gson.fromJson(reader, HUDConfig.class);
            if (loaded != null) config = loaded;
        } catch (IOException e) {
            System.err.println("[OPMod] Could not load config: " + e.getMessage());
        }

        // Direkt HUDSettings aus der geladenen Config setzen
        syncToHUDSettings();
    }

    /** Speichert die aktuelle Config in die JSON-Datei */
    public static void save() {
        syncFromHUDConfig(); // erst die aktuellen HUDSettings in die Config übertragen

        try {
            CONFIG_FILE.getParentFile().mkdirs(); // Ordner erstellen, falls nötig
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                gson.toJson(config, writer);
            }
        } catch (IOException e) {
            System.err.println("[OPMod] Could not save config: " + e.getMessage());
        }
    }

    /** Synchronisiert die HUDSettings in die Config */
    private static void syncFromHUDConfig() {
        config.hudX = hudX;
        config.hudY = HUDConfig.hudY;

        config.showHUD = HUDConfig.showHUD;
        config.showJob = HUDConfig.showJob;
        config.showLevel = HUDConfig.showLevel;
        config.showFortschritt = HUDConfig.showFortschritt;
        config.showTrackingZeit = HUDConfig.showTrackingZeit;
        config.showTimeUntilLevelUp = HUDConfig.showTimeUntilLevelUp;
        config.showGeld = HUDConfig.showGeld;
        config.showGeldProStunde = HUDConfig.showGeldProStunde;
        config.showXP = HUDConfig.showXP;
        config.showXPProStunde = HUDConfig.showXPProStunde;
        config.showYaw = HUDConfig.showYaw;
    }

    /** Synchronisiert die Config in die HUDSettings */
    private static void syncToHUDSettings() {
        hudX = hudX;
        ConfigManager.HUDConfig.hudY = config.hudY;

        ConfigManager.HUDConfig.showHUD = config.showHUD;
        ConfigManager.HUDConfig.showJob = config.showJob;
        ConfigManager.HUDConfig.showLevel = config.showLevel;
        ConfigManager.HUDConfig.showFortschritt = config.showFortschritt;
        ConfigManager.HUDConfig.showTrackingZeit = config.showTrackingZeit;
        ConfigManager.HUDConfig.showTimeUntilLevelUp = config.showTimeUntilLevelUp;
        ConfigManager.HUDConfig.showGeld = config.showGeld;
        ConfigManager.HUDConfig.showGeldProStunde = config.showGeldProStunde;
        ConfigManager.HUDConfig.showXP = config.showXP;
        ConfigManager.HUDConfig.showXPProStunde = config.showXPProStunde;
        ConfigManager.HUDConfig.showYaw = config.showYaw;
    }

    /** Liefert die Config Instanz (falls direkt benötigt) */
    public static HUDConfig get() {
        return config;
    }



    /** Setzt die HUD-Position und speichert die Config sofort */
    public static void setHUDPosition(int x, int y) {
        hudX = x;
        ConfigManager.HUDConfig.hudY = y;
        save();
    }
}
