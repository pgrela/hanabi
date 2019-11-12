package com.pgrela.algorithms.genetic.engine;

public interface SurvivalSkills<SKILLS extends SurvivalSkills<SKILLS>> extends Comparable<SKILLS> {
    int compareTo(SKILLS other);
    double score();
}
