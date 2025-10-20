package dev.opmod.features;

import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class OffhandBlocker {

  private final SettingsManager settings;

  public OffhandBlocker(SettingsManager settings) {
    this.settings = settings;
  }

  public void register() {
    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          if (isBlocked(hand)) return ActionResult.FAIL;
          return ActionResult.PASS;
        });

    UseItemCallback.EVENT.register(
        (player, world, hand) -> {
          if (isBlocked(hand)) return ActionResult.FAIL;
          return ActionResult.PASS;
        });
  }

  private boolean isBlocked(Hand hand) {
    Setting<?> setting = settings.getSettingByKey("disable_offhand_use");
    boolean enabled = setting != null && Boolean.TRUE.equals(setting.getValue());
    return enabled && hand == Hand.OFF_HAND;
  }
}
