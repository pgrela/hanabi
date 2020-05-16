package com.pgrela.neural.fast;

import com.pgrela.neural.core.Input;
import com.pgrela.neural.core.Output;
import com.pgrela.neural.mnist.MnistData;
import com.pgrela.neural.training.TrainingSample;
import com.pgrela.neural.training.TrainingSet;

import java.nio.file.Path;

public class Main {

    public static final Path SAVE_DIR = Path.of(System.getProperty("user.home"), "tmp", "nn");

    public static void main(String[] args) {
        int nThreads = 8;
        int batch = 200;
        double learningFactor = 0.01;
        Network network = new Network(batch, learningFactor, 4, nThreads, 28 * 28, 32, 10);
        network.setBatchSize(batch);
        network.setupMultithreading(nThreads);
        network.setLearningFactor(learningFactor);
        MnistData mnistData = new MnistData();
        TrainingSet learningSet = mnistData.learningSet(60000);
        TrainingSet testingSet = mnistData.testingSet(10000);
        double[][] learningSetInputs = learningSet.getSamples().stream().map(TrainingSample::getInput).map(Main::asDoubles).toArray(double[][]::new);
        double[][] learningSetOutputs = learningSet.getSamples().stream().map(TrainingSample::getOutput).map(Main::asDoubles).toArray(double[][]::new);
        double[][] testingSetInputs = testingSet.getSamples().stream().map(TrainingSample::getInput).map(Main::asDoubles).toArray(double[][]::new);
        double[][] testingSetOutputs = testingSet.getSamples().stream().map(TrainingSample::getOutput).map(Main::asDoubles).toArray(double[][]::new);
        learningSet = null;
        testingSet = null;
        Trainer trainer = new Trainer(network, learningSetInputs, learningSetOutputs, testingSetInputs, testingSetOutputs, nThreads, batch);
        trainer.train(10000);
    }

    private static double[] asDoubles(Input input) {
        double[] in = new double[input.size()];
        for (int i = 0; i < in.length; i++) {
            in[i] = input.get(i);
        }
        return in;
    }

    private static double[] asDoubles(Output output) {
        double[] in = new double[output.size()];
        for (int i = 0; i < in.length; i++) {
            in[i] = output.get(i);
        }
        return in;
    }
}
