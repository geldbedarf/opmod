/*
 * Copyright (c) 2025 Geldbedarf & CalledCracki
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package dev.opmod;

import dev.opmod.commands.InventoryWarningCommands;
import dev.opmod.commands.OffhandBlockerCommands;
import dev.opmod.commands.TrackerCommand;
import dev.opmod.features.InventoryFullWarning;
import dev.opmod.features.OffhandBlocker;
import dev.opmod.misc.PresenceBuilder;
import dev.opmod.ui.HUDOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class OPMODClient implements ClientModInitializer {

  // Beispiel: Eine Referenz auf dein Warnsystem
  private InventoryFullWarning inventoryFullWarning;

  @Override
  public void onInitializeClient() {
    HUDOverlay.init();

    // OffhandBlocker instanziieren
    OffhandBlocker offhandBlocker = new OffhandBlocker();
    offhandBlocker.register();

    // Commands registrieren
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> {
          TrackerCommand.register(dispatcher);
        });
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> {
          InventoryWarningCommands.register(dispatcher);
        });
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> {
          OffhandBlockerCommands.register(dispatcher);
        });

    PresenceBuilder.start();

    // InventoryFullWarning instanziieren
    this.inventoryFullWarning = new InventoryFullWarning();

    // Events registrieren
    registerEvents();

    System.out.println("OPMOD loaded");
  }

  private void registerEvents() {
    ClientTickEvents.END_CLIENT_TICK.register(
        client -> {
          if (client.player != null) {
            if (inventoryFullWarning != null) {
              inventoryFullWarning.tick();
            }
          }
        });
  }
}
