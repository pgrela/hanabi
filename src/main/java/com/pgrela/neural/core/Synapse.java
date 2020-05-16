package com.pgrela.neural.core;

public class Synapse implements Dendrite, Axon {
    private final Axon input;
    private final Dendrite output;
    double weight;

    private Synapse( Axon input, Dendrite output, double weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }

    public static Synapse connect(Neuron input, Neuron output, double weight) {
        Synapse synapse = new Synapse(input, output, weight);
        input.connectAxon(synapse);
        output.connectAxon(synapse);
        return synapse;
    }

    public static Synapse connect(Electrode input, Neuron output, double weight) {
        Synapse synapse = new Synapse(input, output, weight);
        output.connectAxon(synapse);
        return synapse;
    }

    @Override
    public void propagate(double a) {
        output.propagate(a * weight);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Axon getInput() {
        return input;
    }

    public Dendrite getOutput() {
        return output;
    }
}
