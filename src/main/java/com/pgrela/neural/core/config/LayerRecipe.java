package com.pgrela.neural.core.config;

import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.core.Neuron;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class LayerRecipe<NEURON_RECIPE> {
    private final NeuralRandom random;
    private final Function<NEURON_RECIPE, Neuron> neuronFactory;
    private final NEURON_RECIPE neuronRecipe;
    private final int size;

    public LayerRecipe(NeuralRandom random, Function<NEURON_RECIPE, Neuron> neuronFactory, NEURON_RECIPE neuronRecipe, int size) {
        this.random = random;
        this.neuronFactory = neuronFactory;
        this.neuronRecipe = neuronRecipe;
        this.size = size;
    }

    public NeuralRandom getRandom() {
        return random;
    }

    public Function<NEURON_RECIPE, Neuron> neuronFactory() {
        return neuronFactory;
    }

    public int size() {
        return size;
    }

    public NEURON_RECIPE neuronRecipe() {
        return neuronRecipe;
    }

    public NEURON_RECIPE getNeuronRecipe() {
        return neuronRecipe;
    }

    public Function<NEURON_RECIPE, Neuron> getNeuronFactory() {
        return neuronFactory;
    }

    public int getSize() {
        return size;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractBuilder<Builder, LayerRecipe<NeuronRecipe>, NeuronRecipe, NeuronRecipe.Builder> {

        public Builder() {
            super(Builder.class);
        }

        @Override
        public LayerRecipe<NeuronRecipe> build() {
            return new LayerRecipe<NeuronRecipe>(random, neuronFactory, neuronRecipe().build(), size);
        }

        @Override
        public NeuronRecipe.Builder newNeuronBuilder() {
            return new NeuronRecipe.Builder();
        }
    }

    public static abstract class AbstractBuilder<
            SELF extends LayerRecipe.AbstractBuilder<SELF, LAYER_RECIPE, NEURON_RECIPE, NEURON_BUILDER>,
            LAYER_RECIPE extends LayerRecipe<NEURON_RECIPE>,
            NEURON_RECIPE,
            NEURON_BUILDER extends NeuronRecipe.AbstractBuilder<NEURON_BUILDER, NEURON_RECIPE>>
            extends ExtendableBuilder<SELF, LAYER_RECIPE> {
        protected NeuralRandom random;
        protected Function<NEURON_RECIPE, Neuron> neuronFactory;
        protected UnaryOperator<NEURON_BUILDER> neuronRecipeBuilder;
        protected int size;
        private DoubleUnaryOperator activationFunction;

        public AbstractBuilder(Class<SELF> myself) {
            super(myself);
        }

        public SELF setActivationFunction(DoubleUnaryOperator activationFunction) {
            this.activationFunction = activationFunction;
            return myself;
        }

        public SELF setRandom(NeuralRandom random) {
            this.random = random;
            return myself;
        }

        public SELF setNeuronFactory(Function<NEURON_RECIPE, Neuron> neuronFactory) {
            this.neuronFactory = neuronFactory;
            return myself;
        }

        public SELF setNeuronRecipe(UnaryOperator<NEURON_BUILDER> neuronRecipeBuilder) {
            this.neuronRecipeBuilder = neuronRecipeBuilder;
            return myself;
        }

        protected NEURON_BUILDER neuronRecipe(){
            NEURON_BUILDER builder = neuronRecipeBuilder.apply(newNeuronBuilder());
            if(activationFunction!=null) {
                builder.setActivationFunction(activationFunction);
            }
            return builder;
        }

        public SELF setSize(int size) {
            this.size = size;
            return myself;
        }

        public NeuralRandom getRandom() {
            return random;
        }

        public Function<NEURON_RECIPE, Neuron> getNeuronFactory() {
            return neuronFactory;
        }

        public abstract NEURON_BUILDER newNeuronBuilder();

        public int getSize() {
            return size;
        }
    }

}
