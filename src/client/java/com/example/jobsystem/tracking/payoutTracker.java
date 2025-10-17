package com.example.jobsystem.tracking;

import java.util.HashMap;
import java.util.regex.*;

public class payoutTracker {

  public static HashMap<String, Object> parseGameMessage(String packet) {
    HashMap<String, Object> result = new HashMap<>();

    try {
      // --- 1. Job extrahieren ---
      String[] jobs = {
        "Minenarbeiter", "Holzfäller", "Gräber", "Jäger", "Fischer", "Farmer", "Builder"
      };
      for (String job : jobs) {
        if (packet.contains(job)) {
          result.put("Job", job);
          break;
        }
      }

      // --- 2. Zahlen extrahieren ---
      Pattern pattern = Pattern.compile("[+-]?\\d+[\\.,]?\\d*");
      Matcher matcher = pattern.matcher(packet);

      int count = 0;
      while (matcher.find()) {
        String zahlStr = matcher.group().replace(",", ".");
        try {
          double zahl = Double.parseDouble(zahlStr);

          switch (count) {
            case 0 -> result.put("XP", zahl);
            case 1 -> result.put("Geld", zahl);
            case 2 -> result.put("Level", (int) zahl); // Level als int
            case 3 -> result.put("Prozent", zahl);
          }

          count++;
        } catch (NumberFormatException e) {
          // Wenn eine Zahl nicht geparst werden kann, einfach skippen
          continue;
        }
      }
    } catch (Exception e) {
      // Gesamtes Packet fehlerhaft → einfach überspringen
      System.out.println("Fehler beim Parsen, Packet wird übersprungen.");
      return null;
    }

    return result;
  }
}
