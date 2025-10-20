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

public final class PresenceBuilder {
  private static IPCClient client;
  private static final long APP_ID = 1428151805879259156L;
  private static String currentState = "";
  private static String currentDetails = "";
  private static boolean connected = false;
  private static long lastUpdate = 0;

  public static void start() {
    if (client != null) return;

    client = new IPCClient(APP_ID);
    client.setListener(
        new IPCListener() {
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
            System.err.println(
                "[OPMod] Discord RPC disconnected: " + (t != null ? t.getMessage() : "null"));
          }
        });

    new Thread(
            () -> {
              try {
                client.connect();
                System.out.println("[OPMod] Attempting Discord RPC connection...");
              } catch (NoDiscordClientException e) {
                System.err.println("[OPMod] Discord client not found!");
              }
            })
        .start();

    ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> stop());

    ClientTickEvents.END_CLIENT_TICK.register(
        mc -> {
          if (!connected) return;
          MinecraftClient client = MinecraftClient.getInstance();
          if (client == null) return;

          long now = System.currentTimeMillis();
          if (now - lastUpdate > 15000) {
            update(currentDetails, currentState);
            lastUpdate = now;
          }

          if (client.player == null || client.world == null) {
            if (!currentState.equals("Im Menü")) {
              update("Im Menü", "Bereit zum Spielen");
            }
            return;
          }

          ClientPlayNetworkHandler handler = client.getNetworkHandler();
          if (handler != null && handler.getServerInfo() != null) {
            String serverName = handler.getServerInfo().name;
            if (!currentState.contains(serverName)) {
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

    RichPresence.Builder b =
        new RichPresence.Builder()
            .setDetails(details)
            .setState(state)
            .setActivityType(ActivityType.Playing)
            .setStartTimestamp(System.currentTimeMillis() / 1000L)
            .setLargeImage("opmod_large", "OPMod");

    client.sendRichPresence(b.build());
  }

  public static void stop() {
    if (client == null) return;
    try {
      client.close();
      System.out.println("[OPMod] Discord RPC stopped.");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      client = null;
      connected = false;
    }
  }
}
