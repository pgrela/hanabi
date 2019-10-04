package com.pgrela.games.hanabi.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Hint {

  private final Player fromPlayer;
  private final Player toPlayer;
  private final List<UnknownCard> indicatedCards;

  Hint(Player fromPlayer, Player toPlayer,
      List<UnknownCard> indicatedCards) {
    this.fromPlayer = fromPlayer;
    this.toPlayer = toPlayer;
    this.indicatedCards = Collections.unmodifiableList(new ArrayList<>(indicatedCards));
  }

  public Player getFromPlayer() {
    return fromPlayer;
  }

  public Player getToPlayer() {
    return toPlayer;
  }

  public List<UnknownCard> getIndicatedCards() {
    return indicatedCards;
  }
}
