package dev.opmod.features;

import dev.opmod.misc.Prefix;
import dev.opmod.settings.Setting;
import dev.opmod.settings.SettingsManager;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.MinecraftClient;

public class SignConfirmProtect {

  private final SettingsManager settings;
  private String pendingCommand = null;
  private long pendingUntil = 0;

  private static final long TIMEOUT_MS = 20_000; // 20 Sekunden
  private static final MiniMessage MM = MiniMessage.miniMessage();

  public SignConfirmProtect(SettingsManager settings) {
    this.settings = settings;
  }

  /** Fängt /sign ab und erzwingt /sign confirm */
  public boolean interceptMessage(String message) {
    if (!isEnabled() || message == null) return false;

    // Nur /sign-Kommandos abfangen
    if (message.equalsIgnoreCase("/sign confirm")) {
      return handleConfirm();
    }

    if (message.startsWith("/sign")) {
      startPending(message); // speichert komplettes Originalkommando
      warnUser(message);
      return true; // blockt, bis bestätigt
    }

    return false;
  }

  private boolean handleConfirm() {
    if (pendingCommand == null || System.currentTimeMillis() > pendingUntil) {
      sendMsg("<red>❌ Keine offene Signierung zu bestätigen.</red>");
      reset();
      return true; // block /sign confirm
    }

    // Original /sign ausführen
    sendSignCommand(pendingCommand);
    sendMsg("<green>✔ Deine Signatur wurde hinzugefügt.</green>");
    reset();
    return true;
  }

  private void startPending(String fullCommand) {
    this.pendingCommand = fullCommand; // inklusive /sign exakt wie eingegeben
    this.pendingUntil = System.currentTimeMillis() + TIMEOUT_MS;
  }

  private void warnUser(String original) {
    sendMsg(
        "<red>[ACHTUNG]</red> <gray>Du hast <yellow>" + original + "</yellow> eingegeben.</gray>");
    sendMsg(
        "<white>Bestätige mit <yellow>/sign confirm</yellow>. <gray>(20 Sekunden)</gray></white>");
  }

  private void sendSignCommand(String fullCommand) {
    MinecraftClient.getInstance().player.networkHandler.sendCommand(fullCommand.substring(1));
  }

  private void sendMsg(String mini) {
    var player = MinecraftClient.getInstance().player;
    if (player == null) return;

    MinecraftClientAudiences.of().audience().sendMessage(Prefix.get().append(MM.deserialize(mini)));
  }

  private boolean isEnabled() {
    Setting<?> s = settings.getSettingByKey("confirm_sign_command");
    return s != null && Boolean.TRUE.equals(s.getValue());
  }

  private void reset() {
    pendingCommand = null;
    pendingUntil = 0;
  }
}
