package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;

import java.util.Collection;

public class MatureHerd<GENOME extends Genome<GENOME>> {

    private Collection<MatureGenosaur<GENOME>> members;

    public MatureHerd(Collection<MatureGenosaur<GENOME>> list) {
        this.members = list;
    }

    Collection<MatureGenosaur<GENOME>> members() {
        return members;
    }

    public int size() {
        return members.size();
    }
}
