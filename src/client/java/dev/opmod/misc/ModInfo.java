package dev.opmod.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Enthält Metadaten über die Mod und dient als zentrale Referenz für Version/Name. */
public class ModInfo {
  public static final String MOD_ID = "opmod";
  public static final String GITHUB_RELEASES_API =
      "https://api.github.com/repos/geldbedarf/opmod/releases/latest";

  private static final Logger LOGGER = LogManager.getLogger(ModInfo.class);
  private static String name = MOD_ID;
  private static String version = "0.0.0"; // Fallback für dev

  static {
    Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(MOD_ID);
    mod.ifPresent(
        container -> {
          name = container.getMetadata().getName();
          version = container.getMetadata().getVersion().getFriendlyString();
        });
  }

  public static String getId() {
    return MOD_ID;
  }

  public static String getName() {
    return name;
  }

  public static String getVersion() {
    return version;
  }

  /**
   * Prüft auf GitHub, ob eine neuere Version der Mod verfügbar ist. Diese Methode sollte NICHT im
   * Main-Thread laufen, da sie Netzwerkzugriff benötigt.
   */
  public static void checkForUpdatesAsync() {
    new Thread(
            () -> {
              try {
                String latest = fetchLatestReleaseVersion();
                if (latest == null || latest.isEmpty()) return;

                if (!latest.equals(getVersion())) {
                  notifyPlayerAboutUpdate(latest);
                  LOGGER.warn(
                      "Update verfügbar! Aktuelle Version: {} | Neueste Version: {}",
                      getVersion(),
                      latest);
                } else {
                  LOGGER.info("Mod ist aktuell (Version {}).", getVersion());
                }
              } catch (Exception e) {
                LOGGER.error(
                    "Konnte keine Versionsinformation von GitHub laden: {}", e.getMessage());
              }
            })
        .start();
  }

  private static String fetchLatestReleaseVersion() {
    try {
      URL url = new URL(GITHUB_RELEASES_API);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("User-Agent", "OPMOD-Version-Checker");
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        // GitHub API liefert JSON – wir suchen die "tag_name"
        int index = line.indexOf("\"tag_name\"");
        if (index != -1) {
          return line.split(":")[1].replace("\"", "").replace(",", "").trim();
        }
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  private static void notifyPlayerAboutUpdate(String latestVersion) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client != null && client.player != null) {
      client.execute(
          () ->
              client.player.sendMessage(
                  Text.literal(
                      "§eEin Update für OPMOD ist verfügbar! Aktuell: §c"
                          + getVersion()
                          + " §7→ Neu: §a"
                          + latestVersion
                          + " §7Download: https://github.com/geldbedarf/opmod/releases"),
                  false));
    }
  }
}
