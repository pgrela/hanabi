package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.api.Genosaur;
import java.util.Arrays;
import java.util.Random;

public class Polynomial implements Genosaur<Polynomial> {

  private double genom[];
  static final Random random = new Random();

  public static Polynomial random() {
    return new Polynomial(random.nextInt(10), random.nextInt(10), random.nextInt(10),
        random.nextInt(10));
  }

  public Polynomial(double... genom) {
    this.genom = genom;
  }

  @Override
  public Polynomial mutate() {
    double[] copy = Arrays.copyOf(genom, genom.length);
    for (int i = 0; i < copy.length; i++) {
      copy[i] += random.nextGaussian()/2;
    }
    return new Polynomial(copy);
  }

  @Override
  public Polynomial procreateWith(Polynomial partner) {
    double[] copy = new double[genom.length];
    for (int i = 0; i < copy.length; i++) {
      copy[i] = (genom[i] + partner.genom[i]) / 2;
    }
    return new Polynomial(copy);
  }

  public double y(double x) {
    double y = 0;
    for (double a : genom) {
      y = y * x + a;
    }
    return y;
  }
}
