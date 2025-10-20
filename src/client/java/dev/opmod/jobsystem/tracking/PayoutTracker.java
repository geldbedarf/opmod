package dev.opmod.jobsystem.tracking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayoutTracker {

  private static final Logger LOGGER = LogManager.getLogger(PayoutTracker.class);
  private final JobData jobData;

  private static final String[] JOBS = {
    "Minenarbeiter", "Holzfäller", "Gräber", "Jäger", "Fischer", "Farmer", "Builder"
  };

  private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?\\d+[\\.,]?\\d*");

  public PayoutTracker(JobData jobData) {
    this.jobData = jobData;
  }

  public void parseGameMessage(String message) {
    try {
      String jobName = detectJob(message);
      if (jobName == null) return;

      Matcher matcher = NUMBER_PATTERN.matcher(message);
      double xp = 0;
      double money = 0;
      int level = jobData.getLevel();
      double percent = jobData.getPercent();
      int count = 0;

      while (matcher.find()) {
        String raw = matcher.group().replace(",", ".");
        double num = Double.parseDouble(raw);

        switch (count) {
          case 0 -> xp += num;
          case 1 -> money += num;
          case 2 -> level = (int) num;
          case 3 -> percent = num;
        }
        count++;
      }

      jobData.update(jobName, xp, money, level, percent);
    } catch (Exception ignored) {
    }
  }

  private String detectJob(String message) {
    for (String job : JOBS) if (message.contains(job)) return job;
    return jobData.getJobName();
  }
}
