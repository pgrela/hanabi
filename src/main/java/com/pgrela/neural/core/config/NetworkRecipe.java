package com.pgrela.neural.core.config;

import com.pgrela.neural.core.BiasedNeuron;
import com.pgrela.neural.core.NeuralRandom;
import com.pgrela.neural.utils.Activation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class NetworkRecipe<LAYER_RECIPE extends LayerRecipe<NEURON_RECIPE>, NEURON_RECIPE extends NeuronRecipe> {
    private final NeuralRandom neuralRandom;
    private final int inputLayerSize;
    private final List<LAYER_RECIPE> layerRecipes;
    private final LAYER_RECIPE outputLayer;

    protected NetworkRecipe(NeuralRandom neuralRandom, int inputLayerSize, List<LAYER_RECIPE> layerRecipes, LAYER_RECIPE outputLayer) {
        this.neuralRandom = neuralRandom;
        this.inputLayerSize = inputLayerSize;
        this.layerRecipes = layerRecipes;
        this.outputLayer = outputLayer;
    }

    public List<LAYER_RECIPE> getLayerRecipes() {
        return layerRecipes;
    }

    public NeuralRandom getRandom() {
        return neuralRandom;
    }

    public int getInputLayerSize() {
        return inputLayerSize;
    }

    public LAYER_RECIPE getOutputLayer() {
        return outputLayer;
    }

    public static class Builder extends AbstractBuilder<Builder,
            NetworkRecipe<LayerRecipe<NeuronRecipe>, NeuronRecipe>,
            LayerRecipe<NeuronRecipe>, NeuronRecipe, LayerRecipe.Builder, NeuronRecipe.Builder> {
        public Builder() {
            super(Builder.class);
        }

        @Override
        protected LayerRecipe.Builder layerBuilder() {
            return LayerRecipe.builder();
        }

        @Override
        protected NeuronRecipe.Builder neuronBuilder() {
            return null;
        }

        @Override
        public NetworkRecipe<LayerRecipe<NeuronRecipe>, NeuronRecipe> build() {
            return null;
        }
    }

    protected static abstract class AbstractBuilder<
            SELF extends AbstractBuilder<SELF, NETWORK_RECIPE, LAYER_RECIPE, NEURON_RECIPE, LAYER_BUILDER, NEURON_BUILDER>,
            NETWORK_RECIPE extends NetworkRecipe<LAYER_RECIPE, NEURON_RECIPE>,
            LAYER_RECIPE extends LayerRecipe<NEURON_RECIPE>,
            NEURON_RECIPE extends NeuronRecipe,
            LAYER_BUILDER extends LayerRecipe.AbstractBuilder<LAYER_BUILDER, LAYER_RECIPE, NEURON_RECIPE, NEURON_BUILDER>,
            NEURON_BUILDER extends NeuronRecipe.AbstractBuilder<NEURON_BUILDER, NEURON_RECIPE>>
            extends ExtendableBuilder<SELF, NETWORK_RECIPE> {

        private List<UnaryOperator<LAYER_BUILDER>> layerRecipes = new ArrayList<>();
        protected NeuralRandom neuralRandom;
        protected int inputLayerSize;
        private UnaryOperator<LAYER_BUILDER> outputLayer;

        public AbstractBuilder(Class<SELF> myself) {
            super(myself);
        }

        protected abstract LAYER_BUILDER layerBuilder();

        protected abstract NEURON_BUILDER neuronBuilder();

        public SELF addLayerRecipe(UnaryOperator<LAYER_BUILDER> layerBuilder) {
            this.layerRecipes.add(layerBuilder);
            return myself;
        }

        public SELF setRandom(NeuralRandom neuralRandom) {
            this.neuralRandom = neuralRandom;
            return myself;
        }

        public SELF setRandom(int neuralRandom) {
            this.neuralRandom = new NeuralRandom(neuralRandom);
            return myself;
        }

        public SELF setInputLayerSize(int inputLayerSize) {
            this.inputLayerSize = inputLayerSize;
            return myself;
        }

        public SELF setOutputLayer(UnaryOperator<LAYER_BUILDER> outputLayer) {
            this.outputLayer = outputLayer;
            return myself;
        }

        protected List<LAYER_RECIPE> layerRecipes() {
            return layerRecipes.stream().map(layer ->
                    layer.apply(defaultLayerBuilder(layerBuilder()
                            .setRandom(neuralRandom)
                            .setNeuronFactory(BiasedNeuron::neuron)
                            .setNeuronRecipe(builder -> builder.setActivationFunction(Activation.SIGMA_1_0_1::function)
                                    .setRandom(neuralRandom)))))
                    .map(LAYER_BUILDER::build).collect(Collectors.toList());

        }

        protected LAYER_RECIPE outputLayerRecipe() {
            return outputLayer.apply(outputLayerBuilder(layerBuilder().setRandom(neuralRandom)
                    .setNeuronFactory(BiasedNeuron::neuron)
                    .setNeuronRecipe(builder -> builder.setActivationFunction(Activation.SIGMA_0_1::function)
                            .setRandom(neuralRandom)))).build();

        }


        protected LAYER_BUILDER defaultLayerBuilder(LAYER_BUILDER layerBuilder) {
            return layerBuilder;
        }

        protected LAYER_BUILDER outputLayerBuilder(LAYER_BUILDER layerBuilder) {
            return layerBuilder;
        }

    }
}
