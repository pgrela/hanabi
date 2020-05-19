package com.pgrela.neural.fast;

import com.pgrela.neural.fast.Network.Processor;
import com.pgrela.neural.utils.Utils;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.pgrela.neural.fast.Main.SAVE_DIR;

public class Trainer {
    private Network network;
    private final double[][] learningSetInputs;
    private final double[][] learningSetOutputs;
    private final double[][] testingSetInputs;
    private final double[][] testingSetOutputs;
    private int nThreads;
    private int batch;

    public Trainer(Network network,
                   double[][] learningSetInputs,
                   double[][] learningSetOutputs,
                   double[][] testingSetInputs,
                   double[][] testingSetOutputs, int nThreads, int batch) {
        this.batch = batch;
        this.network = network;
        this.learningSetInputs = learningSetInputs;
        this.learningSetOutputs = learningSetOutputs;
        this.testingSetInputs = testingSetInputs;
        this.testingSetOutputs = testingSetOutputs;
        this.nThreads = nThreads;
    }

    public void train(int epochs) {
        int saveEvery = 20;
        Future<Processor>[] outputs = new Future[nThreads];
        for (int epoch = 0; epoch < epochs; epoch++) {
            long startTime = System.nanoTime();
            double cost = 0;
            int learnAccuracy = 0, testAccuracy = 0;
            for (int i = 0; i < learningSetInputs.length; i += batch) {
//                cost += network.learn(learningSetInputs[i], learningSetOutputs[i]);
//                learnAccuracy += isCorrect(network.output(), learningSetOutputs[i]) ? 1 : 0;
                BatchStats batchStats = network.learnBatch(learningSetInputs, learningSetOutputs, i, batch);
                cost += batchStats.getCostSum();
                learnAccuracy += batchStats.getCorrect();
            }
            for (int i = 0; i < testingSetInputs.length; i += nThreads) {
                for (int j = 0; j < nThreads && i + j < testingSetInputs.length; j++) {
                    outputs[j] = network.processAsync(testingSetInputs[i + j], j);
                }
                for (int j = 0; j < nThreads && i + j < testingSetInputs.length; j++) {
                    try {
                        testAccuracy += isCorrect(outputs[j].get().output(), testingSetOutputs[i + j]) ? 1 : 0;
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            cost /= learningSetInputs.length;
            learnAccuracy = learnAccuracy * 100 / learningSetInputs.length;
            testAccuracy = testAccuracy * 100 / testingSetInputs.length;
            long msTime = (System.nanoTime() - startTime) / 1000_000;
            System.out.format("Epoch %3d, Cost: %1.3f, Acc: %3d%%, %s Test: %3d%% %s time: %dms\n",
                    epoch, cost,
                    learnAccuracy,
                    "#".repeat(learnAccuracy / 10) + ".".repeat(10 - learnAccuracy / 10),
                    testAccuracy,
                    "#".repeat(testAccuracy / 10) + ".".repeat(10 - testAccuracy / 10), msTime
            );
            if(epoch>0 && epoch%saveEvery==0 && learningSetInputs.length>5000){
                network.toFile(new File(SAVE_DIR.toString(), String.format("autosave_ls%d_e%d_t%d.nn", learningSetInputs.length, epoch, testAccuracy)));
            }
            if (learnAccuracy == 100 && testAccuracy == 100) {
                System.out.println("100% DONE!");
                return;
            }
        }
    }

    static boolean isCorrect(double[] actual, double[] expected) {
        return Utils.maxIndex(expected) == Utils.maxIndex(actual);
    }


}
