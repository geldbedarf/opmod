package dev.opmod.features;

import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class InventoryFullWarning {

  private final SettingsManager settings;
  private long lastWarningTime = 0;
  private static final long COOLDOWN_MS = 3 * 60 * 1000; // 3 Minuten

  public InventoryFullWarning(SettingsManager settings) {
    this.settings = settings;
  }

  public void tick() {
    if (!isEnabled()) return;
    if (!isCooldownOver()) return;

    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc == null || mc.player == null) return;

    if (isInventoryNowFull(mc)) {
      warn();
    }
  }

  private boolean isEnabled() {
    Setting<?> s = settings.getSettingByKey("inventory_full_warning");
    return s != null && Boolean.TRUE.equals(s.getValue());
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

    for (ItemStack stack : inv.main) {
      if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
        return false;
      }
    }

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
