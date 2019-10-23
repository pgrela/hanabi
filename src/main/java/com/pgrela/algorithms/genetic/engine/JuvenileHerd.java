package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

public class JuvenileHerd {

    public MatureHerd seekAdventures(Jungle jungle) {
        for(Genosaur genosaur: members()) {
            SurvivalSkills survivalSkills = jungle.judge(genosaur);
        }
        return new MatureHerd();
    }

    private Iterable<? extends Genosaur> members() {
        return null;
    }
}
