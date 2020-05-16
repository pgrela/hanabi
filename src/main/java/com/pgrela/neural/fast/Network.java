package com.pgrela.neural.fast;

import com.pgrela.neural.core.ActivationFunction;
import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.utils.Activation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.pgrela.neural.fast.Trainer.isCorrect;

public class Network {
    private int nThreads;
    private double[][] biases;
    private double[][][] weights;
    private double[][] values;
    private double[][] sensitivity;
    private double[][] inputSums;
    private double learningFactor;
    private int[] layers;
    private int outputLayer;
    private int batch;
    private int batchNo = 0;
    private final double[][] expectedBiases;
    private final double[][] biasessSensitivity;
    private final double[][][] expectedWeights;
    private final double[][][] weightsSensitivity;
    private final ActivationFunction innerFunction = Activation.SIGMA_1_0_1;
    private final ActivationFunction outerFunction = Activation.SIGMA_1_0_1;
    private ExecutorService executorService;
    private Processor[] processors;
    private Learner[] learners;

    private Network(NeuralRandom random, int batch, double learningFactor, int nThreads, int... layers) {
        this(layers, Arrays.stream(layers)
                .mapToObj(i -> DoubleStream.generate(random::nextBias).limit(i).toArray())
                .toArray(double[][]::new), IntStream.range(0, layers.length - 1).mapToObj(fromLayer ->
                Stream.generate(() -> DoubleStream.generate(random::nextWeight).limit(layers[fromLayer + 1]).toArray())
                        .limit(layers[fromLayer])
                        .toArray(double[][]::new)).toArray(double[][][]::new), batch, learningFactor, nThreads);
    }

    private Network(int randomSeed, int batch, double learningFactor, int nThreads, int... layers) {
        this(new NeuralRandom(randomSeed), batch, learningFactor, nThreads, layers);
    }

    public Network(int batch, double learningFactor, int randomSeed, int nThreads, int... layers) {
        this(randomSeed, batch, learningFactor, nThreads, layers);

    }

    public Network(int[] layers, double[][] biases, double[][][] weights, int batch, double learningFactor, int nThreads) {
        this.layers = layers;
        this.biases = biases;
        this.weights = weights;

        outputLayer = layers.length - 1;

        values = Arrays.stream(layers).mapToObj(double[]::new).toArray(double[][]::new);
        sensitivity = Arrays.stream(layers).mapToObj(double[]::new).toArray(double[][]::new);
        inputSums = Arrays.stream(layers).mapToObj(double[]::new).toArray(double[][]::new);
        expectedBiases = Arrays.stream(layers).mapToObj(double[]::new).toArray(double[][]::new);
        biasessSensitivity = Arrays.stream(layers).mapToObj(double[]::new).toArray(double[][]::new);
        expectedWeights = IntStream.range(0, layers.length - 1)
                .mapToObj(fromLayer -> Stream.generate(() -> new double[layers[fromLayer + 1]]).limit(layers[fromLayer]).toArray(double[][]::new))
                .toArray(double[][][]::new);
        weightsSensitivity = IntStream.range(0, layers.length - 1)
                .mapToObj(fromLayer -> Stream.generate(() -> new double[layers[fromLayer + 1]]).limit(layers[fromLayer]).toArray(double[][]::new))
                .toArray(double[][][]::new);

        setBatchSize(batch);
        setLearningFactor(learningFactor);
        setupMultithreading(nThreads);
    }

    public void setBatchSize(int batch) {
        this.batch = batch;
    }

    public void setLearningFactor(double learningFactor) {
        this.learningFactor = learningFactor;
    }

    public void setupMultithreading(int nThreads) {
        this.nThreads = nThreads;
        executorService = Executors.newFixedThreadPool(this.nThreads);
        processors = Stream.generate(() -> new Processor(this)).limit(this.nThreads).toArray(Processor[]::new);
        learners = Stream.generate(() -> new Learner(this)).limit(this.nThreads).toArray(Learner[]::new);
    }

