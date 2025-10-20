package dev.opmod.jobsystem.tracking;

public class JobData {

  private static String jobName = "Unbekannt";
  private static double xp = 0;
  private static double money = 0;
  private static int level = 0;
  private static double percent = 0;

  public void update(String jobName, double xp, double money, int level, double percent) {
    this.jobName = jobName;
    this.xp = xp;
    this.money = money;
    this.level = level;
    this.percent = percent;
  }

  public static void reset() {
    jobName = "Unbekannt";
    xp = 0;
    money = 0;
    level = 0;
    percent = 0;
  }

  public String getJobName() {
    return jobName;
  }

  public double getXp() {
    return xp;
  }

  public double getMoney() {
    return money;
  }

  public int getLevel() {
    return level;
  }

  public double getPercent() {
    return percent;
  }
}
