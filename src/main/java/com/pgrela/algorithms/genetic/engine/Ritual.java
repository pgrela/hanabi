package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

public interface Ritual<GENOSAUR extends Genosaur<GENOSAUR>> {
  JuvenileHerd<GENOSAUR> undergo(MatureHerd<GENOSAUR> genosaurs);
}
