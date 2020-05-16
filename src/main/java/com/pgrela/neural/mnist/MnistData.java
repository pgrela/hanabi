package com.pgrela.neural.mnist;

import com.pgrela.neural.core.Input;
import com.pgrela.neural.core.Output;
import com.pgrela.neural.training.TrainingSample;
import com.pgrela.neural.training.TrainingSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MnistData {

    private static final Path DIR = Path.of(System.getProperty("user.home"), "tmp");
    private static final String LEARN_IMAGES = Path.of(DIR.toString(), "train-images.idx3-ubyte").toString();
    private static final String LEARN_LABELS = Path.of(DIR.toString(), "train-labels.idx1-ubyte").toString();
    private static final String TEST_IMAGES = Path.of(DIR.toString(), "t10k-images.idx3-ubyte").toString();
    private static final String TEST_LABELS = Path.of(DIR.toString(), "t10k-labels.idx1-ubyte").toString();

    public TrainingSet learningSet(int size) {
        return readSamples(size, LEARN_IMAGES, LEARN_LABELS);
    }

    public TrainingSet testingSet(int size) {
        return readSamples(size, TEST_IMAGES, TEST_LABELS);
    }

    private TrainingSet readSamples(int size, String inputImagePath, String inputLabelPath) {
        FileInputStream inImage = null;
        FileInputStream inLabel = null;
        TrainingSet trainingSet = new TrainingSet();
        try {
            inImage = new FileInputStream(inputImagePath);
            inLabel = new FileInputStream(inputLabelPath);

            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

            int numberOfPixels = numberOfRows * numberOfColumns;
            Double[] imgPixels = new Double[numberOfPixels];


            for (int i = 1; i <= size; i++) {
                for (int p = 0; p < numberOfPixels; p++) {
                    imgPixels[p] = inImage.read() / 255d;
//                    int gray = 255 - inImage.read();
//                    imgPixels[p] = gray / 128d - 1;
                }

                int label = inLabel.read();

                List<Double> input = new ArrayList<>(Arrays.asList(imgPixels));
                List<Double> output = digit(label);
                trainingSet.addSample(new TrainingSample(new Input(input), new Output(output)));
                if (i > 0 && i % 200 == 0) System.out.printf("Loaded %d samples from %s\n", i, inputImagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RuntimeException retrhown = null;
            if (inImage != null) {
                try {
                    inImage.close();
                } catch (IOException e) {
                    retrhown = new RuntimeException(e);
                }
            }
            if (inLabel != null) {
                try {
                    inLabel.close();
                } catch (IOException e) {
                    retrhown = new RuntimeException(e);
                }
            }
            if (retrhown != null) {
                throw retrhown;
            }
        }
        return trainingSet;
    }

    static List<Double> digit(int n) {
        Double[] floats = new Double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d};
        floats[n] = 1d;
        return Arrays.asList(floats);
    }

}
