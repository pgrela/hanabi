package com.pgrela.games.hanabi.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FireworkDeck {

  private final Color color;
  private final List<Number> expectedNumbers;
  private final List<Card> cards = new ArrayList<>();

  public static FireworkDeck empty(Color color) {
    return new FireworkDeck(color, Number.ONE_TO_FIVE);
  }

  public FireworkDeck(Color color, List<Number> expectedNumbers) {

    this.color = color;
    this.expectedNumbers = expectedNumbers;
  }

  public boolean isFinished() {
    return expectedNumbers.size() == cards.size();
  }

  public boolean canAccept(KnownCard card) {
    return !isFinished()
        && card.getColor().equals(color)
        && card.getNumber().equals(expectedNumbers.get(cards.size()));
  }

  public void add(Card card) {
    if (!canAccept(card)) {
      throw new IllegalGameMoveException("Can not add this card");
    }
    cards.add(card);
  }

  public Optional<Card> topCard() {
    return cards.isEmpty() ? Optional.empty() : Optional.of(cards.get(cards.size() - 1));
  }

  public int score() {
    return cards.size();
  }
}
