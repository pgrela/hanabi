package com.pgrela.algorithms.genetic.api;

public interface Genosaur<GENOSAUR extends Genosaur> {
    GENOSAUR mutate();
    GENOSAUR procreateWith(GENOSAUR partner);
}
