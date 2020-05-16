package com.pgrela.neural.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Output {
    private List<Double> values;

    public Output(List<Double> values) {
        this.values = new ArrayList<>(values);
    }

    public static Output probe(List<? extends Probe> probes) {
        return new Output(
                probes.stream()
                        .mapToDouble(Probe::value)
                        .boxed()
                        .collect(Collectors.toList())
        );
    }

    public int size() {
        return values.size();
    }

    public double get(int i) {
        return values.get(i);
    }
}
