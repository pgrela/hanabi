package com.pgrela.algorithms.genetic.engine;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Zoologist {
    public HerdStatistics observe(MatureHerd matureHerd) {
        Collection<MatureGenosaur> members = matureHerd.members();
        return new HerdStatistics(members.stream().map(MatureGenosaur::survivalSkills).toArray(SurvivalSkills[]::new));
    }
}
