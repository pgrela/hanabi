package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;
import java.util.Collection;

public class Pangea<GENOSAUR extends Genosaur<GENOSAUR>> {

  private Jungle jungle;
  private JuvenileHerd<GENOSAUR> herd;
  private Ritual<GENOSAUR> ritual;
  private Collection<Zoologist> zoologists;

  public Pangea(
      Jungle jungle,
      JuvenileHerd<GENOSAUR> herd, Ritual<GENOSAUR> ritual,
      Collection<Zoologist> zoologists) {
    this.jungle = jungle;
    this.herd = herd;
    this.ritual = ritual;
    this.zoologists = zoologists;
  }

  public void start() {
    Zoologist zoologist = new Zoologist();
    MatureHerd<GENOSAUR> matureHerd = herd.seekAdventures(jungle);

    for (int year = 0; year < 300; year++) {
      herd = ritual.undergo(matureHerd);
      matureHerd = herd.seekAdventures(jungle);

      HerdStatistics statistics = zoologist.observe(matureHerd);
      System.out.println(statistics);
    }
    System.out.println("done");
  }
}
