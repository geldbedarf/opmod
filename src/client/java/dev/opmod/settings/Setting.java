package dev.opmod.settings;

import java.util.function.Consumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class Setting<T> {
  protected final String key;
  protected final String title;
  protected final String description;
  protected T value;
  private Consumer<T> onChange;

  public Setting(String key, String title, String description, T defaultValue) {
    this.key = key;
    this.title = title;
    this.description = description;
    this.value = defaultValue;
  }

  public abstract void render(
      DrawContext ctx, TextRenderer tr, int x, int y, int mouseX, int mouseY);

  public abstract boolean mouseClicked(double mouseX, double mouseY);

  public String getKey() {
    return key;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T newValue) {
    this.value = newValue;
    if (onChange != null) onChange.accept(newValue);
  }

  public void setOnChange(Consumer<T> onChange) {
    this.onChange = onChange;
  }
}
