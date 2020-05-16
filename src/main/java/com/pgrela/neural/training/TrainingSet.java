package com.pgrela.neural.training;

import java.util.ArrayList;
import java.util.List;

public class TrainingSet {
    private final List<TrainingSample> samples;

    public TrainingSet() {
        this(new ArrayList<>());
    }

    public TrainingSet(List<TrainingSample> samples) {
        this.samples = samples;
    }

    public List<TrainingSample> getSamples() {
        return samples;
    }

    public void addSample(TrainingSample sample) {
        samples.add(sample);
    }
}
