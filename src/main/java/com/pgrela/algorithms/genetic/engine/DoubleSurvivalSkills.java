package com.pgrela.algorithms.genetic.engine;

public class DoubleSurvivalSkills implements SurvivalSkills<DoubleSurvivalSkills> {

  private double score;

  public DoubleSurvivalSkills(double score) {
    this.score = score;
  }

  @Override
  public int compareTo(DoubleSurvivalSkills other) {
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
