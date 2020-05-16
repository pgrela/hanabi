package com.pgrela.neural.core;

import java.util.ArrayList;
import java.util.Collection;

public class SimpleElectrode implements Electrode {
    private Collection<Dendrite> dendrites = new ArrayList<>();

    private double value = 0;
    public static SimpleElectrode electrode() {
        return new SimpleElectrode();
    }

    private SimpleElectrode() {
    }

    @Override
    public void spark(double a) {
        value = a;
        for (Dendrite dendrite : dendrites) {
            dendrite.propagate(value);
        }
    }

    @Override
    public void connectTo(Dendrite dendrite) {
        dendrites.add(dendrite);
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return "SimpleElectrode{" +
                "value=" + value +
                '}';
    }
}
