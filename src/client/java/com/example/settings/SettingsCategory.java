package com.example.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsCategory {
  private final String name;
  private final List<Setting> settings = new ArrayList<>();

  public SettingsCategory(String name) {
    this.name = name;
  }

  public void add(Setting setting) {
    settings.add(setting);
  }

  public String getName() {
    return name;
  }

  public List<Setting> getSettings() {
    return settings;
  }
}
