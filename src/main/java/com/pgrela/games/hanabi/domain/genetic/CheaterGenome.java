package com.pgrela.games.hanabi.domain.genetic;

import com.pgrela.algorithms.genetic.api.Genome;
import com.pgrela.algorithms.genetic.engine.Randomness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class CheaterGenome implements Genome<CheaterGenome> {

    public static final int COLORS = 5;
    public static final int NUMBERS = 5;
    public static final int GENOME_LENGTH = (COLORS + NUMBERS);
    private CheaterGene[] genome;
    private static final Random RANDOM = Randomness.RANDOM;

    public CheaterGenome(CheaterGene[] genome) {
        this.genome = genome;
    }

    public static CheaterGenome random() {
        CheaterGene[] genome = new CheaterGene[GENOME_LENGTH];
        for (int i = 0; i < genome.length; i++) {
            genome[i] = CheaterGene.random();
        }
        return new CheaterGenome(genome);
    }

    @Override
    public CheaterGenome mutate() {
        CheaterGene[] copy = cloneGenome();
        swapTwoGenes(copy);
        swapTwoGenes(copy);
        swapTwoGenes(copy);
        mutateGene(copy);
        mutateGene(copy);
        copy[RANDOM.nextInt(copy.length)] = CheaterGene.random();
        return new CheaterGenome(copy);
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
    public CheaterGenome cross(CheaterGenome partner) {
        CheaterGene[] copy = new CheaterGene[genome.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = RANDOM.nextBoolean() ? genome[i] : partner.genome[i];
        }
        return new CheaterGenome(copy);
    }

    @Override
    public CheaterGenome deserialize(String genome) {
        return fromString(genome);
    }

    @Override
    public String serialize() {
        return "[" +
                String.join(",", Arrays.stream(genome).map(CheaterGene::toString).collect(Collectors.joining(","))) +
                "]";
    }

    public static CheaterGenome fromString(String genome) {
        return new CheaterGenome(Arrays.stream(genome.substring(1, genome.length() - 1).split(",")).map(CheaterGene::deserialize).toArray(CheaterGene[]::new));
    }

    @Override
    public CheaterGenome immigrant() {
        return random();
    }

    public CheaterGene[] genes() {
        return genome;
    }

    @Override
    public CheaterGenome shuffle() {
        CheaterGene[] copy = cloneGenome();
        Collections.shuffle(new ArrayList<>(Arrays.asList(copy)), RANDOM);
        return new CheaterGenome(copy);
    }

    public CheaterGenome clone(){
        return new CheaterGenome(Arrays.stream(genome).map(CheaterGene::clone).toArray(CheaterGene[]::new));
    }
}
