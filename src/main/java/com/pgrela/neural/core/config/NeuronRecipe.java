package com.pgrela.neural.core.config;

import com.pgrela.neural.core.NeuralRandom;

import java.util.function.DoubleUnaryOperator;

public class NeuronRecipe {
    private NeuralRandom random;
    private DoubleUnaryOperator activationFunction;

    public NeuronRecipe(NeuralRandom random, DoubleUnaryOperator activationFunction) {
        this.random = random;
        this.activationFunction = activationFunction;
    }

    public NeuralRandom getRandom() {
        return random;
    }

    public DoubleUnaryOperator getActivationFunction() {
        return activationFunction;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends NeuronRecipe.AbstractBuilder<Builder, NeuronRecipe> {

        public Builder() {
            super(Builder.class);
        }

        @Override
        public NeuronRecipe build() {
            return new NeuronRecipe(random, activationFunction);
        }
    }

    public static abstract class AbstractBuilder<
            SELF extends AbstractBuilder<SELF, NEURON_RECIPE>,
            NEURON_RECIPE> extends ExtendableBuilder<SELF, NEURON_RECIPE> {
        protected NeuralRandom random;
        protected DoubleUnaryOperator activationFunction;

        public AbstractBuilder(Class<SELF> myself) {
            super(myself);
        }

        public SELF setRandom(NeuralRandom random) {
            this.random = random;
            return myself;
        }

        public SELF setActivationFunction(DoubleUnaryOperator activationFunction) {
            this.activationFunction = activationFunction;
            return myself;
        }
    }
}
