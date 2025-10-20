/* Injects "Chat" and "Settings" buttons into the Minecraft pause menu (GameMenuScreen) */
package dev.opmod.mixin.client;

import dev.opmod.ui.screens.SettingsScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

  protected GameMenuScreenMixin(Text title) {
    super(title);
  }

  @Inject(method = "init", at = @At("TAIL"))
  private void addCustomButtons(CallbackInfo ci) {
    int buttonWidth = 60;
    int buttonHeight = 20;
    int spacing = 5;

    this.addDrawableChild(
        ButtonWidget.builder(
                Text.literal("Settings"),
                button -> {
                  assert this.client != null;
                  this.client.setScreen(new SettingsScreen());
                })
            .dimensions(5 + buttonWidth + spacing, 5, buttonWidth, buttonHeight)
            .build());
  }
}
