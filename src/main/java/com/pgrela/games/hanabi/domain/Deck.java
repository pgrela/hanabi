package com.pgrela.games.hanabi.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Deck {

  private Deque<Card> cards;

  Deck(Deque<Card> cards) {
    this.cards = cards;
  }

  public static Deck shuffle(Collection<Card> cards) {
    List<Card> orderedCards = new ArrayList<>(cards);
    Collections.shuffle(orderedCards);
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
}
