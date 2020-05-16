package com.pgrela.neural.fastfloat;

import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.utils.Activation;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Network {
    private float[][] biases;
    private float[][][] weights;
    private float[][] values;
    private float[][] sensitivity;
    private float[][] inputSums;
    private NeuralRandom random = new NeuralRandom(4);
    private float learningFactor;
    private int[] layers;
    private int outputLayer;
    private int batch;
    private int batchNo = 0;
    private final float[][][] expectedBiases;
    private final float[][][][] expectedWeights;

    public Network(int batch, float learningFactor, int... layers) {
        this.batch = batch;
        this.learningFactor = learningFactor;
        this.layers = layers;
        int inputSize = layers[0];
        outputLayer = layers.length - 1;
        int outputSize = layers[layers.length - 1];
        biases = new float[layers.length][];
        for (int layer = 1; layer < layers.length; layer++) {
            biases[layer] = new float[layers[layer]];
            for (int j = 0; j < biases[layer].length; j++) {
                biases[layer][j] = (float) random.nextBias();
            }
        }
        weights = new float[layers.length - 1][][];
        for (int layer = 0; layer < layers.length - 1; layer++) {
            weights[layer] = new float[layers[layer]][];
            for (int neuronInPreviousLayer = 0; neuronInPreviousLayer < layers[layer]; neuronInPreviousLayer++) {
                weights[layer][neuronInPreviousLayer] = new float[layers[layer + 1]];
                for (int neuronInNextLayer = 0; neuronInNextLayer < layers[layer + 1]; neuronInNextLayer++) {
                    weights[layer][neuronInPreviousLayer][neuronInNextLayer] = (float) random.nextWeight();
                }
            }
        }
        values = new float[layers.length][];
        for (int i = 1; i < layers.length; i++) {
            values[i] = new float[layers[i]];
        }
        sensitivity = Arrays.stream(layers).mapToObj(float[]::new).toArray(float[][]::new);
        inputSums = Arrays.stream(layers).mapToObj(float[]::new).toArray(float[][]::new);
        expectedBiases = Arrays.stream(layers)
                .mapToObj(size -> IntStream.range(0, size).mapToObj(i -> new float[batch]).toArray(float[][]::new))
                .toArray(float[][][]::new);
        expectedWeights = IntStream.range(0, layers.length - 1).mapToObj(fromLayer ->
                IntStream.range(0, layers[fromLayer])
                        .mapToObj(j -> IntStream.range(0, layers[fromLayer + 1]).mapToObj(i -> new float[batch]).toArray(float[][]::new))
                        .toArray(float[][][]::new)).toArray(float[][][][]::new);
    }

    private void zero(float[][] a) {
        for (int i = 0; i < a.length; i++) {
            Arrays.fill(a[i], 0);
        }
    }

    private void zero(float[][][] a) {
        for (int i = 0; i < a.length; i++) {
            zero(a[i]);
        }
    }

    private void zero(float[][][][] a) {
        for (int i = 0; i < a.length; i++) {
            zero(a[i]);
        }
    }

    public float[] process(float[] input) {
        values[0] = input;
        for (int nextLayer = 1; nextLayer < layers.length; nextLayer++) {
            for (int currentNeuron = 0; currentNeuron < layers[nextLayer]; currentNeuron++) {
                float inputSum = 0;
                for (int i = 0; i < layers[nextLayer - 1]; i++) {
                    inputSum += values[nextLayer - 1][i] * weights[nextLayer - 1][i][currentNeuron];
                }
                inputSums[nextLayer][currentNeuron] = inputSum;//
                float value = inputSum + biases[nextLayer][currentNeuron];
                values[nextLayer][currentNeuron] = nextLayer == outputLayer ? Activation.SIGMA_0_1.function(value) : Activation.SIGMA_1_0_1.function(value);
            }
        }
        return values[outputLayer];
    }

    public float learn(float[] input, float[] output) {
        zero(inputSums);
        zero(sensitivity);
        process(input);
        float cost = 0;
        for (int i = 0; i < layers[outputLayer]; i++) {
            float singlecost = (values[outputLayer][i] - output[i]);
            sensitivity[outputLayer][i] = 2 * singlecost;
            cost += singlecost * singlecost;
        }
        for (int currentLayer = outputLayer; currentLayer > 0; currentLayer--) {
            int previousLayer = currentLayer - 1;
            for (int i = 0; i < layers[currentLayer]; i++) {
                float inputSumPlusB = inputSums[currentLayer][i] + biases[currentLayer][i];
                float derivative = currentLayer == outputLayer ? Activation.SIGMA_0_1.derivative(inputSumPlusB) : Activation.SIGMA_1_0_1.derivative(inputSumPlusB);
                float internalSensitivity = derivative * sensitivity[currentLayer][i];
                expectedBiases[currentLayer][i][batchNo] = biases[currentLayer][i] - internalSensitivity * learningFactor;
                //biases[currentLayer][i] -= internalSensitivity * learningFactor;
                for (int j = 0; j < layers[previousLayer]; j++) {
                    float weightSensitivity = internalSensitivity * values[previousLayer][j];
                    sensitivity[previousLayer][j] += weightSensitivity * weights[previousLayer][j][i];
                    expectedWeights[previousLayer][j][i][batchNo] = weights[previousLayer][j][i] - weightSensitivity * learningFactor;
                    //weights[previousLayer][j][i] -= weightSensitivity * learningFactor;
                }
            }
        }
        ++batchNo;
        if(batchNo==batch){
            summarize();
            batchNo=0;
        }
        return cost;
    }

    private void summarize() {
        for (int previousLayer = 0; previousLayer < layers.length - 1; previousLayer++) {
            int currentLayer = previousLayer+1;
            for (int i = 0; i < layers[currentLayer]; i++) {
                biases[currentLayer][i] = avg(expectedBiases[currentLayer][i]);
                for (int j = 0; j < layers[previousLayer]; j++) {
                    weights[previousLayer][j][i] = avg(expectedWeights[previousLayer][j][i]);
                }
            }
        }
    }

    private float avg(float[] floats) {
        float sum = 0;
        for (float aDouble : floats) {
            sum += aDouble;
        }
        return sum/floats.length;
    }

    public float[] output() {
        return values[outputLayer];
    }
}
