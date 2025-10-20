package dev.opmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.opmod.misc.ModInfo;
import java.io.*;

public class ConfigManager {

  private static final File FILE = new File("config/opmod_hud.json");
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private HUDConfig data;

  public static class HUDConfig {
    public int hud_x = 10;
    public int hud_y = 10;

    public boolean show_hud = true;
    public boolean show_job = true;
    public boolean show_level = true;
    public boolean show_progress = true;
    public boolean show_tracking_time = true;
    public boolean show_time_until_level_up = false;
    public boolean show_money = true;
    public boolean show_money_per_hour = true;
    public boolean show_xp = true;
    public boolean show_xp_per_hour = true;
    public boolean show_yaw = false;
  }

  public ConfigManager() {
    load();
    save();
  }

  public void load() {
    if (!FILE.exists()) {
      loadDefaults();
      return;
    }

    try (FileReader reader = new FileReader(FILE)) {
      data = GSON.fromJson(reader, HUDConfig.class);
      validate();
    } catch (Exception e) {
      System.err.println(
          "[" + ModInfo.getName() + "] Fehler in opmod_hud.json – Defaults geladen.");
      loadDefaults();
    }
  }

  private void loadDefaults() {
    try (InputStream in = getClass().getResourceAsStream("/assets/opmod/opmod_hud.json")) {
      if (in != null) {
        data = GSON.fromJson(new InputStreamReader(in), HUDConfig.class);
      } else {
        data = new HUDConfig();
      }
    } catch (Exception e) {
      data = new HUDConfig();
    }
  }

  private void validate() {
    HUDConfig def = new HUDConfig();
    try {
      for (var field : HUDConfig.class.getDeclaredFields()) {
        field.setAccessible(true);
        if (field.get(data) == null) {
          field.set(data, field.get(def));
        }
      }
    } catch (Exception ignored) {
    }
  }

  public void save() {
    try {
      FILE.getParentFile().mkdirs();
      try (FileWriter writer = new FileWriter(FILE)) {
        GSON.toJson(data, writer);
      }
    } catch (IOException e) {
      System.err.println(
          "[" + ModInfo.getName() + "] Konnte Config nicht speichern: " + e.getMessage());
    }
  }

  public HUDConfig getConfig() {
    return data;
  }

  public void set(String key, Object value) {
    try {
      var field = HUDConfig.class.getDeclaredField(key);
      field.setAccessible(true);
      field.set(data, value);
      save();
    } catch (Exception e) {
      System.err.println("[" + ModInfo.getName() + "] Config Feld nicht gesetzt: " + key);
    }
  }
}
