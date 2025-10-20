package dev.opmod.settings;

import dev.opmod.config.ConfigManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {

  private final List<SettingsCategory> categories = new ArrayList<>();
  private final ConfigManager config;

  public SettingsManager(ConfigManager config) {
    this.config = config;
    buildCategories();
  }

  public List<SettingsCategory> getCategories() {
    return categories;
  }

  public Setting<?> getSettingByKey(String key) {
    return categories.stream()
        .flatMap(c -> c.getSettings().stream())
        .filter(s -> s.getKey().equals(key))
        .findFirst()
        .orElse(null);
  }

  private void buildCategories() {
    SettingsCategory general = new SettingsCategory("Allgemein");
    general.add(toggle("show_hud", "HUD anzeigen", true));
    general.add(toggle("inventory_full_warning", "Inventar voll Warnung", true));
    general.add(toggle("confirm_sign_command", "Sign Schutz", true));
    general.add(toggle("disable_offhand_use", "Offhand Placement deaktivieren", false));
    categories.add(general);

    SettingsCategory performance = new SettingsCategory("Performance");
    performance.add(toggle("auto_idle_render", "Auto Idle Render", false));
    categories.add(performance);

    SettingsCategory jobs = new SettingsCategory("Job Tracker");
    jobs.add(toggle("show_job", "Job anzeigen", true));
    jobs.add(toggle("show_level", "Level anzeigen", true));
    jobs.add(toggle("show_progress", "Fortschritt anzeigen", true));
    jobs.add(toggle("show_tracking_time", "Tracking Zeit anzeigen", true));
    jobs.add(toggle("show_time_until_level_up", "Zeit bis LevelUp anzeigen", false));
    jobs.add(toggle("show_money", "Geld anzeigen", true));
    jobs.add(toggle("show_money_per_hour", "Geld pro Stunde anzeigen", true));
    jobs.add(toggle("show_xp", "XP anzeigen", true));
    jobs.add(toggle("show_xp_per_hour", "XP pro Stunde anzeigen", true));
    jobs.add(toggle("show_yaw", "Yaw anzeigen", false));
    categories.add(jobs);
  }

  private ToggleSetting toggle(String key, String title, boolean def) {
    boolean startValue = readBoolean(key, def);
    ToggleSetting t = new ToggleSetting(key, title, startValue);

    if (!key.equals("auto_idle_render")) {
      t.setOnChange(v -> config.set(key, v));
    }

    return t;
  }

  private boolean readBoolean(String field, boolean def) {
    try {
      Field f = config.getConfig().getClass().getDeclaredField(field);
      f.setAccessible(true);
      Object v = f.get(config.getConfig());
      return v instanceof Boolean ? (Boolean) v : def;
    } catch (Exception e) {
      return def;
    }
  }
}
