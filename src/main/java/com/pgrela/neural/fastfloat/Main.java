package com.pgrela.neural.fastfloat;

import com.pgrela.neural.core.Input;
import com.pgrela.neural.core.Output;
import com.pgrela.neural.mnist.MnistData;
import com.pgrela.neural.training.TrainingSample;
import com.pgrela.neural.training.TrainingSet;

public class Main {

    public static void main(String[] args) {
        Network network = new Network(50, 0.05f, 28 * 28, 48, 10);
        MnistData mnistData = new MnistData();
        TrainingSet learningSet = mnistData.learningSet(500);
        TrainingSet testingSet = mnistData.learningSet(50);
        float[][] learningSetInputs = learningSet.getSamples().stream().map(TrainingSample::getInput).map(Main::asDoubles).toArray(float[][]::new);
        float[][] learningSetOutputs = learningSet.getSamples().stream().map(TrainingSample::getOutput).map(Main::asDoubles).toArray(float[][]::new);
        float[][] testingSetInputs = testingSet.getSamples().stream().map(TrainingSample::getInput).map(Main::asDoubles).toArray(float[][]::new);
        float[][] testingSetOutputs = testingSet.getSamples().stream().map(TrainingSample::getOutput).map(Main::asDoubles).toArray(float[][]::new);
        Trainer trainer = new Trainer(network, learningSetInputs, learningSetOutputs, testingSetInputs, testingSetOutputs);
        trainer.train(100);
    }

    private static float[] asDoubles(Input input) {
        float[] in = new float[input.size()];
        for (int i = 0; i < in.length; i++) {
            in[i] = (float) input.get(i);
        }
        return in;
    }
    private static float[] asDoubles(Output output) {
        float[] in = new float[output.size()];
        for (int i = 0; i < in.length; i++) {
            in[i] = (float) output.get(i);
        }
        return in;
    }
}
