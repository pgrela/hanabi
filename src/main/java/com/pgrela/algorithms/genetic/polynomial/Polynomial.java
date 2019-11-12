package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.engine.Randomness;

import java.util.Random;

public class Polynomial {

  static final Random random = Randomness.RANDOM;

  private final Coefficients coefficients;

  public Polynomial(Coefficients coefficients) {
    this.coefficients = coefficients;
  }

  public double y(double x) {
    double y = 0;
    for (double a : coefficients.getCoefficients()) {
      y = y * x + a;
    }
    return y;
  }
}
