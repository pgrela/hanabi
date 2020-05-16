package com.pgrela.neural.learning.config;

import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.core.ActivationFunction;
import com.pgrela.neural.core.Neuron;
import com.pgrela.neural.core.config.LayerRecipe;

import java.util.function.Function;

public class LearningLayerRecipe extends LayerRecipe<LearningNeuronRecipe> {

    public LearningLayerRecipe(
            NeuralRandom random,
            Function<LearningNeuronRecipe, Neuron> neuronFactory,
            int size,
            LearningNeuronRecipe neuronRecipe) {
        super(random, neuronFactory, neuronRecipe, size);
    }

    public static class Builder extends LayerRecipe.AbstractBuilder<
            LearningLayerRecipe.Builder,
            LearningLayerRecipe,
            LearningNeuronRecipe,
            LearningNeuronRecipe.Builder> {

        private ActivationFunction activationFunction;

        public Builder() {
            super(LearningLayerRecipe.Builder.class);
        }

        public Builder setActivationFunction(ActivationFunction activationFunction){
            this.activationFunction = activationFunction;
            return myself;
        }

        protected LearningNeuronRecipe.Builder neuronRecipe(){
            LearningNeuronRecipe.Builder builder = super.neuronRecipe();
            if(activationFunction!=null) {
                builder.setActivationFunction(activationFunction::function);
                builder.setDerivative(activationFunction::derivative);
            }
            return builder;
        }

        @Override
        public LearningNeuronRecipe.Builder newNeuronBuilder() {
            return new LearningNeuronRecipe.Builder();
        }

        public LearningLayerRecipe build() {
            return new LearningLayerRecipe(random, neuronFactory, size, neuronRecipe().build());
        }
    }
}
