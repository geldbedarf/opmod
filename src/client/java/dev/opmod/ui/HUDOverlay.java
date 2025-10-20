package dev.opmod.ui;

import dev.opmod.TimeTracker;
import dev.opmod.config.ConfigManager;
import dev.opmod.jobsystem.tracking.JobData;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class HUDOverlay {

  public static int HUD_WIDTH = 150;
  public static int HUD_HEIGHT = 6;
  public static final int PADDING = 5;
  public static final int LINE_HEIGHT = 10;
  private static boolean alignRight;

  public static void init() {
    ConfigManager.load();
    HudRenderCallback.EVENT.register(HUDOverlay::renderHUD);
  }

  public static void setAlignmentByPosition(int screenWidth) {
    alignRight = ConfigManager.get().hudX + HUD_WIDTH / 2 >= screenWidth / 2;
  }

  public static void renderHUD(DrawContext ctx, RenderTickCounter tickCounter) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client.player == null || client.options.hudHidden) return;

    var cfg = ConfigManager.get();
    if (!cfg.showHUD) return;

    TextRenderer tr = client.textRenderer;
    int x = cfg.hudX;
    int y = cfg.hudY;

    List<Text> lines = new ArrayList<>();
    HUD_HEIGHT = 6;

    if (cfg.showJob) {
      HUD_HEIGHT += 10;
      lines.add(
          Text.literal("Aktueller Job: ")
              .formatted(Formatting.GRAY)
              .append(Text.literal(JobData.jobName).formatted(Formatting.AQUA)));
    }

    if (cfg.showLevel) {
      HUD_HEIGHT += 10;
      lines.add(formatLine("Level", String.valueOf(JobData.level)));
    }

    if (cfg.showFortschritt) {
      HUD_HEIGHT += 10;
      lines.add(
          formatLine(
              "Fortschritt",
              String.format("%.2f%%", JobData.percent) + " " + buildProgressBar(JobData.percent)));
    }

    if (cfg.showTrackingZeit) {
      HUD_HEIGHT += 10;
      lines.add(formatLine("Tracking Zeit", TimeTracker.getFormattedTime()));
    }

    if (cfg.showTimeUntilLevelUp) {
      HUD_HEIGHT += 10;
      double xpPerLevel = 2_500_000;
      double currentXpInLevel = (JobData.percent / 100.0) * xpPerLevel;
      double remainingXp = xpPerLevel - currentXpInLevel;

      double timeSeconds = TimeTracker.getTime();
      double xpPerHour = (timeSeconds > 0) ? (JobData.xp / timeSeconds * 3600) : 0;

      String formattedTime;
      if (xpPerHour <= 0) {
        formattedTime = "00:00";
      } else {
        double hoursUntilLevelUp = remainingXp / xpPerHour;
        int totalMinutes = (int) (hoursUntilLevelUp * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        formattedTime = String.format("%02d:%02d", hours, minutes);
      }

      lines.add(formatLine("Zeit bis Level Up", formattedTime));
    }

    if (cfg.showGeld) {
      HUD_HEIGHT += 10;
      lines.add(formatLine("Geld", String.format("%,.2f", JobData.money)));
    }

    if (cfg.showGeldProStunde) {
      HUD_HEIGHT += 10;
      lines.add(
          formatLine(
              "Geld/h",
              String.format(
                  "%,.2f",
                  (TimeTracker.getTime() > 0)
                      ? (JobData.money / TimeTracker.getTime() * 3600)
                      : 0)));
    }

    if (cfg.showXP) {
      HUD_HEIGHT += 10;
      lines.add(formatLine("XP", String.format("%,.2f", JobData.xp)));
    }

    if (cfg.showXPProStunde) {
      HUD_HEIGHT += 10;
      lines.add(
          formatLine(
              "XP/h",
              String.format(
                  "%,.2f",
                  (TimeTracker.getTime() > 0) ? (JobData.xp / TimeTracker.getTime() * 3600) : 0)));
    }

    if (cfg.showYaw) {
      HUD_HEIGHT += 10;
      lines.add(formatLine("Yaw", String.format("%.2f°", client.player.getYaw())));
    }

    ctx.fill(x - PADDING, y - PADDING, x + HUD_WIDTH, y + HUD_HEIGHT, 0x88000000);

    for (Text line : lines) {
      int textWidth = tr.getWidth(line);
      int drawX = alignRight ? x + HUD_WIDTH - textWidth - PADDING : x + PADDING;
      ctx.drawTextWithShadow(tr, line, drawX, y, 0xFFFFFFFF);
      y += LINE_HEIGHT;
    }
  }

  private static String buildProgressBar(double percent) {
    StringBuilder bar = new StringBuilder();
    int green = (int) Math.round(percent / 10.0);
    for (int i = 0; i < 10; i++) {
      bar.append(i < green ? "§a|" : "§8|");
    }
    return bar.toString();
  }

  private static Text formatLine(String label, String value) {
    return Text.literal(label + ": ")
        .formatted(Formatting.GRAY)
        .append(Text.literal(value).formatted(Formatting.AQUA));
  }
}
