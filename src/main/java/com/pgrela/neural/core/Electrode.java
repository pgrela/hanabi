package com.pgrela.neural.core;

public interface Electrode extends Axon {
    void spark(double value);

    void connectTo(Dendrite dendrite);

    double value();
}
