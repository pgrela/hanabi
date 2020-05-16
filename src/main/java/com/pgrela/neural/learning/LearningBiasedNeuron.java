package com.pgrela.neural.learning;

import com.pgrela.neural.core.Axon;
import com.pgrela.neural.core.BiasedNeuron;
import com.pgrela.neural.core.Dendrite;
import com.pgrela.neural.core.Neuron;
import com.pgrela.neural.learning.config.LearningConfig;
import com.pgrela.neural.learning.config.LearningNeuronRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class LearningBiasedNeuron implements Neuron {
    private final BiasedNeuron neuron;
    private final List<LearningSynapse> axons = new ArrayList<>();
    private final DoubleUnaryOperator derivative;
    private int dendrites=0;

    private double outputSensitivity = 0;
    private double bInputSumSensitivity = 0;
    private int notes = 0;

    private Collection<Double> expectedBs = new ArrayList<>(100);

    public static LearningBiasedNeuron neuron(LearningNeuronRecipe config) {
        return new LearningBiasedNeuron(config.getDerivative(), BiasedNeuron.neuron(config));
    }

    public double getbInputSumSensitivity() {
        return bInputSumSensitivity;
    }

    public LearningBiasedNeuron(DoubleUnaryOperator derivative, BiasedNeuron neuron) {
        this.derivative = derivative;
        this.neuron = neuron;
    }

    @Override
    public void propagate(double ax) {
        neuron.propagate(ax);
    }

    @Override
    public void connectDendrite(Dendrite dendrite) {
        neuron.connectDendrite(dendrite);
        ++dendrites;
    }

    @Override
    public void connectAxon(Axon axon) {
        if (!(axon instanceof LearningSynapse)) {
            throw new IllegalArgumentException();
        }
        neuron.connectAxon(axon);
        axons.add((LearningSynapse) axon);
    }

    @Override
    public double value() {
        return neuron.value();
    }

    public void summariseKnowledge() {
        if (expectedBs.size() > 0) {
            neuron.setBias(expectedBs.stream().reduce(0d, Double::sum) / expectedBs.size());
        }
        expectedBs.clear();
    }

    @Override
    public void reset() {
        neuron.reset();
        notes = 0;
        outputSensitivity = 0;
    }

    public void noteSensitivity(double sensitivity, LearningConfig learningConfig) {
        outputSensitivity += sensitivity;
        ++notes;
        if (notes == dendrites) {
            learn(learningConfig);
        }
    }

    private void learn(LearningConfig learningConfig) {
        double sigSensitivity = derivative.applyAsDouble(neuron.getBias() + neuron.getInputSum()) * outputSensitivity;
        double bSensitivity = sigSensitivity;
        double inputSumSensitivity = sigSensitivity;
        bInputSumSensitivity = sigSensitivity;

        for (int i = 0; i < axons.size(); i++) {
            axons.get(i).noteSensitivity(inputSumSensitivity, learningConfig);
        }

        expectedBs.add(neuron.getBias() - bSensitivity * learningConfig.getLearningFactor());

    }

    @Override
    public String toString() {
        return "learning";
    }
}
