package com.pgrela.neural.fast;

public class BatchStats {
    private final int correct;
    private final double costSum;

    public BatchStats(int correct, double costSum) {
        this.correct = correct;
        this.costSum = costSum;
    }

    public int getCorrect() {
        return correct;
    }

    public double getCostSum() {
        return costSum;
    }
}
