/*
 * Copyright (c) 2025 Geldbedarf & CalledCracki
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package com.example;

import com.example.commands.TrackerCommand;
import com.example.misc.PresenceBuilder;
import com.example.ui.HUDOverlay;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class OPMODClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        try {
            PresenceBuilder.start();
        } catch (NoDiscordClientException e) {
            throw new RuntimeException(e);
        }

        HUDOverlay.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TrackerCommand.register(dispatcher);
        });

        System.out.println("OPMOD loaded");
    }
}
