package com.example.settings;

import com.example.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    public static final List<SettingsCategory> CATEGORIES = new ArrayList<>();

    static {
        SettingsCategory general = new SettingsCategory("General");
        general.add(new ToggleSetting("Enable HUD", "Show or hide the HUD overlay", true));
        general.add(new ToggleSetting("Debug Mode", "Show debug information", false));

        SettingsCategory job_tracker = new SettingsCategory("JOB Tracker");
        job_tracker.add(new ToggleSetting("Enable HUD", "Schaltet das Tracker HUD an", ConfigManager.get().showHUD));
        job_tracker.add(new ToggleSetting("Show Job Name", "Zeigt den aktuellen Job im HUD an", ConfigManager.get().showJob));
        job_tracker.add(new ToggleSetting("Show Level", "Zeigt das Joblevel im HUD an", ConfigManager.get().showLevel));
        job_tracker.add(new ToggleSetting("Show Progress", "Zeigt den Fortschritt mit Balken im HUD an", ConfigManager.get().showFortschritt));
        job_tracker.add(new ToggleSetting("Show Tracking Time", "Zeigt die Tracking Zeit im HUD an", ConfigManager.get().showTrackingZeit));
        job_tracker.add(new ToggleSetting("Show Time Until Level Up", "Zeigt die Zeit bis zum nächsten Level im HUD an", ConfigManager.get().showTimeUntilLevelUp));
        job_tracker.add(new ToggleSetting("Show Money", "Zeigt das verdiente Geld im HUD an", ConfigManager.get().showGeld));
        job_tracker.add(new ToggleSetting("Show Money/Hour", "Zeigt Geld pro Stunde im HUD an", ConfigManager.get().showGeldProStunde));
        job_tracker.add(new ToggleSetting("Show XP", "Zeigt die gesammelten XP im HUD an", ConfigManager.get().showXP));
        job_tracker.add(new ToggleSetting("Show XP/Hour", "Zeigt XP pro Stunde im HUD an", ConfigManager.get().showXPProStunde));
        job_tracker.add(new ToggleSetting("Show Yaw", "Zeigt die Spieler-Yaw im HUD an", ConfigManager.get().showYaw));

        CATEGORIES.add(general);
        CATEGORIES.add(job_tracker);
    }
}