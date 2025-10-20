package dev.opmod.settings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ToggleSetting extends Setting<Boolean> {

  private int x, y;

  public ToggleSetting(String key, String title, boolean defaultValue) {
    super(key, title, "", defaultValue); // Beschreibung nicht mehr genutzt
  }

  @Override
  public void render(DrawContext ctx, TextRenderer tr, int x, int y, int mouseX, int mouseY) {
    this.x = x;
    this.y = y;

    int boxSize = 12;
    int color = value ? 0xFF00CC66 : 0xFF555555;
    ctx.fill(x, y + 2, x + boxSize, y + boxSize + 2, color); // kleiner & schöner

    ctx.drawTextWithShadow(tr, Text.literal(title), x + 18, y + 4, 0xFFFFFFFF);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY) {
    if (mouseX >= x && mouseX <= x + 12 && mouseY >= y && mouseY <= y + 12) {
      setValue(!value);
      return true;
    }
    return false;
  }
}
