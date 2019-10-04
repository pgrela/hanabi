package com.pgrela.games.hanabi.domain;

import java.util.List;

public class ColorHint extends Hint {

  private Color color;

  public ColorHint(Player fromPlayer, Player toPlayer,
      List<UnknownCard> indicatedCards, Color color) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}
