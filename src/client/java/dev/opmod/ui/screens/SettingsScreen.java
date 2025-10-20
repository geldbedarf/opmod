package dev.opmod.ui.screens;

import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SettingsScreen extends Screen {

  private final SettingsManager settingsManager;
  private int selectedIndex = 0;

  public SettingsScreen(SettingsManager settingsManager) {
    super(Text.literal("OPMod Settings"));
    this.settingsManager = settingsManager;
  }

  @Override
  public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
    this.renderBackground(ctx, mouseX, mouseY, delta);

    super.render(ctx, mouseX, mouseY, delta);

    ctx.fill(0, 0, width, height, 0xAA000000);
    ctx.drawCenteredTextWithShadow(
        textRenderer, Text.literal("OPMod Settings"), width / 2, 15, 0xFFFFFFFF);

    // Linke Box - Kategorien
    ctx.fill(20, 40, 160, height - 40, 0xFF1E1E1E);
    drawBorder(ctx, 20, 40, 160, height - 40, 0xFF303030);
    ctx.drawTextWithShadow(textRenderer, Text.literal("Categories"), 35, 50, 0xFFFFFFFF);

    int y = 80;
    int categoriesCount = settingsManager.getCategories().size();
    if (selectedIndex >= categoriesCount) selectedIndex = 0;

    for (int i = 0; i < categoriesCount; i++) {
      boolean hovered = mouseX >= 30 && mouseX <= 140 && mouseY >= y - 2 && mouseY <= y + 12;
      int color = hovered ? 0xFFAAAAAA : 0xFFFFFFFF;
      Text text = Text.literal(settingsManager.getCategories().get(i).getName());
      if (i == selectedIndex) text = text.copy().formatted(Formatting.UNDERLINE);
      ctx.drawTextWithShadow(textRenderer, text, 35, y, color);
      y += 18;
    }

    // Rechte Box - Settings
    ctx.fill(180, 40, width - 20, height - 40, 0xFF1E1E1E);
    drawBorder(ctx, 180, 40, width - 20, height - 40, 0xFF303030);

    ctx.drawCenteredTextWithShadow(
        textRenderer,
        Text.literal("Settings: " + settingsManager.getCategories().get(selectedIndex).getName()),
        (width + 180) / 2,
        60,
        0xFFFFFFFF);

    int settingY = 90;
    for (Setting<?> setting : settingsManager.getCategories().get(selectedIndex).getSettings()) {
      setting.render(ctx, textRenderer, 200, settingY, mouseX, mouseY);
      settingY += 24;
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    int y = 80;
    int categoriesCount = settingsManager.getCategories().size();
    for (int i = 0; i < categoriesCount; i++) {
      if (mouseX >= 30 && mouseX <= 140 && mouseY >= y - 2 && mouseY <= y + 12) {
        selectedIndex = i;
        return true;
      }
      y += 18;
    }

    for (Setting<?> setting : settingsManager.getCategories().get(selectedIndex).getSettings()) {
      if (setting.mouseClicked(mouseX, mouseY)) return true;
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  private void drawBorder(DrawContext ctx, int x1, int y1, int x2, int y2, int color) {
    ctx.fill(x1, y1, x2, y1 + 1, color);
    ctx.fill(x1, y2 - 1, x2, y2, color);
    ctx.fill(x1, y1, x1 + 1, y2, color);
    ctx.fill(x2 - 1, y1, x2, y2, color);
  }
}
