package dev.opmod.ui;

import java.util.function.Supplier;
import net.minecraft.text.Text;

public class HUDElement {
  private final String settingKey;
  private final Supplier<Text> content;

  public HUDElement(String settingKey, Supplier<Text> content) {
    this.settingKey = settingKey;
    this.content = content;
  }

  public String key() {
    return settingKey;
  }

  public Supplier<Text> content() {
    return content;
  }
}