    private static void zero(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            Arrays.fill(a[i], 0);
        }
    }

    public double[] process(double[] input) {
        values[0] = input;
        process(layers, values, inputSums, biases, outerFunction, innerFunction, weights);
        return values[outputLayer];
    }

    public Future<Processor> processAsync(double[] input, int thread) {
        processors[thread].setInput(input);
        return executorService.submit(processors[thread], processors[thread]);
    }

    public BatchStats learnBatch(double[][] learningSetInputs, double[][] learningSetOutputs, int start, int batch) {
        int[] runsPerThread = new int[nThreads];
        int left = batch;
        for (int i = 0; i < nThreads; i++) {
            runsPerThread[i] = left / (nThreads - i);
            left -= runsPerThread[i];
        }
        Future[] futures = new Future[nThreads];
        int taken = 0;
        for (int thread = 0; thread < nThreads; thread++) {
            learners[thread].setup(learningSetInputs, learningSetOutputs, start + taken, runsPerThread[thread]);
            taken += runsPerThread[thread];
            futures[thread] = executorService.submit(learners[thread]);
        }
        for (int thread = 0; thread < nThreads; thread++) {
            try {
                futures[thread].get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        double sumCost = 0;
        int correct = 0;
        for (int thread = 0; thread < nThreads; thread++) {
            sumCost += learners[thread].cost;
            correct += learners[thread].correct;
            for (int previousLayer = 0; previousLayer < layers.length - 1; previousLayer++) {
                int currentLayer = previousLayer + 1;
                for (int i = 0; i < layers[currentLayer]; i++) {
                    expectedBiases[currentLayer][i] += learners[thread].expectedBiases[currentLayer][i];
                    learners[thread].expectedBiases[currentLayer][i] = 0;
                    for (int j = 0; j < layers[previousLayer]; j++) {
                        expectedWeights[previousLayer][j][i] += learners[thread].expectedWeights[previousLayer][j][i];
                        learners[thread].expectedWeights[previousLayer][j][i] = 0;
                    }
                }
            }
        }
        summarize(batch);
        return new BatchStats(correct, sumCost);
    }

    public static class Learner implements Runnable {
        private final double[][] values;
        private final Network network;
        private final double[][] expectedBiases;
        private final double[][][] expectedWeights;
        private final double[][] sensitivity;
        private final double[][] inputSums;
        private double[][] learningSetInputs;
        private double[][] learningSetOutputs;
        private int start;
        private int batchSize;
        double cost;
        int correct;

        public Learner(Network network) {
            this.network = network;
            values = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
            sensitivity = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
            inputSums = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
            expectedBiases = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
            expectedWeights = IntStream.range(0, network.layers.length - 1)
                    .mapToObj(fromLayer -> Stream.generate(() -> new double[network.layers[fromLayer + 1]])
                            .limit(network.layers[fromLayer])
                            .toArray(double[][]::new))
                    .toArray(double[][][]::new);
        }

        public void setInput(double[] input) {
            values[0] = input;
        }

        public double[] output() {
            return values[network.layers.length - 1];
        }

        @Override
        public void run() {
            for (int i = 0; i < batchSize; i++) {
                cost += learn(learningSetInputs[start + i],
                        learningSetOutputs[start + i],
                        network.layers,
                        inputSums,
                        network.biases,
                        values,
                        sensitivity,
                        expectedBiases,
                        expectedWeights,
                        network.weights,
                        network.outerFunction, network.innerFunction, network.learningFactor);
                correct += isCorrect(output(), learningSetOutputs[start + i]) ? 1 : 0;
            }
        }

        public void setup(double[][] learningSetInputs, double[][] learningSetOutputs, int start, int batchSize) {
            this.learningSetInputs = learningSetInputs;
            this.learningSetOutputs = learningSetOutputs;
            this.start = start;
            this.batchSize = batchSize;
            this.cost = 0;
            this.correct = 0;
        }
    }

    public static class Processor implements Runnable {
        private final double[][] values;
        private final Network network;
        private final double[][] inputSums;

        public Processor(Network network) {
            this.network = network;
            values = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
            inputSums = Arrays.stream(network.layers).mapToObj(double[]::new).toArray(double[][]::new);
        }

        public void setInput(double[] input) {
            values[0] = input;
        }

        public double[] output() {
            return values[network.layers.length - 1];
        }

        @Override
        public void run() {
            process(network.layers, values, inputSums, network.biases, network.outerFunction, network.innerFunction, network.weights);
        }

    }

    private static void process(int[] layers,
                                double[][] values,
                                double[][] inputSums,
                                double[][] biases,
                                ActivationFunction outerFunction, ActivationFunction innerFunction, double[][][] weights) {
        for (int nextLayer = 1; nextLayer < layers.length; nextLayer++) {
            for (int currentNeuron = 0; currentNeuron < layers[nextLayer]; currentNeuron++) {
                double inputSum = 0;
                for (int i = 0; i < layers[nextLayer - 1]; i++) {
                    inputSum += values[nextLayer - 1][i] * weights[nextLayer - 1][i][currentNeuron];
                }
                inputSums[nextLayer][currentNeuron] = inputSum;
                double value = inputSum + biases[nextLayer][currentNeuron];
                values[nextLayer][currentNeuron] = nextLayer == layers.length - 1 ? outerFunction.function(value) : innerFunction.function(value);
            }
        }
    }

    public double learn(double[] input, double[] output) {
        double cost = learn(input,
                output,
                layers,
                inputSums,
                biases,
                values,
                sensitivity,
                expectedBiases,
                expectedWeights,
                weights,
                outerFunction, innerFunction, learningFactor);
        ++batchNo;
        if (batchNo == batch) {
            //verify(output);
            summarize(batch);
            batchNo = 0;
        }
        return cost;
    }

    public static double learn(double[] input,
                               double[] output,
                               int[] layers,
                               double[][] inputSums,
                               double[][] biases,
                               double[][] values,
                               double[][] sensitivity,
                               double[][] expectedBiases,
                               double[][][] expectedWeights,
                               double[][][] weights,
                               ActivationFunction outerFunction, ActivationFunction innerFunction, double learningFactor) {
        zero(sensitivity);
        int outputLayer = layers.length - 1;
        values[0] = input;
        process(layers, values, inputSums, biases, outerFunction, innerFunction, weights);
        double cost = 0;
        for (int i = 0; i < layers[outputLayer]; i++) {
            double singlecost = (values[outputLayer][i] - output[i]);
            sensitivity[outputLayer][i] = 2 * singlecost;
            cost += singlecost * singlecost;
        }
        for (int currentLayer = outputLayer; currentLayer > 0; currentLayer--) {
            int previousLayer = currentLayer - 1;
            for (int i = 0; i < layers[currentLayer]; i++) {
                double inputSumPlusB = inputSums[currentLayer][i] + biases[currentLayer][i];
                double derivative = currentLayer == outputLayer ? outerFunction.derivative(inputSumPlusB) : innerFunction.derivative(inputSumPlusB);
                double internalSensitivity = derivative * sensitivity[currentLayer][i];
                expectedBiases[currentLayer][i] += (biases[currentLayer][i] - internalSensitivity * learningFactor);
                for (int j = 0; j < layers[previousLayer]; j++) {
                    double weightSensitivity = internalSensitivity * values[previousLayer][j];
                    sensitivity[previousLayer][j] += internalSensitivity * weights[previousLayer][j][i];
                    expectedWeights[previousLayer][j][i] += (weights[previousLayer][j][i] - weightSensitivity * learningFactor);
                }
            }
        }
        return cost;
    }

    private void verify(double[] output) {
        double delta = 0.0001;
        for (int layer = 1; layer < layers.length; layer++) {
            for (int neuron = 0; neuron < layers[layer]; neuron++) {
                {
                    double original = biases[layer][neuron];
                    biases[layer][neuron] = original + delta;
                    double costPlus = cost(process(values[0]), output);
                    biases[layer][neuron] = original - delta;
                    double costMinus = cost(process(values[0]), output);
                    biases[layer][neuron] = original;
                    double measuredSensitivity = (costPlus - costMinus) / 2 / delta;
                    same(biasessSensitivity[layer][neuron], measuredSensitivity);
                }

                int previousLayer = layer - 1;
                for (int previousNeuron = 0; previousNeuron < layers[previousLayer]; previousNeuron++) {
                    double original = weights[previousLayer][previousNeuron][neuron];
                    weights[previousLayer][previousNeuron][neuron] = original + delta;
                    double costPlus = cost(process(values[0]), output);
                    weights[previousLayer][previousNeuron][neuron] = original - delta;
                    double costMinus = cost(process(values[0]), output);
                    weights[previousLayer][previousNeuron][neuron] = original;
                    double measuredSensitivity = (costPlus - costMinus) / 2 / delta;
                    same(weightsSensitivity[previousLayer][previousNeuron][neuron], measuredSensitivity);
                }
            }
        }
        process(values[0]);
        System.out.println("Verified");
    }

    private void same(double x, double y) {
        double zero = 0.000000001;
        double ax = Math.abs(x);
        double ay = Math.abs(y);
        if (ax < zero && ay < zero) return;
        double xy = x / y;
        if (.9 < xy && xy < 1.1) {
            return;
        }
        System.out.println(xy);
        if (.5 < xy && xy < 2) return;
        System.out.println(xy);
        //throw new RuntimeException();
    }

    private double cost(double[] result, double[] output) {
        double c = 0;
        for (int i = 0; i < result.length; i++) {
            double s = result[i] - output[i];
            c += s * s;
        }
        return c;
    }

    private void summarize(int batch) {
        for (int previousLayer = 0; previousLayer < layers.length - 1; previousLayer++) {
            int currentLayer = previousLayer + 1;
            for (int i = 0; i < layers[currentLayer]; i++) {
                biases[currentLayer][i] = expectedBiases[currentLayer][i] / batch;
                expectedBiases[currentLayer][i] = 0;
                for (int j = 0; j < layers[previousLayer]; j++) {
                    weights[previousLayer][j][i] = expectedWeights[previousLayer][j][i] / batch;
                    expectedWeights[previousLayer][j][i] = 0;
                }
            }
        }
    }

    public double[] output() {
        return values[outputLayer];
    }

    public String serializeToReadableString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.stream(layers).mapToObj(Integer::toString).collect(Collectors.joining(" ")));
        sb.append("\n");
        for (int layer = 0; layer < layers.length; layer++) {
            sb.append(Arrays.stream(biases[layer]).mapToObj(Double::toString).collect(Collectors.joining(" ")));
            sb.append("\n");
        }
        for (int layer = 0; layer < outputLayer; layer++) {
            for (int neuron = 0; neuron < weights[layer].length; neuron++) {
                sb.append(Arrays.stream(weights[layer][neuron]).mapToObj(Double::toString).collect(Collectors.joining(" ")));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static Network readFromReadableString(List<String> s) {
        Iterator<String> lines = s.iterator();
        int[] layers = Arrays.stream(lines.next().split(" ")).mapToInt(Integer::valueOf).toArray();
        double[][] biases = new double[layers.length][];
        for (int i = 0; i < layers.length; i++) {
            biases[i] = Arrays.stream(lines.next().split(" ")).mapToDouble(Double::valueOf).toArray();
        }
        double[][][] weights = new double[layers.length - 1][][];
        for (int layer = 0; layer < layers.length - 1; layer++) {
            weights[layer] = new double[layers[layer]][];
            for (int neuron = 0; neuron < weights[layer].length; neuron++) {
                weights[layer][neuron] = Arrays.stream(lines.next().split(" ")).mapToDouble(Double::valueOf).toArray();
            }
        }
        return new Network(layers, biases, weights, 10, 0.01, 1);
    }

    public void toFile(File file) {
        file.getParentFile().mkdirs();
        try {
            Files.write(file.toPath(), serializeToReadableString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Network fromFile(File file) {
        System.out.println(file.getAbsolutePath());
        try {
            return readFromReadableString(Files.readAllLines(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
