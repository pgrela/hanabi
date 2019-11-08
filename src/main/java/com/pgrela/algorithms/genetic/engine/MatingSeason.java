package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MatingSeason<GENOSAUR extends Genosaur<GENOSAUR>> implements Ritual<GENOSAUR> {

  @Override
  public JuvenileHerd<GENOSAUR> undergo(MatureHerd<GENOSAUR> genosaurs) {
    int size = genosaurs.size();
    List<MatureGenosaur<GENOSAUR>> ordered = genosaurs.members().stream().sequential()
        .sorted(Comparator.<MatureGenosaur<GENOSAUR>, SurvivalSkills>comparing(
            MatureGenosaur::survivalSkills).reversed())
        .collect(Collectors.toList());
    List<MatureGenosaur<GENOSAUR>> breeding = ordered.stream().limit(size/10).collect(Collectors.toList());
    Random random = new Random();
    List<GENOSAUR> nextGeneration = breeding.stream()
        .map(MatureGenosaur::rejuvenate)
        .collect(Collectors.toList());
    for (int i = 0; i < size/2; i++) {
      MatureGenosaur<GENOSAUR> mother = breeding.get(random.nextInt(breeding.size()));
      MatureGenosaur<GENOSAUR> father = breeding.get(random.nextInt(breeding.size()));
      GENOSAUR child = mother.procreateWith(father);
      nextGeneration.add(child);
    }

    ordered.stream().map(MatureGenosaur::rejuvenate).map(Genosaur::mutate)
        .limit(size - nextGeneration.size())
        .forEach(nextGeneration::add);

    return new JuvenileHerd<>(nextGeneration);
  }
}
