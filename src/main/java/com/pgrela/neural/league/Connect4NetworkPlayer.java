package com.pgrela.neural.league;

import com.pgrela.games.engine.api.Player;
import com.pgrela.neural.fast.Network;
import com.pgrela.neural.utils.NameGenerator;
import java.util.Arrays;
import java.util.Random;

public class Connect4NetworkPlayer extends Network implements Player {

    public static final int INPUT_SIZE = 42 * 3;
    public static final int OUTPUT_SIZE = 7;

    private String name;
    private int ws;
    private int bs;

    public Connect4NetworkPlayer(int randomSeed) {
        super(1, 0.01, randomSeed, 1, INPUT_SIZE, 200, OUTPUT_SIZE);
        name = NameGenerator.name(randomSeed);
        for (int i = 1; i < layers.length; i++) {
            ws += layers[i - 1] * layers[i];
        }
        bs = Arrays.stream(layers).reduce(0, Integer::sum) - layers[0];
    }

    protected Connect4NetworkPlayer(String name) {
        super(new int[0],null, null, 1, 1, 1);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void getReady() {}

    public void chaos(Random chaos, int amount) {
        for (int i = 0; i < bs * amount / 100; i++) {
            double[] b = biases[chaos.nextInt(biases.length)];
            b[chaos.nextInt((b.length))] *= chaos.nextInt(2) - 1;
        }
        for (int i = 0; i < ws * amount / 100; i++) {
            double[][] w = weights[chaos.nextInt(weights.length)];
            double[] we = w[chaos.nextInt(w.length)];
            we[chaos.nextInt(we.length)] *= chaos.nextInt(2) - 1;
        }
    }
}
