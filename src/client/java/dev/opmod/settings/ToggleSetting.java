package dev.opmod.settings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ToggleSetting extends Setting {
  private boolean enabled;
  private int x, y;

  public ToggleSetting(String name, String description, boolean defaultState) {
    super(name, description);
    this.enabled = defaultState;
  }

  @Override
  public void render(DrawContext ctx, TextRenderer tr, int x, int y, int mouseX, int mouseY) {
    this.x = x;
    this.y = y;

    int buttonX = x;
    int buttonY = y;
    int buttonSize = 16;
    int buttonColor = enabled ? 0xFF00CC66 : 0xFF555555;
    ctx.fill(buttonX, buttonY, buttonX + buttonSize, buttonY + buttonSize, buttonColor);

    int titleX = buttonX + buttonSize + 10;
    ctx.drawTextWithShadow(tr, Text.literal(name), titleX, y + 4, 0xFFFFFFFF);

    int descX = titleX + tr.getWidth(name) + 12;
    ctx.drawTextWithShadow(tr, Text.literal(description), descX, y + 4, 0xFFAAAAAA);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY) {
    int buttonX = x;
    int buttonY = y;
    int buttonSize = 16;

    if (mouseX >= buttonX
        && mouseX <= buttonX + buttonSize
        && mouseY >= buttonY
        && mouseY <= buttonY + buttonSize) {
      enabled = !enabled;
      return true;
    }
    return false;
  }

  @Override
  public Object getValue() {
    return enabled;
  }

  @Override
  public void setValue(Object value) {
    this.enabled = (Boolean) value;
  }
}
