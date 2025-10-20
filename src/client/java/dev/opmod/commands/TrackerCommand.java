package dev.opmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.opmod.TimeTracker;
import dev.opmod.misc.Prefix;
import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import dev.opmod.settings.ToggleSetting;
import java.util.Locale;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.MinecraftClient;

public class TrackerCommand {

  private final SettingsManager settingsManager;
  private final TimeTracker timeTracker;
  private final MinecraftClientAudiences adventure;

  private static final MiniMessage MM = MiniMessage.miniMessage();

  public TrackerCommand(SettingsManager settingsManager, TimeTracker timeTracker) {
    this.settingsManager = settingsManager;
    this.timeTracker = timeTracker;
    this.adventure = MinecraftClientAudiences.of();
  }

  public static void register(
      CommandDispatcher<FabricClientCommandSource> dispatcher, SettingsManager sm, TimeTracker tt) {

    TrackerCommand instance = new TrackerCommand(sm, tt);

    dispatcher.register(
        ClientCommandManager.literal("opmod")
            .then(
                ClientCommandManager.literal("tracker")
                    .then(ClientCommandManager.literal("reset").executes(instance::reset))
                    .then(ClientCommandManager.literal("start").executes(instance::start))
                    .then(ClientCommandManager.literal("stop").executes(instance::stop))
                    .then(ClientCommandManager.literal("status").executes(instance::status))
                    .then(
                        ClientCommandManager.literal("default").executes(instance::defaultSettings))
                    .then(
                        ClientCommandManager.literal("display")
                            .then(
                                ClientCommandManager.argument("option", StringArgumentType.string())
                                    .then(
                                        ClientCommandManager.argument(
                                                "value", BoolArgumentType.bool())
                                            .executes(instance::displaySetting))))));
  }

  private int reset(CommandContext<FabricClientCommandSource> context) {
    timeTracker.reset();
    send("<green>Tracker wurde zurückgesetzt.</green>");
    return 1;
  }

  private int start(CommandContext<FabricClientCommandSource> context) {
    timeTracker.start();
    send("<green>Tracking gestartet.</green>");
    return 1;
  }

  private int stop(CommandContext<FabricClientCommandSource> context) {
    timeTracker.pause();
    send("<yellow>Tracking pausiert.</yellow>");
    return 1;
  }

  private int status(CommandContext<FabricClientCommandSource> context) {
    send("<gray>Tracking Zeit:</gray> <aqua>" + timeTracker.getFormatted() + "</aqua>");
    return 1;
  }

  private int defaultSettings(CommandContext<FabricClientCommandSource> context) {
    settingsManager
        .getCategories()
        .forEach(
            category ->
                category
                    .getSettings()
                    .forEach(
                        setting -> {
                          if (setting instanceof ToggleSetting toggle) {
                            toggle.setValue(true);
                          }
                        }));
    send("<green>Standard-Layout aktiviert.</green>");
    return 1;
  }

  private int displaySetting(CommandContext<FabricClientCommandSource> context) {
    String option = context.getArgument("option", String.class).toLowerCase(Locale.ROOT);
    boolean value = BoolArgumentType.getBool(context, "value");

    String key = mapOptionToKey(option);
    if (key == null) {
      send("<red>Unbekannte Option:</red> " + option);
      return 0;
    }

    Setting<?> setting = settingsManager.getSettingByKey(key);
    if (setting == null) {
      send("<red>Setting nicht gefunden:</red> " + key);
      return 0;
    }

    if (setting instanceof ToggleSetting toggle) {
      toggle.setValue(value);
    } else {
      send("<red>Dieser Setting-Typ unterstützt boolean nicht.</red>");
      return 0;
    }

    send(
        "<gray>"
            + option
            + " wird nun</gray> "
            + (value ? "<green>eingeblendet</green>" : "<red>ausgeblendet</red>"));
    return 1;
  }

  private void send(String message) {
    Component msg = Prefix.get().append(MM.deserialize(message));
    if (MinecraftClient.getInstance().player != null) {
      adventure.audience().sendMessage(msg);
    }
  }

  private String mapOptionToKey(String option) {
    return switch (option) {
      case "job" -> "show_job";
      case "level" -> "show_level";
      case "progress" -> "show_progress";
      case "time" -> "show_tracking_time";
      case "timeleft" -> "show_time_until_level_up";
      case "money" -> "show_money";
      case "moneyph" -> "show_money_per_hour";
      case "xp" -> "show_xp";
      case "xpph" -> "show_xp_per_hour";
      case "yaw" -> "show_yaw";
      default -> null;
    };
  }
}
