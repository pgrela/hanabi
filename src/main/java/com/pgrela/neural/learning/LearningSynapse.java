package com.pgrela.neural.learning;

import com.pgrela.neural.learning.config.LearningConfig;
import com.pgrela.neural.core.Axon;
import com.pgrela.neural.core.Dendrite;
import com.pgrela.neural.core.Electrode;

import java.util.ArrayList;
import java.util.List;

public class LearningSynapse implements Dendrite, Axon {
    private LearningBiasedNeuron input;
    private Dendrite output;
    double weight = 0;
    double lastValue = 0;

    private double outputSensitivity = 0;
    private List<Double> expectedWeights = new ArrayList<>(100);

    private LearningSynapse(LearningBiasedNeuron input, LearningBiasedNeuron output, double weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }

    public static LearningSynapse connect(LearningBiasedNeuron input, LearningBiasedNeuron output, double weight) {
        LearningSynapse synapse = new LearningSynapse(input, output, weight);
        input.connectDendrite(synapse);
        output.connectAxon(synapse);
        return synapse;
    }

    public static LearningSynapse connect(Electrode input, LearningBiasedNeuron output, double weight) {
        LearningSynapse synapse = new LearningSynapse(null, output, weight);
        input.connectTo(synapse);
        output.connectAxon(synapse);
        return synapse;
    }

    @Override
    public void propagate(double a) {
        lastValue = a;
        output.propagate(a * weight);
    }

    public double weight() {
        return weight;
    }

    public void noteSensitivity(double sensitivity, LearningConfig learningConfig) {
        outputSensitivity = sensitivity * lastValue;
        if (input != null)
            input.noteSensitivity(sensitivity * weight, learningConfig);
        expectedWeights.add(weight - sensitivity * lastValue * learningConfig.getLearningFactor());
    }

    public void reset() {
        outputSensitivity = 0;
    }

    public void summariseKnowledge() {
        if (expectedWeights.size() > 0) {
            weight = expectedWeights.stream().reduce(0d, Double::sum) / expectedWeights.size();
            expectedWeights.clear();
        }
    }

    public double getOutputSensitivity() {
        return outputSensitivity;
    }
}
