package com.pgrela.games.hanabi.domain;

import com.pgrela.algorithms.genetic.engine.Randomness;
import com.pgrela.games.hanabi.domain.api.CardValue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {

    public static final String DELIMITER = ",";
    public static final String SEPARATOR = "-";
    private Deque<Card> cards;

    Deck(Deque<Card> cards) {
        this.cards = cards;
    }

    public static Deck shuffle(Collection<Card> cards) {
        List<Card> orderedCards = new ArrayList<>(cards);
        Collections.shuffle(orderedCards, Randomness.RANDOM);
        return new Deck(new ArrayDeque<>(orderedCards));
    }

    public Card draw() {
        return cards.pop();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Deck clone() {
        return new Deck(new ArrayDeque<>(cards));
    }

    String serialize() {
        return "[" +
                cards.stream().map(Card::value).map(c -> String.format("%s" + SEPARATOR + "%s", c.getColor().serialize(), c.getNumber().serialize())).collect(Collectors.joining(DELIMITER))
                + "]";
    }

    public static Deck deserialize(String deck) {
        HashMap<CardValue, ArrayDeque<Card>> cards = Card.BASIC_DECK.stream().collect(Collectors.groupingBy(Card::value, HashMap::new, Collectors.toCollection(ArrayDeque::new)));
        return new Deck(Arrays.stream(deck.substring(1, deck.length() - 1).split(DELIMITER)).map(s -> {
            String[] split = s.split(SEPARATOR);
            return cards.get(new CardValue(Color.deserialize(split[0]), Number.deserialize(split[1]))).pop();
        }).collect(Collectors.toCollection(ArrayDeque::new)));
    }
}
