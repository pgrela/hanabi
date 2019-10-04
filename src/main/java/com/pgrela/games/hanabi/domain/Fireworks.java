package com.pgrela.games.hanabi.domain;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Fireworks {
  HashMap<Color, FireworkDeck> decks;

  Fireworks(HashMap<Color, FireworkDeck> decks) {
    this.decks = decks;
  }

  public static Fireworks empty(){
    HashMap<Color, FireworkDeck> decks = new HashMap<>();
    for (Color color : Color.BASIC_COLORS) {
      decks.put(color, FireworkDeck.empty(color));
    }
    return new Fireworks(decks);
  }
  boolean areFinished(){
    return decks.values().stream().allMatch(FireworkDeck::isFinished);
  }

  public boolean canAccept(Card card) {
    return decks.get(card.getColor()).canAccept(card);
  }
  public boolean canAccept(KnownCard card) {
    return decks.get(card.getColor()).canAccept(card);
  }

  public void add(Card card) {
    decks.get(card.getColor()).add(card);
  }

  public List<Card> topCards() {
    return decks.values().stream().map(FireworkDeck::topCard).map(o->o.orElse(null)).collect(
        Collectors.toList());
  }
  public int score(){
    return decks.values().stream().mapToInt(FireworkDeck::score).sum();
  }
}
