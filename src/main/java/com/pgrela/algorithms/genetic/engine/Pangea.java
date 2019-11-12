package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;
import java.util.Collection;

public class Pangea<GENOME extends Genome<GENOME>> {

  private Jungle jungle;
  private JuvenileHerd<GENOME> herd;
  private Ritual<GENOME> ritual;
  private Collection<Zoologist> zoologists;

  public Pangea(
      Jungle<GENOME> jungle,
      JuvenileHerd<GENOME> herd, Ritual<GENOME> ritual,
      Collection<Zoologist> zoologists) {
    this.jungle = jungle;
    this.herd = herd;
    this.ritual = ritual;
    this.zoologists = zoologists;
  }

  public void start() {
    Zoologist zoologist = new Zoologist();
    MatureHerd<GENOME> matureHerd = herd.seekAdventures(jungle);

    for (int year = 0; year < 300; year++) {
      herd = ritual.undergo(matureHerd);
      matureHerd = herd.seekAdventures(jungle);

      zoologist.observe(matureHerd);
    }
  }
}
