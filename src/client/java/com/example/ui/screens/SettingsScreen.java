package com.example.ui.screens;

import com.example.settings.Setting;
import com.example.settings.SettingsManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SettingsScreen extends Screen {
  private int selectedIndex = 0;

  public SettingsScreen() {
    super(Text.literal("OPMod Settings"));
  }

  @Override
  public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
    if (selectedIndex >= SettingsManager.CATEGORIES.size()) selectedIndex = 0;

    this.renderBackground(ctx, mouseX, mouseY, delta);
    super.render(ctx, mouseX, mouseY, delta);

    // Titel
    ctx.fill(0, 0, width, height, 0xAA000000);
    ctx.drawCenteredTextWithShadow(
        textRenderer, Text.literal("OPMod Settings"), width / 2, 15, 0xFFFFFFFF);

    // GUI Box auf der linken Seite - Kategorien
    ctx.fill(20, 40, 160, height - 40, 0xFF1E1E1E);
    drawBorder(ctx, 20, 40, 160, height - 40, 0xFF303030);
    ctx.drawTextWithShadow(textRenderer, Text.literal("Categories"), 35, 50, 0xFFFFFFFF);

    int y = 80;

    // Hover Logik
    for (int i = 0; i < SettingsManager.CATEGORIES.size(); i++) {
      boolean hovered = mouseX >= 30 && mouseX <= 140 && mouseY >= y - 2 && mouseY <= y + 12;
      int color = hovered ? 0xFFAAAAAA : 0xFFFFFFFF;
      Text text = Text.literal(SettingsManager.CATEGORIES.get(i).getName());
      if (i == selectedIndex) text = text.copy().formatted(Formatting.UNDERLINE);
      ctx.drawTextWithShadow(textRenderer, text, 35, y, color);
      y += 18;
    }

    // GUI Box auf der rechten Seite - Einstellungen
    ctx.fill(180, 40, width - 20, height - 40, 0xFF1E1E1E);
    drawBorder(ctx, 180, 40, width - 20, height - 40, 0xFF303030);

    ctx.drawCenteredTextWithShadow(
        textRenderer,
        Text.literal("Settings: " + SettingsManager.CATEGORIES.get(selectedIndex).getName()),
        (width + 180) / 2,
        60,
        0xFFFFFFFF);

    int settingY = 90;
    for (Setting setting : SettingsManager.CATEGORIES.get(selectedIndex).getSettings()) {
      setting.render(ctx, textRenderer, 200, settingY, mouseX, mouseY);
      settingY += 24;
    }
  }

  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    int y = 80;
    for (int i = 0; i < SettingsManager.CATEGORIES.size(); i++) {
      if (mouseX >= 30 && mouseX <= 140 && mouseY >= y - 2 && mouseY <= y + 12) {
        selectedIndex = i;
        return true;
      }
      y += 18;
    }

    for (Setting setting : SettingsManager.CATEGORIES.get(selectedIndex).getSettings()) {
      if (setting.mouseClicked(mouseX, mouseY)) return true;
    }

    // Aufruf von super nur, wenn nötig
    return false;
  }

  private void drawBorder(DrawContext ctx, int x1, int y1, int x2, int y2, int color) {
    ctx.fill(x1, y1, x2, y1 + 1, color);
    ctx.fill(x1, y2 - 1, x2, y2, color);
    ctx.fill(x1, y1, x1 + 1, y2, color);
    ctx.fill(x2 - 1, y1, x2, y2, color);
  }
}
