package com.pgrela.games.hanabi.domain.genetic;

import com.pgrela.algorithms.genetic.engine.Randomness;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.pgrela.games.hanabi.domain.genetic.CheaterGene.Action.PLAY_FIFTH;

public class CheaterGene {

    public static final int GENE_LENGTH = 4;

    public CheaterNucleotide nucleotide(int index) {
        return nucleotides[index];
    }

    public CheaterGene mutate() {
        CheaterNucleotide[] copy = Arrays.copyOf(nucleotides, nucleotides.length);
        copy[random.nextInt(copy.length)] = Action.RANDOMIZED_ACTIONS[random.nextInt(Action.RANDOMIZED_ACTIONS.length)];
        return new CheaterGene(copy);
    }

    public static CheaterGene deserialize(String gene) {
        return new CheaterGene(gene.chars().mapToObj(c -> (char) c).map(Object::toString).map(Action::deserialize).toArray(CheaterNucleotide[]::new));
    }

    public enum Action implements CheaterNucleotide {
        PLAY_FIRST("1"), PLAY_SECOND("2"), PLAY_THIRD("3"), PLAY_FOURTH("4"), PLAY_FIFTH("5"), HOLD("H"), DISCARD("D"), NO_HINT("-");
        String letter;
        private static Map<String, Action> actions = new HashMap<>();
        static {
            for(Action action: values()){
                actions.put(action.letter, action);
            }
        }

        static Action[] RANDOMIZED_ACTIONS = new Action[]{
                PLAY_FIRST,
                PLAY_FIRST,
                PLAY_SECOND,
                PLAY_THIRD,
                PLAY_FOURTH,
                PLAY_FIFTH,
                HOLD,
                HOLD,
                DISCARD,
                DISCARD,
                NO_HINT,
                NO_HINT,
                NO_HINT,
                NO_HINT,
                NO_HINT,
                NO_HINT,
                NO_HINT,
        };

        Action(String letter) {
            this.letter = letter;
        }

        @Override
        public String toString() {
            return letter;
        }

        public static Action deserialize(String character) {
            return actions.get(character);
        }
    }

    public static final Action[] PLAYS = new Action[]{Action.PLAY_FIRST, Action.PLAY_SECOND, Action.PLAY_THIRD, Action.PLAY_FOURTH, PLAY_FIFTH};

    private static final Random random = Randomness.RANDOM;
    private CheaterNucleotide[] nucleotides;

    public static CheaterGene random() {
        CheaterNucleotide[] gene = new CheaterNucleotide[GENE_LENGTH];
        for (int i = 0; i < gene.length; i++) {
            gene[i] = Action.RANDOMIZED_ACTIONS[random.nextInt(Action.RANDOMIZED_ACTIONS.length)];
        }
        return new CheaterGene(gene);
    }

    public CheaterGene(CheaterNucleotide[] nucleotides) {
        this.nucleotides = nucleotides;
    }

    @Override
    public String toString() {
        return Arrays.stream(nucleotides).map(CheaterNucleotide::toString).collect(Collectors.joining());
    }

    public CheaterGene clone() {
        return new CheaterGene(Arrays.copyOf(nucleotides, nucleotides.length));
    }
}
