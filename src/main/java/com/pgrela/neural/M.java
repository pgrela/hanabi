package com.pgrela.neural;

public class M {

    public static final int LEARNING_SEED = 8;
    public static final int TESTING_SEED = 3;
    public static final int LEARNING_SET = 100;
    public static final int TESTING_SET = LEARNING_SET;
    public static final int LEARNING_SET_ITERATIONS = 10000;
    public static final int INPUT_BITS = 8;
    public static final int OUTPUT_BITS = 2;

    public static void main(String[] args) {
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
//                        .setActivationFunction(Activation.SIGMA_0_1)
//                        .build())
//                .build();
//        LearningNetwork network = new LearningNetwork(config);
//
//        for (int learningIteration = 0; learningIteration < LEARNING_SET_ITERATIONS; learningIteration++) {
//            double cost = learn(network);
//            System.out.println(cost);
//        }
//        System.out.println(test(network));
//        System.out.println(network.process(List.of(1d,0d,1d,0d,1d,0d,0d,0d)));
//    }
//
//    private static double learn(LearningNetwork network) {
//        Random learningSetRandom = new Random(LEARNING_SEED);
//        double cost = 0;
//        for (int i = 0; i < LEARNING_SET; i++) {
//            int n = learningSetRandom.nextInt(1 << INPUT_BITS);
//            List<Double> input = binary8(n);
//            double div3 = input.stream().reduce(0d, Double::sum)> 2.5f ? 1d : 0d;
//            List<Double> output = List.of(div3, 1 - div3);
//            //cost += network.learn(input, output);
//            cost += network.learn(input, input);
//            network.reset();
//        }
//        network.summariseKnowledge();
//        return cost / LEARNING_SET;
//    }
//
//    private static double test(LearningNetwork network) {
//        Random testingSetRandom = new Random(TESTING_SEED);
//        double cost = 0;
//        for (int i = 0; i < TESTING_SET; i++) {
//            int n = testingSetRandom.nextInt(1 << 8);
//            List<Double> input = binary8(n);
//            double div3 = input.stream().reduce(0d, Double::sum)> 2.5f ? 1d : 0d;
//            List<Double> expected = List.of(div3, 1 - div3);
//            expected = input;
//            List<Double> answer = network.process(input);
//
//            cost += LearningNetwork.cost(answer, expected);
//            network.reset();
//        }
//        return cost / TESTING_SET;
//    }
//
//
//    static List<Double> binary8(int n) {
//        return IntStream.range(0, INPUT_BITS)
//                .map(i -> 1 << i)
//                .map(i -> (i & n) > 0 ? 1 : 0)
//                .mapToObj(i -> (double) i)
//                .collect(Collectors.toUnmodifiableList());
    }
}
