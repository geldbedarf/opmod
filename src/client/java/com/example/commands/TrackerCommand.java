package com.example.commands;

import com.example.TimeTracker;
import com.example.config.ConfigManager;
import com.example.jobsystem.tracking.JobData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TrackerCommand {

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    LiteralCommandNode<FabricClientCommandSource> register =
        dispatcher.register(
            ClientCommandManager.literal("opmod")
                .then(
                    ClientCommandManager.literal("tracker")

                        // -----------------------------
                        // /tracker reset
                        // -----------------------------
                        .then(
                            ClientCommandManager.literal("reset")
                                .executes(
                                    context -> {
                                      TimeTracker.reset();
                                      JobData.jobName = "Unbekannt";
                                      JobData.xp = 0;
                                      JobData.money = 0;
                                      JobData.level = 0;
                                      JobData.percent = 0;

                                      // Nachricht an Spieler senden
                                      Text text =
                                          Text.literal(
                                              "§b§lOPMOD §8» §aJob-Tracker wurde erfolgreich zurückgesetzt.");
                                      MinecraftClient.getInstance().player.sendMessage(text, false);

                                      return 1;
                                    }))

                        // -----------------------------
                        // /tracker display <option> <true|false>
                        // -----------------------------
                        .then(
                            ClientCommandManager.literal("display")
                                .then(
                                    ClientCommandManager.literal("DEFAULT")
                                        .executes(
                                            context -> {
                                              display(context, "Job", true);
                                              display(context, "Level", true);
                                              display(context, "Fortschritt", true);
                                              display(context, "TrackingZeit", true);
                                              display(context, "ZeitBisLevelUp", false);
                                              display(context, "Geld", true);
                                              display(context, "GeldProStunde", true);
                                              display(context, "Geld", true);
                                              display(context, "GeldProStunde", true);
                                              display(context, "Yaw", false);
                                              return 1;
                                            }))
                                .then(
                                    ClientCommandManager.literal("Job")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "Job",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("Level")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "Level",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("Fortschritt")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "Fortschritt",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("TrackingZeit")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "TrackingZeit",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("ZeitBisLevelUp")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "ZeitBisLevelUp",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("Geld")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "Geld",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("GeldProStunde")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "GeldProStunde",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("XP")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "XP",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("XPProStunde")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "XPProStunde",
                                                            BoolArgumentType.getBool(
                                                                context, "value")))))
                                .then(
                                    ClientCommandManager.literal("Yaw")
                                        .then(
                                            ClientCommandManager.argument(
                                                    "value", BoolArgumentType.bool())
                                                .executes(
                                                    context ->
                                                        display(
                                                            context,
                                                            "Yaw",
                                                            BoolArgumentType.getBool(
                                                                context, "value"))))))

                        // -----------------------------
                        // /tracker enable <on|off>
                        // -----------------------------
                        .then(
                            ClientCommandManager.literal("enable")
                                .then(
                                    ClientCommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(
                                            context -> {
                                              boolean value =
                                                  BoolArgumentType.getBool(context, "value");
                                              ConfigManager.set("showHUD", value);

                                              // Nachricht an Spieler senden
                                              Text text =
                                                  Text.literal(
                                                      "§b§lOPMOD §8» §bJob-Tracker §7wird nun "
                                                          + (value
                                                              ? "§aeingeblendet"
                                                              : "§causgeblendet"));
                                              MinecraftClient.getInstance()
                                                  .player
                                                  .sendMessage(text, false);

                                              return 1;
                                            })))));
  }

  private static int display(
      com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> context,
      String option,
      boolean value) {
    switch (option) {
      case "Job" -> ConfigManager.set("showJob", value);
      case "Level" -> ConfigManager.set("showLevel", value);
      case "Fortschritt" -> ConfigManager.set("showFortschritt", value);
      case "TrackingZeit" -> ConfigManager.set("showTrackingZeit", value);
      case "ZeitBisLevelUp" -> ConfigManager.set("showTimeUntilLevelUp", value);
      case "Geld" -> ConfigManager.set("showGeld", value);
      case "GeldProStunde" -> ConfigManager.set("showGeldProStunde", value);
      case "XP" -> ConfigManager.set("showXP", value);
      case "XPProStunde" -> ConfigManager.set("showXPProStunde", value);
      case "Yaw" -> ConfigManager.set("Yaw", value);
    }

    // Nachricht an Spieler senden
    Text text =
        Text.literal(
            "§b§lOPMOD §8» §b"
                + option
                + " §7wird nun "
                + (value ? "§aeingeblendet" : "§causgeblendet"));
    MinecraftClient.getInstance().player.sendMessage(text, false);

    return 1;
  }
}
