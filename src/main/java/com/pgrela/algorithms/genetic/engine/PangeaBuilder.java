package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;

import java.util.Arrays;
import java.util.Collection;

public class PangeaBuilder<GENOME extends Genome<GENOME>> {

  private Jungle<GENOME> jungle;
  private Collection<GENOME> herd;
  private Ritual<GENOME> ritual;
  private Zoologist[] zoologists;
  private int years;

  public PangeaBuilder<GENOME> withHerd(Collection<GENOME> polynomials) {
    herd = polynomials;
    return this;
  }

  public PangeaBuilder<GENOME> withJungle(Jungle<GENOME> jungle) {
    this.jungle = jungle;
    return this;
  }

  public PangeaBuilder<GENOME> withRitual(Ritual<GENOME> ritual) {
    this.ritual = ritual;
    return this;
  }

  public PangeaBuilder<GENOME> withZoologists(Zoologist... zoologists) {
    this.zoologists = zoologists;
    return this;
  }

  public Pangea create() {
    return new Pangea<>(jungle, new JuvenileHerd<>(herd), ritual, Arrays.asList(zoologists), years);
  }

  public PangeaBuilder<GENOME> forGenerations(int generations) {
    this.years = generations;
    return this;
  }
}
