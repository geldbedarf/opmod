package dev.opmod.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Prefix {
  private static final MiniMessage MM = MiniMessage.miniMessage();

  // [OPMOD »] mit Farbverlauf
  public static Component get() {
    Component core =
        MM.deserialize(
            "<gradient:#3494e6:#ec6ead><bold>OPMOD</bold></gradient> <dark_gray>»</dark_gray> ");
    return core;
  }
}
