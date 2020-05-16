package com.pgrela.neural.core;

import java.util.Random;

public class NeuralRandom {
    private Random random;

    public NeuralRandom(int seed) {
        random = new Random(seed);
    }

    public double nextWeight() {
        return random.nextDouble() * 2 - 1;
    }

    public double nextBias() {
        return random.nextDouble() * 2 - 1;
    }
}
