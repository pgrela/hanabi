package com.pgrela.algorithms.genetic.engine;

public class IntegerSurvivalSkills implements SurvivalSkills<IntegerSurvivalSkills> {

  private int score;

  public IntegerSurvivalSkills(int score) {
    this.score = score;
  }

  @Override
  public int compareTo(IntegerSurvivalSkills other) {
    return Double.compare(score, other.score);
  }

  public double score() {
    return score;
  }

  @Override
  public String toString() {
    return String.format("%d", score);
  }
}
