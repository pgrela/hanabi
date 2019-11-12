package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.engine.DoubleSurvivalSkills;
import com.pgrela.algorithms.genetic.engine.Jungle;
import com.pgrela.algorithms.genetic.engine.SurvivalSkills;

public class Interpolator implements Jungle<Coefficients> {

  @Override
  public SurvivalSkills evaluate(Coefficients coefficients) {
    Polynomial polynomial = new Polynomial(coefficients);
    int x[] = {0, 1, 2, 3, 4, 5};
    int y[] = {1, 2, 3, 4, 5, 6};
    double score = 0;
    for (int i = 0; i < x.length; i++) {
      double v = y[i] - polynomial.y(x[i]);
      score += v * v;
    }
    return new DoubleSurvivalSkills(-score);
  }
}
