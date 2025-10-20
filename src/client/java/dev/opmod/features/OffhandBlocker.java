package dev.opmod.features;

import dev.opmod.config.ConfigManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class OffhandBlocker {

  private long lastMessageTime = 0; // Timestamp der letzten Nachricht
  private static final long COOLDOWN_MS = 1000; // 1 Sekunde Cooldown

  public void register() {

    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          if (isBlocked(hand)) {
            sendCooldownMessage();
            return ActionResult.FAIL;
          }
          return ActionResult.PASS;
        });

    UseItemCallback.EVENT.register(
        (player, world, hand) -> {
          if (isBlocked(hand)) {
            sendCooldownMessage();
            return ActionResult.FAIL;
          }

          return ActionResult.PASS;
        });
    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          if (isBlocked(hand)) {
            sendCooldownMessage();
            return ActionResult.FAIL;
          }
          return ActionResult.PASS;
        });
  }

  private boolean isBlocked(Hand hand) {
    boolean enabled = ConfigManager.get().enableOffhandBlocker;
    return enabled && hand == Hand.OFF_HAND;
  }

  private void sendCooldownMessage() {
    long now = System.currentTimeMillis();
    if (now - lastMessageTime < COOLDOWN_MS) return; // noch im Cooldown

    ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player != null) {
      player.sendMessage(Text.literal("§b§lOPMOD §8» §cOffhand blockiert!"), false);
    }
    lastMessageTime = now;
  }
}
