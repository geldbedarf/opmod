package com.example.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    public static final List<SettingsCategory> CATEGORIES = new ArrayList<>();

    static {
        SettingsCategory general = new SettingsCategory("General");
        general.add(new ToggleSetting("Enable HUD", "Show or hide the HUD overlay", true));
        general.add(new ToggleSetting("Debug Mode", "Show debug information", false));

        SettingsCategory hud = new SettingsCategory("HUD");
        hud.add(new ToggleSetting("Compact HUD", "Enable compact HUD layout", false));
        hud.add(new ToggleSetting("Test", "This is a test", false));

        CATEGORIES.add(general);
        CATEGORIES.add(hud);
    }
}