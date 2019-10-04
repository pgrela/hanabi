package com.pgrela.games.hanabi.domain;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Hand implements MyHand, SomeonesHand {

  private List<Card> cards;

  public Hand(List<Card> cards) {
    this.cards = cards;
  }

  void remove(Card card) {
    if (!cards.contains(card)) {
      throw new IllegalGameMoveException("Does not have the card");
    }
    cards.remove(card);
  }

  void add(Card card) {
    cards.add(card);
  }

  @Override
  public Card getFirstCard() {
    return cards.get(0);
  }

  @Override
  public UnknownCard getMostRecentCard() {
    return cards.get(cards.size() - 1);
  }

  public List<UnknownCard> getCards() {
    return Collections.unmodifiableList(cards);
  }

  public List<Card> getRealCards() {
    return Collections.unmodifiableList(cards);
  }

  @Override
  public List<KnownCard> getKnownCards() {
    return Collections.unmodifiableList(cards);
  }
}
