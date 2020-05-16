package com.pgrela.neural;

import com.pgrela.neural.learning.LearningNetwork;
import com.pgrela.neural.learning.LearningNetworkFactory;
import com.pgrela.neural.learning.config.LearningNetworkRecipe;
import com.pgrela.neural.mnist.MnistData;
import com.pgrela.neural.training.Trainer;

public class MNIST {

    public static void main(String[] args) {

        LearningNetworkRecipe config = LearningNetworkRecipe.builder()
                .setRandom(4)
                .setInputLayerSize(28 * 28)
                .addLayerRecipe(layer -> layer.setSize(48))
                .setOutputLayer(output -> output
                        .setSize(10))
                .setLearningFactor(0.05d)
                .build();
        LearningNetwork network = new LearningNetworkFactory().create(config);
        MnistData mnistData = new MnistData();
        Trainer trainer = new Trainer(network, mnistData.learningSet(500), mnistData.testingSet(50));
        trainer.train(30, 100);
    }

}
