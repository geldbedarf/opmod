package dev.opmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Tracks elapsed active time (tick-based), useful for tracking job progress. */
public class TimeTracker {

  private static final Logger LOGGER = LogManager.getLogger(TimeTracker.class);

  private long elapsedSeconds = 0;
  private boolean running = false;

  public void start() {
    if (!running) {
      running = true;
      LOGGER.debug("TimeTracker started");
    }
  }

  public void pause() {
    if (running) {
      running = false;
      LOGGER.debug("TimeTracker paused");
    }
  }

  public void reset() {
    running = false;
    elapsedSeconds = 0;
    LOGGER.debug("TimeTracker reset");
  }

  /** Called once per client tick (20x per second) */
  public void onTick() {
    if (running) {
      elapsedSeconds++;
    }
  }

  public long getElapsedSeconds() {
    return elapsedSeconds;
  }

  public String getFormatted() {
    long seconds = elapsedSeconds;
    long h = seconds / 3600;
    long m = (seconds % 3600) / 60;
    long s = seconds % 60;

    return (h > 0) ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);
  }

  public boolean isRunning() {
    return running;
  }
}
