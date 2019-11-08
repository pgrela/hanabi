package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

public class MatureGenosaur<GENOSAUR extends Genosaur<GENOSAUR>> {

    private GENOSAUR genosaur;
    private SurvivalSkills survivalSkills;

    public MatureGenosaur(GENOSAUR genosaur, SurvivalSkills survivalSkills) {
        this.genosaur = genosaur;
        this.survivalSkills = survivalSkills;
    }

    public SurvivalSkills survivalSkills() {
        return survivalSkills;
    }

    public GENOSAUR rejuvenate() {
        return genosaur;
    }

    public GENOSAUR procreateWith(MatureGenosaur<GENOSAUR> partner) {
        return genosaur.procreateWith(partner.genosaur);
    }
}
