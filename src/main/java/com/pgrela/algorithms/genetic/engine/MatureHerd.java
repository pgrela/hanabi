package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MatureHerd<GENOSAUR extends Genosaur<GENOSAUR>> {

    private Collection<MatureGenosaur<GENOSAUR>> members;

    public MatureHerd(Collection<MatureGenosaur<GENOSAUR>> list) {
        this.members = list;
    }

    Collection<MatureGenosaur<GENOSAUR>> members() {
        return members;
    }

    public int size() {
        return members.size();
    }
}
