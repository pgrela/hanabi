package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;

public interface Ritual<GENOME extends Genome<GENOME>> {
  JuvenileHerd<GENOME> undergo(MatureHerd<GENOME> genosaurs);
}
