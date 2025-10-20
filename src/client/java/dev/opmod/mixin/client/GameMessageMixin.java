package dev.opmod.mixin.client;

import dev.opmod.OPMODClient;
import dev.opmod.TimeTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class GameMessageMixin {

  @Inject(method = "onGameMessage", at = @At("HEAD"))
  private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
    if (OPMODClient.INSTANCE == null) return;

    Text content = packet.content();
    if (content == null) return;

    String message = content.getString();
    if (message.isBlank()) return;

    // Starte TimeTracker, wenn eine Job-Nachricht erkannt wurde
    if (message.contains("+") && message.contains("XP") && message.contains("$")) {
      TimeTracker tracker = OPMODClient.INSTANCE.getTimeTracker();
      if (!tracker.isRunning()) tracker.start();

      // An PayoutTracker weitergeben (Parsen erledigt dieser)
      OPMODClient.INSTANCE.getPayoutTracker().parseGameMessage(message);
    }
  }
}
