package com.pgrela.games.hanabi.domain;

import java.util.List;

public class NumberHint extends Hint {

  private final Number number;

  public NumberHint(Player fromPlayer, Player toPlayer, List<UnknownCard> indicatedCards,
      Number number) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.number = number;
  }

  public Number getNumber() {
    return number;
  }
}
