package com.pgrela.neural.training;

import com.pgrela.neural.core.Output;
import com.pgrela.neural.learning.LearningNetwork;

public class Trainer {
    private LearningNetwork network;
    private final TrainingSet learningSet;
    private final TrainingSet testingSet;

    public Trainer(LearningNetwork network, TrainingSet learningSet, TrainingSet testingSet) {
        this.network = network;
        this.learningSet = learningSet;
        this.testingSet = testingSet;
    }

    public void train(int epochs, int batch) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            long startTime = System.nanoTime();
            double cost = 0;
            int learnAccuracy = 0, testAccuracy = 0;
            int processed = 0;
            for (TrainingSample sample : learningSet.getSamples()) {
                cost += network.learn(sample.getInput(), sample.getOutput());
                learnAccuracy += isCorrect(network.output(), sample.getOutput()) ? 1 : 0;
                if (++processed % batch == 0) {
                    network.summariseKnowledge();
                }
            }
            if (processed % batch != 0) {
                network.summariseKnowledge();
            }
            for (TrainingSample sample : testingSet.getSamples()) {
                Output output = network.process(sample.getInput());
                testAccuracy += isCorrect(output, sample.getOutput()) ? 1 : 0;
            }
            cost /= learningSet.getSamples().size();
            learnAccuracy = learnAccuracy * 100 / learningSet.getSamples().size();
            testAccuracy = testAccuracy * 100 / testingSet.getSamples().size();
            long msTime = (System.nanoTime()-startTime)/1000_000;
            System.out.format("Epoch %3d, Cost: %1.3f, Acc: %3d%%, %s Test: %3d%% %s time: %dms\n",
                    epoch, cost,
                    learnAccuracy,
                    "#".repeat(learnAccuracy / 10) + ".".repeat(10 - learnAccuracy / 10),
                    testAccuracy,
                    "#".repeat(testAccuracy / 10) + ".".repeat(10 - testAccuracy / 10), msTime
            );
            if(learnAccuracy==100 && testAccuracy==100){
                System.out.println("100% DONE!");
                return;
            }
        }
    }

    private boolean isCorrect(Output actual, Output expected) {
        return maxIndex(expected) == maxIndex(actual);
    }

    private int maxIndex(Output list) {
        int guess = -1;
        double max = -10;
        for (int j = 0; j < 10; j++) {
            if (list.get(j) > max) {
                guess = j;
                max = list.get(j);
            }
        }
        return guess;
    }


}
