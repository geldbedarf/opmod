/* Manages Discord Rich Presence connection and updates for OPMod, including automatic state changes based on game context */
package com.example.misc;

import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.ActivityType;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;

public final class PresenceBuilder {
    private static IPCClient client;
    private static final long APP_ID = 1428151805879259156L;
    private static String currentState = "";
    private static String currentDetails = "";
    private static boolean connected = false;

    public static void start() throws NoDiscordClientException {
        if (client != null) return;
        client = new IPCClient(APP_ID);
        client.setListener(new IPCListener() {
            @Override
            public void onPacketSent(IPCClient client, Packet packet) {}

            @Override
            public void onPacketReceived(IPCClient client, Packet packet) {}

            @Override
            public void onActivityJoin(IPCClient client, String secret) {}

            @Override
            public void onActivitySpectate(IPCClient client, String secret) {}

            @Override
            public void onActivityJoinRequest(IPCClient client, String secret, User user) {}

            @Override
            public void onReady(IPCClient client) {
                connected = true;
                System.out.println("[OPMod] Discord RPC connected successfully!");
                update("Im Menü", "Bereit zum Spielen");
            }

            @Override
            public void onClose(IPCClient client, JsonObject json) {
                connected = false;
                System.err.println("[OPMod] Discord RPC closed: " + json);
            }

            @Override
            public void onDisconnect(IPCClient client, Throwable t) {
                connected = false;
                System.err.println("[OPMod] Discord RPC disconnected: " + (t != null ? t.getMessage() : "null"));
            }
        });
        client.connect();
        System.out.println("[OPMod] Attempting Discord RPC connection...");

        ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> stop());

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (!connected) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.player == null) {
                if (!currentState.equals("Im Menü")) {
                    update("Im Menü", "Bereit zum Spielen");
                }
                return;
            }

            ClientWorld world = client.world;
            if (world == null) {
                if (!currentState.equals("Im Menü")) {
                    update("Im Menü", "Bereit zum Spielen");
                }
                return;
            }

            ClientPlayNetworkHandler handler = client.getNetworkHandler();
            if (handler != null && handler.getServerInfo() != null) {
                String serverName = handler.getServerInfo().name;
                if (!currentState.equals("Multiplayer") || !currentDetails.contains(serverName)) {
                    update("Spielt auf " + serverName, "Multiplayer");
                }
                return;
            }

            if (!currentState.equals("Singleplayer")) {
                update("In einer Welt", "Singleplayer");
            }
        });
    }

    public static void update(String details, String state) {
        if (client == null || !connected) return;
        currentDetails = details;
        currentState = state;

        RichPresence.Builder b = new RichPresence.Builder()
                .setDetails(details)
                .setState(state)
                .setActivityType(ActivityType.Playing)
                .setStartTimestamp(System.currentTimeMillis())
                .setLargeImage("opmod_large", "OPMod");
        client.sendRichPresence(b.build());
    }

    public static void stop() {
        if (client == null) return;
        client.close();
        client = null;
        connected = false;
    }
}