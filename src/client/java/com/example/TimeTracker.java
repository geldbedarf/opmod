package com.example;

public class TimeTracker {
  private static long startTime = -1;

  public static void start() {
    if (startTime == -1) {
      startTime = System.currentTimeMillis();
    }
  }

  public static void reset() {
    startTime = -1;
  }

  public static String getFormattedTime() {
    if (startTime == -1) return "00:00";

    long totalSeconds = getTime();
    long hours = totalSeconds / 3600;
    long minutes = (totalSeconds % 3600) / 60;
    long seconds = totalSeconds % 60;

    if (hours > 0) {
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    } else {
      return String.format("%02d:%02d", minutes, seconds);
    }
  }

  public static long getTime() {
    if (startTime == -1) return 0;
    return (System.currentTimeMillis() - startTime) / 1000;
  }
}
