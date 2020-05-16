package com.pgrela.neural.training;

import com.pgrela.neural.core.Input;
import com.pgrela.neural.core.Output;

public class TrainingSample {
    private final Input input;
    private final Output output;

    public TrainingSample(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    public Input getInput() {
        return input;
    }

    public Output getOutput() {
        return output;
    }
}
