package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.Fireworks;
import com.pgrela.games.hanabi.domain.api.KnownCard;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FireworksImpl implements Fireworks {
    private Map<Color, FireworkDeck> decks;

    FireworksImpl(Map<Color, FireworkDeck> decks) {
        this.decks = decks;
    }

    public static FireworksImpl empty() {
        return new FireworksImpl(Color.BASIC_COLORS.stream().collect(Collectors.toMap(Function.identity(),
                FireworkDeck::empty)));
    }

    @Override
    public boolean areFinished() {
        return decks.values().stream().allMatch(FireworkDeck::isFinished);
    }

    @Override
    public boolean canAccept(KnownCard card) {
        return decks.get(card.getColor()).canAccept(card);
    }

    public void add(KnownCard card) {
        decks.get(card.getColor()).add(card);
    }

    @Override
    public List<KnownCard> topCards() {
        return decks.values().stream().map(FireworkDeck::topCard).map(o -> o.orElse(null)).collect(
                Collectors.toList());
    }

    @Override
    public int score() {
        return decks.values().stream().mapToInt(FireworkDeck::score).sum();
    }

    @Override
    public boolean contains(KnownCard card) {
        return decks.get(card.getColor()).topCard().filter(top -> top.getNumber().compareTo(card.getNumber()) >= 0).isPresent();
    }
}
