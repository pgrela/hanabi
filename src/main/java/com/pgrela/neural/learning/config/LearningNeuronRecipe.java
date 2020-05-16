package com.pgrela.neural.learning.config;

import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.core.config.NeuronRecipe;

import java.util.function.DoubleUnaryOperator;

public class LearningNeuronRecipe extends NeuronRecipe {
    private DoubleUnaryOperator derivative;

    public LearningNeuronRecipe(NeuralRandom random, DoubleUnaryOperator activationFunction, DoubleUnaryOperator derivative) {
        super(random, activationFunction);
        this.derivative = derivative;
    }

    public DoubleUnaryOperator getDerivative() {
        return derivative;
    }

    public static class Builder extends NeuronRecipe.AbstractBuilder<Builder, LearningNeuronRecipe> {
        protected DoubleUnaryOperator derivative;

        public Builder() {
            super(LearningNeuronRecipe.Builder.class);
        }

        public Builder setDerivative(DoubleUnaryOperator derivative) {
            this.derivative = derivative;
            return myself;
        }

        public LearningNeuronRecipe build() {
            return new LearningNeuronRecipe(random, activationFunction, derivative);
        }
    }
}
