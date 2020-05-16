package com.pgrela.neural.learning;

import com.pgrela.neural.learning.config.LearningConfig;
import com.pgrela.neural.core.Probe;

public class LearningProbe implements Probe {
    private LearningBiasedNeuron neuron;
    private double lastRecordedValue;

    public LearningProbe(LearningBiasedNeuron neuron) {
        this.neuron = neuron;
        neuron.connectDendrite(this);
    }

    @Override
    public double value() {
        return lastRecordedValue;
    }

    public void expectToBe(double expected, LearningConfig learningConfig) {
        double sensitivity = 2 * (lastRecordedValue - expected);
        neuron.noteSensitivity(sensitivity, learningConfig);
    }

    @Override
    public void propagate(double value) {
        lastRecordedValue=value;
    }
}
