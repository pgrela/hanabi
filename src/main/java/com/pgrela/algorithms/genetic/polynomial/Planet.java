package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.engine.Jungle;
import com.pgrela.algorithms.genetic.engine.MatingSeason;
import com.pgrela.algorithms.genetic.engine.Pangea;
import com.pgrela.algorithms.genetic.engine.Ritual;
import com.pgrela.algorithms.genetic.engine.SurvivalSkills;
import com.pgrela.algorithms.genetic.engine.Zoologist;
import java.util.ArrayList;
import java.util.Collection;

public class Planet {

  public static void main(String[] args) {
    Collection<Polynomial> polynomials = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      polynomials.add(Polynomial.random());
    }
    Ritual<Polynomial> ritual = new MatingSeason<>();
    Pangea pangea = new PangeaBuilder<Polynomial>()
        .withHerd(polynomials)
        .withJungle(new J())
        .withRitual(ritual)
        .withZoologists(new Zoologist())
        .create();
    pangea.start();
  }

  static class J implements Jungle<Polynomial> {

    @Override
    public SurvivalSkills judge(Polynomial polynomial) {
      int x[] = {0, 1, 2, 3, 4, 5};
      int y[] = {1, 2, 3, 4, 5, 6};
      double score = 0;
      for (int i = 0; i < x.length; i++) {
        double v = y[i] - polynomial.y(x[i]);
        score += v * v;
      }
      return new SurvivalSkills(-score);
    }
  }

}
