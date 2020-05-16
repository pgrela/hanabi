package com.pgrela.neural.learning;

import com.pgrela.neural.learning.config.LearningConfig;
import com.pgrela.neural.core.Input;
import com.pgrela.neural.core.Network;
import com.pgrela.neural.core.Output;

import java.util.Collection;
import java.util.List;

public class LearningNetwork {
    private Network network;
    private final Collection<LearningBiasedNeuron> neurons;
    private final List<LearningProbe> probes;
    private LearningConfig config;
    private List<LearningSynapse> synapses;
    private Output output;

    public LearningNetwork(Network network,
                           List<LearningSynapse> synapses,
                           Collection<LearningBiasedNeuron> neurons,
                           List<LearningProbe> probes,
                           LearningConfig config) {
        this.network = network;
        this.synapses = synapses;
        this.neurons = neurons;
        this.probes = probes;
        this.config = config;
    }

    public Output process(Input input) {
        return network.process(input);
    }

    public double learn(Input input, Output expectedOutput) {
        output = process(input);
        double cost = 0;
        for (int i = 0; i < expectedOutput.size(); i++) {
            LearningProbe learningProbe = probes.get(i);
            double expectedValue = expectedOutput.get(i);
            double singleCost = expectedValue - learningProbe.value();
            learningProbe.expectToBe(expectedValue, config);
            cost += singleCost * singleCost;
        }
        return cost;
    }

    public Output output(){
        return output;
    }

    public void summariseKnowledge() {
        for (LearningBiasedNeuron neuron : neurons) {
            neuron.summariseKnowledge();
        }
        for (LearningSynapse synapse : synapses) {
            synapse.summariseKnowledge();
        }
    }
}
