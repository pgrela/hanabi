package com.pgrela.neural.league;

import java.util.Arrays;
import java.util.Random;

public class Idiot extends Connect4NetworkPlayer {
    private final int randomSeed;
    private Random random;
    private double[] output = new double[7];

    public Idiot(int randomSeed) {
        super("Idiot " + randomSeed);
        this.randomSeed = randomSeed;
        random = new Random();
    }

    @Override
    public void getReady() {
        random.setSeed(randomSeed);
    }

    @Override
    public double[] process(double[] input) {
        Arrays.fill(output, 0);
        for (int i = 0; i < 7; i++) {
            int k = random.nextInt(7);
            if (input[(0 * 7 + k) * 2] + input[(0 * 7 + k) * 2 + 1] < .5) {
                output[k] = 1;
                return output;
            }
        }
        for (int i = 0; i < 7; i++) {
            if (input[(0 * 7 + i) * 2] + input[(0 * 7 + i) * 2 + 1] < .5) {
                output[i] = 1;
                return output;
            }
        }
        return output;
    }

    @Override
    public double learn(double[] input, double[] output) {
        return 0;
        //throw new RuntimeException("Idiot never learns");
    }
}
