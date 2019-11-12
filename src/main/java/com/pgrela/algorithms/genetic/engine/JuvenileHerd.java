package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JuvenileHerd<GENOME extends Genome<GENOME>> {

  private Collection<GENOME> genomes;

  public JuvenileHerd(Collection<GENOME> genomes) {
    this.genomes = new ArrayList<>(genomes);
  }

  public MatureHerd<GENOME> seekAdventures(Jungle<GENOME> jungle) {
    List<MatureGenosaur<GENOME>> list = StreamSupport.stream(members().spliterator(), true)
        .map(genome -> new MatureGenosaur<>(genome, jungle.evaluate(genome))).collect(
            Collectors.toList());
    return new MatureHerd<>(list);
  }

  private Iterable<GENOME> members() {
    return new ArrayList<>(genomes);
  }
}
