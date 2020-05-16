package com.pgrela.neural.core;

import java.util.ArrayList;
import java.util.List;

public class Input {
    private List<Double> values;

    public Input(List<Double> values) {
        this.values = new ArrayList<>(values);
    }

    public double get(int i) {
        return values.get(i);
    }

    public int size() {
        return values.size();
    }
}
