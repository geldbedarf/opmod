package dev.opmod.mixin.client;

import dev.opmod.OPMODClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class SignCommandMixin {

  @Inject(method = "sendChatCommand(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
  private void onSendChatCommand(String command, CallbackInfo ci) {
    if (OPMODClient.INSTANCE != null && OPMODClient.INSTANCE.getSignConfirmProtect() != null) {

      boolean block = OPMODClient.INSTANCE.getSignConfirmProtect().interceptMessage("/" + command);
      if (block) ci.cancel();
    }
  }
}
