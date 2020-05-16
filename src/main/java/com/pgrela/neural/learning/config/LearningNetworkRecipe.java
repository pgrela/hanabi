package com.pgrela.neural.learning.config;

import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.core.config.NetworkRecipe;
import com.pgrela.neural.utils.Activation;

import java.util.List;

public class LearningNetworkRecipe extends NetworkRecipe<LearningLayerRecipe, LearningNeuronRecipe> implements LearningConfig {
    private final double learningFactor;

    protected LearningNetworkRecipe(NeuralRandom neuralRandom,
                                    int inputLayerSize,
                                    List<LearningLayerRecipe> learningLayerRecipes, LearningLayerRecipe outputLayer, double learningFactor) {
        super(neuralRandom, inputLayerSize, learningLayerRecipes, outputLayer);
        this.learningFactor = learningFactor;
    }

    @Override
    public double getLearningFactor() {
        return learningFactor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends NetworkRecipe.AbstractBuilder<Builder, LearningNetworkRecipe, LearningLayerRecipe, LearningNeuronRecipe,
            LearningLayerRecipe.Builder, LearningNeuronRecipe.Builder> {
        protected double learningFactor;

        private Builder() {
            super(Builder.class);
        }

        public Builder setLearningFactor(double learningFactor) {
            this.learningFactor = learningFactor;
            return myself;
        }

        public LearningNetworkRecipe build() {
            return new LearningNetworkRecipe(neuralRandom, inputLayerSize, layerRecipes(), outputLayerRecipe(), learningFactor);
        }

        @Override
        protected LearningLayerRecipe.Builder layerBuilder() {
            return new LearningLayerRecipe.Builder();
        }

        @Override
        protected LearningNeuronRecipe.Builder neuronBuilder() {
            return new LearningNeuronRecipe.Builder();
        }

        @Override
        protected LearningLayerRecipe.Builder defaultLayerBuilder(LearningLayerRecipe.Builder builder) {
            return super.defaultLayerBuilder(builder).setActivationFunction(Activation.SIGMA_1_0_1);
        }

        @Override
        protected LearningLayerRecipe.Builder outputLayerBuilder(LearningLayerRecipe.Builder builder) {
            return super.outputLayerBuilder(builder).setActivationFunction(Activation.SIGMA_0_1);
        }
    }
}
