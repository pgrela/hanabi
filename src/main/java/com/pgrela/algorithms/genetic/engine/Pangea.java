package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;
import java.util.Collection;

public class Pangea<GENOME extends Genome<GENOME>> {

  private Jungle jungle;
  private JuvenileHerd<GENOME> herd;
  private Ritual<GENOME> ritual;
  private Collection<Zoologist> zoologists;
  private int generations;

  public Pangea(
      Jungle<GENOME> jungle,
      JuvenileHerd<GENOME> herd, Ritual<GENOME> ritual,
      Collection<Zoologist> zoologists, int generations) {
    this.jungle = jungle;
    this.herd = herd;
    this.ritual = ritual;
    this.zoologists = zoologists;
    this.generations = generations;
  }

  public void start() {
    Zoologist zoologist = new Zoologist();
    MatureHerd<GENOME> matureHerd = herd.seekAdventures(jungle);

    for (int generation = 2; generation <= generations; generation++) {
      herd = ritual.undergo(matureHerd);
      matureHerd = herd.seekAdventures(jungle);

      System.out.printf("Generation %4d: ", generation);

      zoologist.observe(matureHerd);
    }
  }
}
