package com.pgrela.algorithms.genetic.engine;

public class SurvivalSkills implements Comparable<SurvivalSkills> {

  private double score;

  public SurvivalSkills(double score) {
    this.score = score;
  }

  @Override
  public int compareTo(SurvivalSkills other) {
    return Double.compare(score, other.score);
  }

  public double score() {
    return score;
  }

  @Override
  public String toString() {
    return String.format("%.2f", score);
  }
}
