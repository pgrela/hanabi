package com.pgrela.neural.core;

public interface Neuron extends Axon, Dendrite {
    void connectDendrite(Dendrite dendrite);

    void connectAxon(Axon axon);

    double value();

    void reset();
}
