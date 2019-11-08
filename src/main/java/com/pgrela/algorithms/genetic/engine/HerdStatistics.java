package com.pgrela.algorithms.genetic.engine;

import java.util.Arrays;

public class HerdStatistics {
  private SurvivalSkills min;
  private SurvivalSkills max;
  private double avg;

  public HerdStatistics(SurvivalSkills[] skills) {
    min = Arrays.stream(skills).min(SurvivalSkills::compareTo).get();
    max = Arrays.stream(skills).max(SurvivalSkills::compareTo).get();
    avg = Arrays.stream(skills).mapToDouble(SurvivalSkills::score).average().orElse(0);
  }

  public SurvivalSkills getMin() {
    return min;
  }

  public SurvivalSkills getMax() {
    return max;
  }

  public double getAvg() {
    return avg;
  }

  @Override
  public String toString() {
    return "HerdStatistics{" +
        "min=" + min +
        ", max=" + max +
        ", avg=" + String.format("%.2f", avg) +
        '}';
  }
}
