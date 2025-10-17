package com.example.mixin.client;

import com.example.TimeTracker;
import com.example.jobsystem.tracking.JobData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class GameMessageMixin {

  @Shadow private @Nullable SyncedClientOptions syncedOptions;

  @Inject(method = "onGameMessage", at = @At("HEAD"))
  private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
    Text content = packet.content();

    // Den Text in String umwandeln
    String message = content.getString();

    // Nur Job-Pakete verarbeiten
    if (message.contains("Level") && message.contains("|") && message.contains("$")) {

      TimeTracker.start();

      List<String> numbers = new ArrayList<>();
      StringBuilder current = new StringBuilder();

      for (int i = 0; i < message.length(); i++) {
        char c = message.charAt(i);
        if ((c >= '0' && c <= '9') || c == '.' || c == ',') {
          current.append(c);
        } else if (current.length() > 0) {
          numbers.add(current.toString());
          current.setLength(0);
        }
      }
      if (current.length() > 0) numbers.add(current.toString());

      // Beispiel-Zuordnung:
      if (numbers.size() >= 4) {
        double newXp = Double.parseDouble(numbers.get(0).replace(',', '.'));
        double newMoney = Double.parseDouble(numbers.get(1).replace(',', '.'));
        int newLevel = (int) Double.parseDouble(numbers.get(2));
        double newPercent = Double.parseDouble(numbers.get(3).replace(',', '.'));

        // Addiere die Hälfte von XP und Geld auf den bisherigen Wert:
        JobData.xp += (newXp / 2);
        JobData.money += (newMoney / 2);

        // Ersetze Level und Prozent direkt:
        JobData.level = newLevel;
        JobData.percent = newPercent;
      }

      switch (message) {
        case "Holzfäller" -> JobData.jobName = "Holzfäller";
        case "Miner" -> JobData.jobName = "Miner";
        case "Fischer" -> JobData.jobName = "Fischer";
        case "Gräber" -> JobData.jobName = "Gräber";
        case "Jäger" -> JobData.jobName = "Jäger";
        case "Builder" -> JobData.jobName = "Builder";
        case "Farmer" -> JobData.jobName = "Farmer";
        default -> JobData.jobName = "Unbekannt";
      }
    }
  }
}
