package com.pgrela.neural.core;

import java.util.Collection;
import java.util.List;

public class Network {
    private final List<? extends Electrode> electrodes;
    private final List<? extends Probe> probes;
    private final Collection<? extends Neuron> neurons;

    public Network(List<? extends Electrode> electrodes, List<? extends Probe> probes, Collection<? extends Neuron> neurons) {
        this.electrodes = electrodes;
        this.probes = probes;
        this.neurons = neurons;
    }

    public Output process(Input input) {
        if (input.size() != electrodes.size()) {
            throw new IllegalArgumentException();
        }
        reset();
        for (int i = 0; i < electrodes.size(); i++) {
            electrodes.get(i).spark(input.get(i));
        }
        return Output.probe(probes);
    }

    private void reset() {
        neurons.forEach(Neuron::reset);
    }
}
