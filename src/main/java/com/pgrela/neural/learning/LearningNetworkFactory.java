package com.pgrela.neural.learning;

import com.pgrela.neural.core.Network;
import com.pgrela.neural.core.SimpleElectrode;
import com.pgrela.neural.core.config.NetworkRecipe;
import com.pgrela.neural.learning.config.LearningLayerRecipe;
import com.pgrela.neural.learning.config.LearningNetworkRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LearningNetworkFactory {
    public LearningNetwork create(LearningNetworkRecipe recipe){

        List<SimpleElectrode> electrodes = IntStream.range(0, recipe.getInputLayerSize())
                .mapToObj(i -> SimpleElectrode.electrode())
                .collect(Collectors.toList());
        List<LearningLayerRecipe> layerRecipes = recipe.getLayerRecipes();
        List<List<LearningBiasedNeuron>> layers = new ArrayList<>();
        for (LearningLayerRecipe layerRecipe : layerRecipes) {
            layers.add(createNeurons(layerRecipe));
        }
        List<LearningBiasedNeuron> output = createNeurons(recipe.getOutputLayer());

        List<LearningProbe> probes = output.stream().map(LearningProbe::new).collect(Collectors.toList());

        List<LearningSynapse> synapses = new ArrayList<>();
        for (SimpleElectrode electrode : electrodes) {
            for (LearningBiasedNeuron neuron : layers.get(0)) {
                LearningSynapse synapse = LearningSynapse.connect(electrode, neuron, recipe.getRandom().nextWeight());
                synapses.add(synapse);
            }
        }
        for (int i = 0; i < layers.size() - 1; i++) {
            synapses.addAll(connectAllToAll(layers.get(i), layers.get(i + 1), recipe));
        }
        synapses.addAll(connectAllToAll(layers.get(layers.size()-1), output, recipe));
        Collection<LearningBiasedNeuron> neurons = layers.stream().flatMap(Collection::stream).collect(Collectors.toList());
        neurons.addAll(output);
        Network network = new Network(electrodes, probes, neurons);
        return new LearningNetwork(network, synapses, neurons, probes, recipe);
    }

    private List<LearningBiasedNeuron> createNeurons(LearningLayerRecipe layerRecipe) {
        List<LearningBiasedNeuron> neurons = new ArrayList<>();
        for (int i = 0; i < layerRecipe.size(); i++) {
            neurons.add(LearningBiasedNeuron.neuron(layerRecipe.neuronRecipe()));
        }
        return neurons;
    }

    private List<LearningSynapse> connectAllToAll(List<LearningBiasedNeuron> input, List<LearningBiasedNeuron> output, NetworkRecipe recipe) {
        List<LearningSynapse> synapses = new ArrayList<>();
        for (LearningBiasedNeuron inputNeuron : input) {
            for (LearningBiasedNeuron outputNeuron : output) {
                LearningSynapse synapse = LearningSynapse.connect(inputNeuron, outputNeuron, recipe.getRandom().nextBias());
                synapses.add(synapse);
            }
        }
        return synapses;
    }
}
