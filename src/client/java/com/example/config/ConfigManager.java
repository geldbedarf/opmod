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

  public static class HUDConfig {
    public int hudX = 10;
    public int hudY = 10;

    public boolean enableInvFullWarning = true;
    public int InvFullWarningInterval = 3;
    public boolean enableInvFullWarningSound = true;

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

  public static void load() {
    if (!CONFIG_FILE.exists()) {
      save(); // Erstelle Datei mit Standardwerten
      return;
    }

    try (FileReader reader = new FileReader(CONFIG_FILE)) {
      HUDConfig loaded = gson.fromJson(reader, HUDConfig.class);
      if (loaded != null) config = loaded;
    } catch (IOException e) {
      System.err.println("[OPMod] Could not load config: " + e.getMessage());
    }
  }

  public static void save() {
    try {
      CONFIG_FILE.getParentFile().mkdirs();
      try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
        gson.toJson(config, writer);
      }
    } catch (IOException e) {
      System.err.println("[OPMod] Could not save config: " + e.getMessage());
    }
  }

  public static HUDConfig get() {
    return config;
  }

  public static int set(String fieldName, Object value) {
    try {
      var field = HUDConfig.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(config, value);
      save();
      System.out.println("[OPMod] Set " + fieldName + " = " + value);
    } catch (Exception e) {
      System.err.println("[OPMod] Could not set field '" + fieldName + "': " + e.getMessage());
    }
      return 0;
  }
}
