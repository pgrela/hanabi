package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;


public interface Jungle<GENOME extends Genome<GENOME>> {
    SurvivalSkills evaluate(GENOME genosaur);
}
