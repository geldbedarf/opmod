package dev.opmod.features.performance;

import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

/*
 * Reduces view distance to 2 after 2 minutes of inactivity and restores on any movement or mouse look; paused while any screen is open.
 */
public class AutoIdleRenderFeature {

  private static final int IDLE_TICKS_THRESHOLD = 1000;
  private static final int LOW_RENDER_DISTANCE = 2;

  private final SettingsManager settings;
  private Vec3d lastPos = null;
  private float lastYaw = 0;
  private float lastPitch = 0;
  private int idleTicks = 0;
  private boolean reduced = false;
  private int originalViewDistance = -1;

  public AutoIdleRenderFeature(SettingsManager settings) {
    this.settings = settings;
  }

  public void register() {
    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (client == null || client.player == null) return;

          Setting<?> s = settings.getSettingByKey("auto_idle_render");
          boolean enabled = s != null && Boolean.TRUE.equals(s.getValue());

          if (!enabled) {
            if (reduced) restore(client);
            resetTracking(client);
            return;
          }

          if (client.currentScreen != null) {
            if (reduced) restore(client);
            resetTracking(client);
            return;
          }

          Vec3d pos = client.player.getPos();
          float yaw = client.player.getYaw();
          float pitch = client.player.getPitch();

          boolean moved = lastPos != null && pos.squaredDistanceTo(lastPos) > 0.0001;
          boolean rotated = yaw != lastYaw || pitch != lastPitch;

          if (moved || rotated) {
            if (reduced) restore(client);
            resetTracking(client);
            return;
          }

          idleTicks++;
          if (!reduced && idleTicks >= IDLE_TICKS_THRESHOLD) {
            reduce(client);
          }
        });
  }

  private void resetTracking(MinecraftClient client) {
    idleTicks = 0;
    if (client.player != null) {
      lastPos = client.player.getPos();
      lastYaw = client.player.getYaw();
      lastPitch = client.player.getPitch();
    }
  }

  private void reduce(MinecraftClient client) {
    if (client.options.getViewDistance() == null) return;
    originalViewDistance = client.options.getViewDistance().getValue();
    client.options.getViewDistance().setValue(LOW_RENDER_DISTANCE);
    reduced = true;
  }

  private void restore(MinecraftClient client) {
    if (client.options.getViewDistance() == null) return;
    if (originalViewDistance > 0) {
      client.options.getViewDistance().setValue(originalViewDistance);
    }
    reduced = false;
    originalViewDistance = -1;
  }
}
