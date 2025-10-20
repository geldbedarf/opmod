package com.example.commands;

import com.example.config.ConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class InventoryWarningCommands {

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    dispatcher.register(
        ClientCommandManager.literal("InvWarning")
            // -----------------------------
            // /InvWarning enable <true|false>
            // -----------------------------
            .then(
                ClientCommandManager.literal("enable")
                    .then(
                        ClientCommandManager.argument("value", BoolArgumentType.bool())
                            .executes(
                                context -> {
                                  boolean value = BoolArgumentType.getBool(context, "value");
                                  ConfigManager.set("enableInvFullWarning", value);

                                  sendMessage("Inventory Warning", value);
                                  return 1;
                                })))
            // -----------------------------
            // /InvWarning interval <number>
            // -----------------------------
            .then(
                ClientCommandManager.literal("interval")
                    .then(
                        ClientCommandManager.argument("value", IntegerArgumentType.integer(1))
                            .executes(
                                context -> {
                                  int value = IntegerArgumentType.getInteger(context, "value");
                                  ConfigManager.set("InvFullWarningInterval", value);

                                  sendMessage("Inventory Warning Interval", value);
                                  return 1;
                                })))
            // -----------------------------
            // /InvWarning sound <true|false>
            // -----------------------------
            .then(
                ClientCommandManager.literal("sound")
                    .then(
                        ClientCommandManager.argument("value", BoolArgumentType.bool())
                            .executes(
                                context -> {
                                  boolean value = BoolArgumentType.getBool(context, "value");
                                  ConfigManager.set("enableInvFullWarningSound", value);

                                  sendMessage("Inventory Warning Sound", value);
                                  return 1;
                                })))
            // -----------------------------
            // /InvWarning reset
            // -----------------------------
            .then(
                ClientCommandManager.literal("reset")
                    .executes(
                        context -> {
                          ConfigManager.set("enableInvFullWarning", true);
                          ConfigManager.set("InvFullWarningInterval", 3);
                          ConfigManager.set("enableInvFullWarningSound", true);

                          sendMessage("Inventory Warning", true);
                          sendMessage("Inventory Warning Interval", 3);
                          sendMessage("Inventory Warning Sound", true);
                          return 1;
                        })));
  }

  private static void sendMessage(String option, boolean value) {
    Text text =
        Text.literal(
            "§b§lOPMOD §8» §b"
                + option
                + " §7ist nun "
                + (value ? "§aeingeschaltet" : "§causgeschaltet"));
    MinecraftClient.getInstance().player.sendMessage(text, false);
  }

  private static void sendMessage(String option, int value) {
    Text text =
        Text.literal(
            "§b§lOPMOD §8» §b"
                + option
                + " §7wurde auf §e"
                + value
                + (value == 1 ? " Minute" : " Minuten")
                + " §7gesetzt");
    MinecraftClient.getInstance().player.sendMessage(text, false);
  }
}
