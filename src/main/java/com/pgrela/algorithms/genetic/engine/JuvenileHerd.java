package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;
import java.util.ArrayList;
import java.util.Collection;

public class JuvenileHerd<GENOSAUR extends Genosaur<GENOSAUR>> {

  private Collection<GENOSAUR> genosaurs;

  public JuvenileHerd(Collection<GENOSAUR> genosaurs) {
    this.genosaurs = new ArrayList<>(genosaurs);
  }

  public MatureHerd<GENOSAUR> seekAdventures(Jungle jungle) {
    Collection<MatureGenosaur<GENOSAUR>> list = new ArrayList<>();
    for (GENOSAUR genosaur : members()) {
      SurvivalSkills survivalSkills = jungle.judge(genosaur);
      list.add(new MatureGenosaur<>(genosaur, survivalSkills));
    }
    return new MatureHerd<>(list);
  }

  private Iterable<GENOSAUR> members() {
    return new ArrayList<>(genosaurs);
  }
}
