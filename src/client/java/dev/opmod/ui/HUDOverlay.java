package dev.opmod.ui;

import dev.opmod.TimeTracker;
import dev.opmod.config.ConfigManager;
import dev.opmod.jobsystem.tracking.JobData;
import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HUDOverlay implements HudRenderCallback {

  private final ConfigManager config;
  private final SettingsManager settings;
  private final TimeTracker timeTracker;
  private final JobData jobData;

  private static final int WIDTH = 180;
  private static final int LINE_HEIGHT = 10;
  private static final int PADDING = 5;

  private final List<HUDElement> elements = new ArrayList<>();

  public HUDOverlay(
      ConfigManager config, SettingsManager settings, TimeTracker timeTracker, JobData jobData) {
    this.config = config;
    this.settings = settings;
    this.timeTracker = timeTracker;
    this.jobData = jobData;
    initElements();
    HudRenderCallback.EVENT.register(this);
  }

  private void initElements() {
    elements.add(new HUDElement("show_job", () -> line("Aktueller Job", jobData.getJobName())));
    elements.add(
        new HUDElement("show_level", () -> line("Level", String.valueOf(jobData.getLevel()))));
    elements.add(
        new HUDElement(
            "show_progress",
            () -> line("Fortschritt", String.format("%.2f%%", jobData.getPercent()))));
    elements.add(
        new HUDElement(
            "show_tracking_time", () -> line("Tracking Zeit", timeTracker.getFormatted())));
    elements.add(
        new HUDElement("show_time_until_level_up", () -> line("Zeit bis LevelUp", calcETA())));
    elements.add(
        new HUDElement("show_money", () -> line("Geld", format(jobData.getMoney()) + "$")));
    elements.add(
        new HUDElement(
            "show_money_per_hour", () -> line("Geld/h", formatPerHour(jobData.getMoney()))));
    elements.add(new HUDElement("show_xp", () -> line("XP", format(jobData.getXp()))));
    elements.add(
        new HUDElement("show_xp_per_hour", () -> line("XP/h", formatPerHour(jobData.getXp()))));
    elements.add(
        new HUDElement(
            "show_yaw",
            () -> {
              float yaw = MinecraftClient.getInstance().player.getYaw();
              return line("Yaw", String.format("%.1f°", yaw));
            }));
  }

  @Override
  public void onHudRender(DrawContext ctx, RenderTickCounter tickCounter) {
    if (!get("show_hud") || MinecraftClient.getInstance().player == null) return;

    int x = config.getConfig().hud_x;
    int y = config.getConfig().hud_y;

    List<Text> lines = new ArrayList<>();
    for (HUDElement e : elements) {
      if (get(e.key())) lines.add(e.content().get());
    }

    int height = lines.size() * LINE_HEIGHT + PADDING * 2;
    ctx.fill(x - PADDING, y - PADDING, x + WIDTH, y + height, 0x88000000);

    TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
    int offsetY = y;
    for (Text t : lines) {
      ctx.drawTextWithShadow(renderer, t, x, offsetY, 0xFFFFFF);
      offsetY += LINE_HEIGHT;
    }
  }

  private boolean get(String key) {
    Setting<?> s = settings.getSettingByKey(key);
    return s != null && Boolean.TRUE.equals(s.getValue());
  }

  private Text line(String label, String value) {
    return Text.literal(label + ": ")
        .formatted(Formatting.GRAY)
        .append(Text.literal(value).formatted(Formatting.AQUA));
  }

  private String format(double value) {
    return String.format("%,.2f", value);
  }

  private String formatPerHour(double total) {
    long seconds = timeTracker.getElapsedSeconds();
    if (seconds == 0) return "0.00";
    return format(total / seconds * 3600);
  }

  private String calcETA() {
    double xpNeeded = 2_500_000;
    double progress = jobData.getPercent() / 100.0;
    double xpCurrent = xpNeeded * progress;
    double xpLeft = xpNeeded - xpCurrent;

    long seconds = timeTracker.getElapsedSeconds();
    double xpPerHour = seconds > 0 ? jobData.getXp() / seconds * 3600 : 0;
    if (xpPerHour <= 0) return "--:--";

    double hours = xpLeft / xpPerHour;
    int minutes = (int) (hours * 60);
    return String.format("%02d:%02d", minutes / 60, minutes % 60);
  }
}
