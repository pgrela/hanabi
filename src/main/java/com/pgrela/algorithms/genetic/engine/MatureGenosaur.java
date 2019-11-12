package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;

public class MatureGenosaur<GENOME extends Genome<GENOME>> {

    private GENOME genome;
    private SurvivalSkills survivalSkills;

    public MatureGenosaur(GENOME genome, SurvivalSkills survivalSkills) {
        this.genome = genome;
        this.survivalSkills = survivalSkills;
    }

    public SurvivalSkills survivalSkills() {
        return survivalSkills;
    }

    public GENOME rejuvenate() {
        return genome;
    }

    public GENOME procreateWith(MatureGenosaur<GENOME> partner) {
        return genome.cross(partner.genome);
    }
}
