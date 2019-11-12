package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;
import java.util.ArrayList;
import java.util.Collection;

public class JuvenileHerd<GENOME extends Genome<GENOME>> {

  private Collection<GENOME> genomes;

  public JuvenileHerd(Collection<GENOME> genomes) {
    this.genomes = new ArrayList<>(genomes);
  }

  public MatureHerd<GENOME> seekAdventures(Jungle<GENOME> jungle) {
    Collection<MatureGenosaur<GENOME>> list = new ArrayList<>();
    for (GENOME genome : members()) {
      SurvivalSkills survivalSkills = jungle.evaluate(genome);
      list.add(new MatureGenosaur<>(genome, survivalSkills));
    }
    return new MatureHerd<>(list);
  }

  private Iterable<GENOME> members() {
    return new ArrayList<>(genomes);
  }
}
