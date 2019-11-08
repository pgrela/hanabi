package com.pgrela.algorithms.genetic.polynomial;

import com.pgrela.algorithms.genetic.api.Genosaur;
import com.pgrela.algorithms.genetic.engine.Jungle;
import com.pgrela.algorithms.genetic.engine.JuvenileHerd;
import com.pgrela.algorithms.genetic.engine.Pangea;
import com.pgrela.algorithms.genetic.engine.Ritual;
import com.pgrela.algorithms.genetic.engine.Zoologist;
import java.util.Arrays;
import java.util.Collection;

public class PangeaBuilder<GENOSAUR extends Genosaur<GENOSAUR>> {

  private Jungle jungle;
  private Collection<GENOSAUR> herd;
  private Ritual<GENOSAUR> ritual;
  private Zoologist[] zoologists;

  public PangeaBuilder<GENOSAUR> withHerd(Collection<GENOSAUR> polynomials) {
    herd = polynomials;
    return this;
  }

  public PangeaBuilder<GENOSAUR> withJungle(Jungle jungle) {
    this.jungle = jungle;
    return this;
  }

  public PangeaBuilder<GENOSAUR> withRitual(Ritual<GENOSAUR> ritual) {
    this.ritual = ritual;
    return this;
  }

  public PangeaBuilder<GENOSAUR> withZoologists(Zoologist... zoologists) {
    this.zoologists = zoologists;
    return this;
  }

  public Pangea create() {
    return new Pangea<>(jungle, new JuvenileHerd<>(herd), ritual, Arrays.asList(zoologists));
  }
}
