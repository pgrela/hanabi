package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatingSeason<GENOME extends Genome<GENOME>> implements Ritual<GENOME> {

    public static final Random RANDOM = Randomness.RANDOM;

    private List<BiConsumer<List<MatureGenosaur<GENOME>>, List<GENOME>>> consumers = new ArrayList<>();

    public MatingSeason<GENOME> preserveBest(int percentile) {
        consumers.add((ordered, newGeneration) -> ordered.stream().map(MatureGenosaur::rejuvenate).limit(ordered.size() * percentile / 100).forEach(newGeneration::add));
        return this;
    }

    public MatingSeason<GENOME> mutateBest(int percentile) {
        consumers.add((ordered, newGeneration) -> ordered.stream()
                .map(MatureGenosaur::rejuvenate)
                .limit(ordered.size() * percentile / 100)
                .map(Genome::mutate)
                .forEach(newGeneration::add));
        return this;
    }

    public MatingSeason<GENOME> breedBest(int topPercentileToBreed, int offSpringPercents) {
        consumers.add((ordered, newGeneration) -> {
            List<MatureGenosaur<GENOME>> breeding = ordered.stream().limit(ordered.size() * topPercentileToBreed / 100).collect(Collectors.toList());
            for (int i = 0; i < ordered.size() * offSpringPercents / 100; i++) {
                MatureGenosaur<GENOME> mother = breeding.get(RANDOM.nextInt(breeding.size()));
                MatureGenosaur<GENOME> father = breeding.get(RANDOM.nextInt(breeding.size()));
                GENOME child = mother.procreateWith(father);
                newGeneration.add(child);
            }
        });
        return this;
    }

    @Override
    public JuvenileHerd<GENOME> undergo(MatureHerd<GENOME> genosaurs) {
        int size = genosaurs.size();
        Comparator<MatureGenosaur<GENOME>> comparator = Comparator.<MatureGenosaur<GENOME>, SurvivalSkills>comparing(
                MatureGenosaur<GENOME>::survivalSkills).reversed();
        List<MatureGenosaur<GENOME>> ordered = genosaurs.members().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        List<GENOME> nextGeneration = new ArrayList<>();

        consumers.forEach(c -> c.accept(ordered, nextGeneration));

        return new JuvenileHerd<>(nextGeneration);
    }

    public MatingSeason<GENOME> killRandomlyLast(int percentileLast, int percentsToReplace, Supplier<GENOME> randomProducer) {
        consumers.add((ordered, newGeneration) -> {
            int top = ordered.size() * (100 - percentileLast) / 100;
            Stream.generate(() -> RANDOM.nextInt(ordered.size() - top))
                    .limit(ordered.size() * percentsToReplace / 100)
                    .forEach(i -> ordered.set(i + top, new MatureGenosaur<>(randomProducer.get(), null)));
        });
        return this;
    }

    public MatingSeason<GENOME> shuffleBest(int percentile) {
        consumers.add((ordered, newGeneration) -> ordered.stream()
                .map(MatureGenosaur::rejuvenate)
                .limit(ordered.size() * percentile / 100)
                .map(Genome::shuffle)
                .forEach(newGeneration::add));
        return this;
    }
}
