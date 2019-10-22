package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.MyHand;
import com.pgrela.games.hanabi.domain.api.SomeonesHand;
import com.pgrela.games.hanabi.domain.api.UnknownCard;

import java.util.Collections;
import java.util.List;

public class Hand implements MyHand, SomeonesHand {

  private List<Card> cards;

  public Hand(List<Card> cards) {
    this.cards = cards;
  }

  KnownCard remove(UnknownCard unknownCard) {
    Card card = (Card) unknownCard;
    if (!cards.contains(card)) {
      throw new IllegalGameMoveException("Does not have the card");
    }
    cards.remove(card);
    return card;
  }

  void add(int index, Card card) {
    cards.add(index,card);
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

  public boolean contains(UnknownCard card) {
    return cards.contains(card);
  }
}
