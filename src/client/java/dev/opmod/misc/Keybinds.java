package dev.opmod.misc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds implements ClientModInitializer {
  public static KeyBinding openKey;

  @Override
  public void onInitializeClient() {
    String category = "key.category.opmod.general";

    openKey =
        KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.opmod.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L, category));
  }
}
