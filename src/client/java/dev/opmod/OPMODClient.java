package dev.opmod;

import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import dev.opmod.commands.TrackerCommand;
import dev.opmod.config.ConfigManager;
import dev.opmod.features.InventoryFullWarning;
import dev.opmod.features.OffhandBlocker;
import dev.opmod.features.SignConfirmProtect;
import dev.opmod.features.performance.AutoIdleRenderFeature;
import dev.opmod.jobsystem.tracking.JobData;
import dev.opmod.jobsystem.tracking.PayoutTracker;
import dev.opmod.misc.ModInfo;
import dev.opmod.misc.PresenceBuilder;
import dev.opmod.settings.SettingsManager;
import dev.opmod.ui.HUDOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OPMODClient implements ClientModInitializer {

  public static final Logger LOGGER = LogManager.getLogger("OPMOD");
  public static OPMODClient INSTANCE;
  private SettingsManager settingsManager;
  private TimeTracker timeTracker;
  private PayoutTracker payoutTracker;
  private JobData jobData;
  private InventoryFullWarning inventoryFullWarning;
  private SignConfirmProtect signConfirmProtect;
  private OffhandBlocker offhandBlocker;
  private AutoIdleRenderFeature autoIdleRenderFeature;

  @Override
  public void onInitializeClient() {
    INSTANCE = this;

    LOGGER.info("Loading {} v{}", ModInfo.getName(), ModInfo.getVersion());

    initManagers();
    registerEvents();
    registerCommands();

    try {
      PresenceBuilder.start();
    } catch (NoDiscordClientException e) {
      throw new RuntimeException(e);
    }

    // ModInfo.checkForUpdatesAsync();

    LOGGER.info("{} initialized successfully ✅", ModInfo.getName());
  }

  private void initManagers() {
    ConfigManager configManager = new ConfigManager();
    settingsManager = new SettingsManager(configManager);
    timeTracker = new TimeTracker();
    timeTracker.start();
    jobData = new JobData();
    payoutTracker = new PayoutTracker(jobData);
    inventoryFullWarning = new InventoryFullWarning(settingsManager);
    signConfirmProtect = new SignConfirmProtect(settingsManager);
    offhandBlocker = new OffhandBlocker(settingsManager);
    autoIdleRenderFeature = new AutoIdleRenderFeature(settingsManager);
    autoIdleRenderFeature.register();
    offhandBlocker.register();

    HUDOverlay hudOverlay = new HUDOverlay(configManager, settingsManager, timeTracker, jobData);
  }

  private void registerEvents() {
    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (client.player != null) {
            if (inventoryFullWarning != null) {
              inventoryFullWarning.tick();
            }
          }
        });
  }

  private void registerCommands() {
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) ->
            TrackerCommand.register(dispatcher, settingsManager, timeTracker));
  }

  public PayoutTracker getPayoutTracker() {
    return payoutTracker;
  }

  public JobData getJobData() {
    return jobData;
  }

  public TimeTracker getTimeTracker() {
    return timeTracker;
  }

  public SettingsManager getSettingsManager() {
    return settingsManager;
  }

  public SignConfirmProtect getSignConfirmProtect() {
    return signConfirmProtect;
  }

  public OffhandBlocker getOffhandBlocker() {
    return offhandBlocker;
  }

  public MinecraftClient getClient() {
    return MinecraftClient.getInstance();
  }
}
