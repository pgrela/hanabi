package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genosaur;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Ritual {
    public List<Genosaur> undergo(List<MatureGenosaur> genosaurs) {
        int size = genosaurs.size();
        List<MatureGenosaur> breeding = genosaurs.stream().sorted(Comparator.comparing(MatureGenosaur::survivalSkills)).limit(size/10).collect(Collectors.toList());
        Random random = new Random();
        List<Genosaur> nextGeneration = breeding.stream().map(MatureGenosaur::rejuvenate).collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            MatureGenosaur mother = breeding.get(random.nextInt(breeding.size()));
            MatureGenosaur father = breeding.get(random.nextInt(breeding.size()));
            Genosaur child = mother.procreateWith(father);
            nextGeneration.add(child);
        }

        List<Genosaur> mutated = nextGeneration.stream().map(Genosaur::mutate).collect(Collectors.toList());


        return mutated;
    }
}
