package com.pgrela.games.hanabi.domain.genetic;

import com.pgrela.algorithms.genetic.api.Genome;
import com.pgrela.algorithms.genetic.engine.Randomness;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class MarkovGenome implements Genome<MarkovGenome> {

    public static final int COLORS = 5;
    public static final int NUMBERS = 5;
    public static final int GENOME_LENGTH = (COLORS + NUMBERS);
    private CheaterGene[] genome;
    private static final Random RANDOM = Randomness.RANDOM;

    public MarkovGenome(CheaterGene[] genome) {
        this.genome = genome;
    }

    public static MarkovGenome random() {
        CheaterGene[] genome = new CheaterGene[GENOME_LENGTH];
        for (int i = 0; i < genome.length; i++) {
            genome[i] = CheaterGene.random();
        }
        return new MarkovGenome(genome);
    }

    @Override
    public MarkovGenome mutate() {
        CheaterGene[] copy = cloneGenome();
        swapTwoGenes(copy);
        swapTwoGenes(copy);
        swapTwoGenes(copy);
        mutateGene(copy);
        mutateGene(copy);
        copy[RANDOM.nextInt(copy.length)] = CheaterGene.random();
        return new MarkovGenome(copy);
    }

    private CheaterGene[] cloneGenome() {
        CheaterGene[] copy = new CheaterGene[genome.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i]=genome[i].clone();
        }
        return copy;
    }

    private void mutateGene(CheaterGene[] copy) {
        int mutated = RANDOM.nextInt(copy.length);
        copy[mutated] = copy[mutated].mutate();
    }

    private void swapTwoGenes(CheaterGene[] copy) {
        int a = RANDOM.nextInt(copy.length);
        int b = RANDOM.nextInt(copy.length);
        CheaterGene swapped = copy[a];
        copy[a] = copy[b];
        copy[b] = swapped;
    }

    @Override
    public MarkovGenome cross(MarkovGenome partner) {
        CheaterGene[] copy = new CheaterGene[genome.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = RANDOM.nextBoolean() ? genome[i] : partner.genome[i];
        }
        return new MarkovGenome(copy);
    }

    @Override
    public MarkovGenome deserialize(String genome) {
        return fromString(genome);
    }

    @Override
    public String serialize() {
        return "[" +
                String.join(",", Arrays.stream(genome).map(CheaterGene::toString).collect(Collectors.joining(","))) +
                "]";
    }

    public static MarkovGenome fromString(String genome) {
        return new MarkovGenome(Arrays.stream(genome.substring(1, genome.length() - 1).split(",")).map(CheaterGene::deserialize).toArray(CheaterGene[]::new));
    }

    @Override
    public MarkovGenome immigrant() {
        return random();
    }

    public CheaterGene[] genes() {
        return genome;
    }

    @Override
    public MarkovGenome shuffle() {
        CheaterGene[] copy = cloneGenome();
        Collections.shuffle(new ArrayList<>(Arrays.asList(copy)), RANDOM);
        return new MarkovGenome(copy);
    }

    public MarkovGenome clone(){
        return new MarkovGenome(Arrays.stream(genome).map(CheaterGene::clone).toArray(CheaterGene[]::new));
    }
}
