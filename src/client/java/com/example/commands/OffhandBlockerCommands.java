package com.example.commands;

import com.example.config.ConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class OffhandBlockerCommands {

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    dispatcher.register(
        ClientCommandManager.literal("opmod")
            .then(
                ClientCommandManager.literal("OffhandBlocker")
                    // -----------------------------
                    // /opmod InvFullWarning enable <true|false>
                    // -----------------------------
                    .then(
                        ClientCommandManager.literal("enable")
                            .then(
                                ClientCommandManager.argument("value", BoolArgumentType.bool())
                                    .executes(
                                        context -> {
                                          boolean value =
                                              BoolArgumentType.getBool(context, "value");
                                          ConfigManager.set("enableOffhandBlocker", value);

                                          sendMessage("Offhand Blocker", value);
                                          return 1;
                                        })))));
  }
  ;

  private static void sendMessage(String option, boolean value) {
    Text text =
        Text.literal(
            "§b§lOPMOD §8» §b"
                + option
                + " §7ist nun "
                + (value ? "§aeingeschaltet" : "§causgeschaltet"));
    MinecraftClient.getInstance().player.sendMessage(text, false);
  }
}
