package com.example.features;

import com.example.config.ConfigManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class OffhandBlocker {

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
    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          if (isBlocked(hand)) return ActionResult.FAIL;
          return ActionResult.PASS;
        });
  }

  private boolean isBlocked(Hand hand) {
    boolean enabled = ConfigManager.get().enableOffhandBlocker;
    return enabled && hand == Hand.OFF_HAND;
  }
}
