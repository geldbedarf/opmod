package com.example;

import com.example.commands.TrackerCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class OPMODClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HUDOverlay.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            TrackerCommand.register(dispatcher);
        });

        System.out.println("OPMOD loaded");
    }
}
