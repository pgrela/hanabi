package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;


public interface Jungle<GENOSAUR extends Genosaur<GENOSAUR>> {
    SurvivalSkills judge(GENOSAUR genosaur);
}
