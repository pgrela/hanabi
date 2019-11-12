package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.api.Genome;
import com.pgrela.algorithms.genetic.engine.Randomness;

import java.util.Arrays;
import java.util.Random;

public class Coefficients implements Genome<Coefficients> {
    private double[] coefficients;
    private static final Random random = Randomness.RANDOM;
    public static Coefficients random() {
        return new  Coefficients(random.nextInt(10), random.nextInt(10), random.nextInt(10),
                random.nextInt(10));
    }

    public Coefficients(double... coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public Coefficients mutate() {
        double[] copy = Arrays.copyOf(coefficients, coefficients.length);
        for (int i = 0; i < copy.length; i++) {
            copy[i] += random.nextGaussian() / 2;
        }
        return new Coefficients(copy);
    }

    @Override
    public Coefficients cross(Coefficients partner) {
        double[] copy = new double[coefficients.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = (coefficients[i] + partner.coefficients[i]) / 2;
        }
        return new Coefficients(copy);
    }

    @Override
    public Coefficients deserialize(String genome) {
        return new Coefficients(Arrays.stream(genome.substring(1,genome.length()-2).split(", ")).mapToDouble(Double::valueOf).toArray());
    }

    @Override
    public String serialize() {
        return Arrays.toString(coefficients);
    }

    @Override
    public Coefficients immigrant() {
        return random();
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
