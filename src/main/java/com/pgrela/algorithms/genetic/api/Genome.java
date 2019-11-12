package com.pgrela.algorithms.genetic.api;

public interface Genome<GENOME extends Genome> {
    GENOME mutate();

    GENOME cross(GENOME partner);

    GENOME deserialize(String genome);

    String serialize();

    GENOME immigrant();

    default GENOME shuffle() {
        return mutate();
    }
}
