package com.pgrela.neural.fastfloat;

public class Trainer {
    private Network network;
    private final float[][] learningSetInputs;
    private final float[][] learningSetOutputs;
    private final float[][] testingSetInputs;
    private final float[][] testingSetOutputs;

    public Trainer(Network network,
                   float[][] learningSetInputs,
                   float[][] learningSetOutputs,
                   float[][] testingSetInputs,
                   float[][] testingSetOutputs) {
        this.network = network;
        this.learningSetInputs = learningSetInputs;
        this.learningSetOutputs = learningSetOutputs;
        this.testingSetInputs = testingSetInputs;
        this.testingSetOutputs = testingSetOutputs;
    }

    public void train(int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            long startTime = System.nanoTime();
            float cost = 0;
            int learnAccuracy = 0, testAccuracy = 0;
            for (int i = 0; i < learningSetInputs.length; i++) {
                cost += network.learn(learningSetInputs[i], learningSetOutputs[i]);
                learnAccuracy += isCorrect(network.output(),learningSetOutputs[i]) ? 1 : 0;
            }
            for (int i = 0; i < testingSetInputs.length; i++) {
                float[] output = network.process(testingSetInputs[i]);
                testAccuracy += isCorrect(output, testingSetOutputs[i]) ? 1 : 0;
            }
            cost /= learningSetInputs.length;
            learnAccuracy = learnAccuracy * 100 / learningSetInputs.length;
            testAccuracy = testAccuracy * 100 / learningSetInputs.length;
            long msTime = (System.nanoTime()-startTime)/1000_000;
            System.out.format("Epoch %3d, Cost: %1.3f, Acc: %3d%%, %s Test: %3d%% %s time: %dms\n",
                    epoch, cost,
                    learnAccuracy,
                    "#".repeat(learnAccuracy / 10) + ".".repeat(10 - learnAccuracy / 10),
                    testAccuracy,
                    "#".repeat(testAccuracy / 10) + ".".repeat(10 - testAccuracy / 10), msTime
            );
        }
    }

    private boolean isCorrect(float[] actual, float[] expected) {
        return maxIndex(expected) == maxIndex(actual);
    }

    private int maxIndex(float[] list) {
        int guess = -1;
        float max = -10;
        for (int j = 0; j < 10; j++) {
            if (list[j] > max) {
                guess = j;
                max = list[j];
            }
        }
        return guess;
    }


}
