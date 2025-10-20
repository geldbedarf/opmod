/*
 * Copyright (c) 2025 Geldbedarf & CalledCracki
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package com.example;

import com.example.commands.InventoryWarningCommands;
import com.example.commands.OffhandBlockerCommands;
import com.example.commands.TrackerCommand;
import com.example.features.InventoryFullWarning;
import com.example.features.OffhandBlocker;
import com.example.misc.PresenceBuilder;
import com.example.ui.HUDOverlay;
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
