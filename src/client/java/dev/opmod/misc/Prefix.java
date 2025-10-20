package dev.opmod.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Prefix {
  private static final MiniMessage MM = MiniMessage.miniMessage();

  public static Component get() {
    return MM.deserialize(
        "<gradient:#3494e6:#ec6ead><bold>OPMOD</bold></gradient> <gray>»</gray> ");
  }
}
