package com.pgrela.algorithms.genetic;

import com.pgrela.algorithms.genetic.engine.MatingSeason;
import com.pgrela.algorithms.genetic.engine.Pangea;
import com.pgrela.algorithms.genetic.engine.Ritual;
import com.pgrela.algorithms.genetic.engine.Zoologist;
import com.pgrela.algorithms.genetic.polynomial.Coefficients;
import com.pgrela.algorithms.genetic.polynomial.Interpolator;
import com.pgrela.algorithms.genetic.engine.PangeaBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class Planet {

  public static void main(String[] args) {
    Collection<Coefficients> polynomials = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      polynomials.add(Coefficients.random());
    }
    Ritual<Coefficients> ritual = new MatingSeason<>();
    Pangea pangea = new PangeaBuilder<Coefficients>()
        .withHerd(polynomials)
        .withJungle(new Interpolator())
        .withRitual(ritual)
        .withZoologists(new Zoologist())
        .create();
    pangea.start();
  }

}
