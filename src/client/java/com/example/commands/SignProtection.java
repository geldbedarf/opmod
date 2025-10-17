package com.example.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SignProtection {

  private static String pendingSignText = null;

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    LiteralCommandNode<FabricClientCommandSource> register =
        dispatcher.register(
            ClientCommandManager.literal("sign")
                // /sign <text>
                .then(
                    ClientCommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(
                            context -> {
                              String text = StringArgumentType.getString(context, "text");

                              // Puffer füllen, nichts an Server senden
                              pendingSignText = text;

                              // Spieler informieren
                              MinecraftClient.getInstance()
                                  .player
                                  .sendMessage(
                                      Text.literal(
                                          "§aText gespeichert!§7 Tippe §b/sign confirm§7 um zu senden."),
                                      false);
                              return 1;
                            }))

                // /sign confirm
                .then(
                    ClientCommandManager.literal("confirm")
                        .executes(
                            context -> {
                              if (pendingSignText == null) {
                                MinecraftClient.getInstance()
                                    .player
                                    .sendMessage(
                                        Text.literal("Kein Text zum Senden vorhanden!"), false);
                              } else {
                                // Das echte Server-Command senden
                                MinecraftClient.getInstance()
                                    .player
                                    .networkHandler
                                    .sendChatMessage("/sign " + pendingSignText);

                                System.out.println("sign " + pendingSignText);

                                // Puffer leeren
                                pendingSignText = null;

                                // Bestätigung lokal
                                MinecraftClient.getInstance()
                                    .player
                                    .sendMessage(Text.literal("Text an Server gesendet!"), false);
                              }

                              return 1;
                            })));
  }
  ;
}
;
