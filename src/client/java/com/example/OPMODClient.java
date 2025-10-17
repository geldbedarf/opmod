/*
 * Copyright (c) 2025 Geldbedarf & CalledCracki
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package com.example;

import com.example.commands.SignProtection;
import com.example.commands.TrackerCommand;
import com.example.ui.HUDOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class OPMODClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    HUDOverlay.init();

    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> {
          TrackerCommand.register(dispatcher);
        });
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> {
          SignProtection.register(dispatcher);
        });

    System.out.println("OPMOD loaded");
  }
}
