package com.example.features;

import com.example.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class InventoryFullWarning {

  private long lastWarningTime = 0;
  private static final long COOLDOWN_MS =
      ConfigManager.get().InvFullWarningInterval
          * 60
          * 1000; // Value Minute(n) - *60*1000 formt die Minutenanzahl in ms um.

  public void tick() {
    if (!isEnabled()) return;
    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc == null || mc.player == null) return;
    // Resetet den Cooldown sobald aus dem Inventar ein Slot frei wird. Jedoch nur wenn mindestens
    // 10 Sekunden nach der letzten Warnung vergangen sind, damit nicht ständig der Sound kommt wenn
    // man was im Inventar sortiert.
    if (!isInventoryNowFull(mc)
        && !isCooldownOver()
        && (System.currentTimeMillis() - lastWarningTime) / 1000 > 10) {
      System.out.println((System.currentTimeMillis() - lastWarningTime) / 1000);
      lastWarningTime = 0;
    }
    if (!isCooldownOver()) return;

    if (isInventoryNowFull(mc)) {
      warn();
    }
  }

  private boolean isEnabled() {
    return ConfigManager.get().enableInvFullWarning;
  }

  private boolean isCooldownOver() {
    return System.currentTimeMillis() - lastWarningTime > COOLDOWN_MS;
  }

  private boolean isInventoryNowFull(MinecraftClient mc) {
    assert mc.player != null;
    var inv = mc.player.getInventory();

    if (inv.getEmptySlot() != -1) {
      return false;
    }

    //    for (ItemStack stack : inv.main) {
    //      if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
    //        return false;
    //      }
    //    }

    return true;
  }

  private void warn() {
    lastWarningTime = System.currentTimeMillis();

    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc == null || mc.player == null) return;

    mc.inGameHud.setTitleTicks(0, 40, 0);
    mc.inGameHud.setTitle(Text.of("§cInventar voll!"));

    mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0f, 1.2f);
  }

  public void resetCooldown() {
    lastWarningTime = 0;
  }
}
