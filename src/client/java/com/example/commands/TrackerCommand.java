package com.example.commands;

import com.example.TimeTracker;
import com.example.config.ConfigManager;
import com.example.jobsystem.tracking.JobData;
import com.example.misc.Prefix;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;

import java.io.ObjectInputFilter;

public class TrackerCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralCommandNode<FabricClientCommandSource> register = dispatcher.register(
                ClientCommandManager.literal("tracker")

                        // -----------------------------
                        // /tracker reset
                        // -----------------------------
                        .then(ClientCommandManager.literal("reset")
                                .executes(context -> {
                                    TimeTracker.reset();
                                    JobData.jobName = "Unbekannt";
                                    JobData.xp = 0;
                                    JobData.money = 0;
                                    JobData.level = 0;
                                    JobData.percent = 0;

                                    Component adventureMsg = Prefix.get().append(
                                            MiniMessage.miniMessage().deserialize("<green>Tracker wurde zurückgesetzt</green>")
                                    );

                                    // In Minecraft Text umwandeln
                                    Text mcText = MinecraftClientAudiences.of().asNative(adventureMsg);

                                    // Nachricht an Spieler senden
                                    MinecraftClient.getInstance().player.sendMessage(mcText, false);

                                    return 1;
                                })
                        )

                        // -----------------------------
                        // /tracker display <option> <true|false>
                        // -----------------------------
                        .then(ClientCommandManager.literal("display")
                                .then(ClientCommandManager.literal("Job")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "Job", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("Level")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "Level", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("Fortschritt")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "Fortschritt", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("TrackingZeit")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "TrackingZeit", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("ZeitBisLevelUp")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "ZeitBisLevelUp", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("Geld")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "Geld", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("GeldProStunde")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "GeldProStunde", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("XP")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "XP", BoolArgumentType.getBool(context, "value")))))
                                .then(ClientCommandManager.literal("XPProStunde")
                                        .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                                .executes(context -> display(context, "XPProStunde", BoolArgumentType.getBool(context, "value")))))
                        )

                        // -----------------------------
                        // /tracker showHUD <on|off>
                        // -----------------------------
                        .then(ClientCommandManager.literal("showHUD")
                                .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean value = BoolArgumentType.getBool(context, "value");
                                            ConfigManager.get().showHUD = value;

                                            Component adventureMsg = Prefix.get().append(
                                                    MiniMessage.miniMessage().deserialize(
                                                            "<gray>Tracker HUD wird nun </gray>" +
                                                                    (value ? "<green>eingeblendet</green>" : "<red>ausgeblendet</red>")
                                                    )
                                            );

                                            // In Minecraft Text umwandeln
                                            Text mcText = MinecraftClientAudiences.of().asNative(adventureMsg);

                                            // Nachricht an Spieler senden
                                            MinecraftClient.getInstance().player.sendMessage(mcText, false);

                                            return 1;
                                        })
                                )
                        )


        );
    }

    private static int display(com.mojang.brigadier.context.CommandContext<FabricClientCommandSource> context, String option, boolean value) {
        switch (option) {
            case "Job" -> ConfigManager.get().showJob = value;
            case "Level" -> ConfigManager.get().showLevel = value;
            case "Fortschritt" -> ConfigManager.get().showFortschritt = value;
            case "TrackingZeit" -> ConfigManager.get().showTrackingZeit = value;
            case "ZeitBisLevelUp" -> ConfigManager.get().showTimeUntilLevelUp = value;
            case "Geld" -> ConfigManager.get().showGeld = value;
            case "GeldProStunde" -> ConfigManager.get().showGeldProStunde = value;
            case "XP" -> ConfigManager.get().showXP = value;
            case "XPProStunde" -> ConfigManager.get().showXPProStunde = value;
        }

        ConfigManager.save();

        Component adventureMsg = Prefix.get().append(
                MiniMessage.miniMessage().deserialize(
                        "<gray>" + option + " wird nun </gray>" + (value ? "<green>eingeblendet</green>" : "<red>ausgeblendet</red>")
                )
        );

        // In Minecraft Text umwandeln
        Text mcText = MinecraftClientAudiences.of().asNative(adventureMsg);

        // Nachricht an Spieler senden
        MinecraftClient.getInstance().player.sendMessage(mcText, false);

        return 1;
    }
}
