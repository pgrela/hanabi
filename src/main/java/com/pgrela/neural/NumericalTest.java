package com.pgrela.neural;

public class NumericalTest {

    public static void main(String[] args) {
//
//        NeuralRandom networkRandom = new NeuralRandom(4);
//        NetworkRecipe config = NetworkRecipe.builder()
//                .setRandom(networkRandom)
//                .setLearningFactor(0.1d)
//                .setActivationFunction(Activation.SIGMA_1_0_1)
//                .addLayer(LayerRecipe.builder()
//                        .setNeuronFactory(SimpleElectrode::neuron)
//                        .setSize(8)
//                        .setRandom(networkRandom)
//                        .setActivationFunction(Activation.SIGMA_1_0_1)
//                        .build())
//                .addLayer(LayerRecipe.builder()
//                        .setNeuronFactory(BiasedNeuron::neuron)
//                        .setSize(8)
//                        .setRandom(networkRandom)
//                        .setActivationFunction(Activation.SIGMA_1_0_1)
//                        .build())
//                .addLayer(LayerRecipe.builder()
//                        .setNeuronFactory(BiasedNeuron::neuron)
//                        .setSize(8)
//                        .setRandom(networkRandom)
//                        .setActivationFunction(Activation.SIGMA_1_0_1)
//                        .build())
//                .build();
//        LearningNetwork network = new LearningNetwork(config);
//
//        for (int i = 0; i < 10; i++) {
//            int n = 13;
//            for (int j = 0; j < 10; j++) {
//                n = (n * 17 + 21) % 256;
//                List<Double> inputOutput = M.binary8(n);
//                network.learn(inputOutput, inputOutput);
//                network.reset();
//            }
//            network.summariseKnowledge();
//        }
//
//        List<Double> inputOutput = M.binary8(113);
//        double cost = network.learn(inputOutput, inputOutput);
//        System.out.println(cost);
//        double delta = 0.00000001d;
//        for (Layer layer : network.layers()) {
//            for (Neuron neuron : layer.neurons()) {
//                if (neuron instanceof BiasedNeuron) {
//                    BiasedNeuron innerNeuron = (BiasedNeuron) neuron;
//                    network.reset();
//                    network.learn(inputOutput, inputOutput);
//                    double outputSensitivity = innerNeuron.getbInputSumSensitivity();
//                    double originalB = innerNeuron.bias;
//                    innerNeuron.bias = originalB - delta;
//                    network.reset();
//                    double costMinus = network.learn(inputOutput, inputOutput);
//                    innerNeuron.bias = originalB + delta;
//                    network.reset();
//                    double costPlus = network.learn(inputOutput, inputOutput);
//                    innerNeuron.bias = originalB;
//                    out(delta, outputSensitivity, costMinus, costPlus);
//                }
//            }
//        }
//        for (Synapse synapse : network.synapses()) {
//            network.reset();
//            network.learn(inputOutput, inputOutput);
//            double outputSensitivity = synapse.getOutputSensitivity();
//            double originalWeight = synapse.weight;
//            synapse.weight = originalWeight - delta;
//            network.reset();
//            double costMinus = network.learn(inputOutput, inputOutput);
//            synapse.weight = originalWeight + delta;
//            network.reset();
//            double costPlus = network.learn(inputOutput, inputOutput);
//            synapse.weight = originalWeight;
//            out(delta, outputSensitivity, costMinus, costPlus);
//        }
//
//    }
//
//    private static void out(double delta, double outputSensitivity, double costMinus, double costPlus) {
//        double percent = (2 * delta * outputSensitivity) / (costPlus - costMinus) * 100 - 100;
//        if (Double.isNaN(percent)) {
//            if (Double.isNaN(outputSensitivity)) {
//                throw new IllegalArgumentException();
//            }
//            if (Math.abs(outputSensitivity) < 0.0000001) {
//                percent = 0;
//            }
//        }
//        if (!Double.isNaN(percent) && Math.abs(percent) > 1) {
//            System.out.print("--------> ");
//        }
//        System.out.format("%3.9f%%\n", percent);
    }
}
