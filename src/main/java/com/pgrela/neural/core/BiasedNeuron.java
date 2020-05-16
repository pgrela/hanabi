package com.pgrela.neural.core;

import com.pgrela.neural.core.config.NeuronRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class BiasedNeuron implements Neuron {
    private double bias;
    private final DoubleUnaryOperator activationFunction;

    private final List<Dendrite> connectedDendrites = new ArrayList<>();
    private final List<Axon> connectedAxons = new ArrayList<>();

    private double inputSum = 0;
    private double value = 0;
    private int sparks = 0;

    public static BiasedNeuron neuron(NeuronRecipe recipe) {
        return new BiasedNeuron(recipe.getRandom().nextBias(), recipe.getActivationFunction());
    }


    private BiasedNeuron(double bias, DoubleUnaryOperator activationFunction) {
        this.bias = bias;
        this.activationFunction = activationFunction;
    }

    private void fire(double a) {
        for (int i = 0; i < connectedDendrites.size(); i++) {
            connectedDendrites.get(i).propagate(a);
        }
    }

    @Override
    public void propagate(double ax) {
        inputSum += ax;
        if (++sparks == connectedAxons.size()) {
            fire(value = activationFunction.applyAsDouble(inputSum + bias));
        }
    }

    @Override
    public void connectDendrite(Dendrite dendrite) {
        connectedDendrites.add(dendrite);
    }

    @Override
    public void connectAxon(Axon axon) {
        connectedAxons.add(axon);
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public void reset() {
        inputSum = 0;
        value = 0;
        sparks = 0;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getBias() {
        return bias;
    }

    public double getInputSum() {
        return inputSum;
    }

    @Override
    public String toString() {
        return String.format("{v:%.3f (b:%.3f)}", value, bias);
    }
}
