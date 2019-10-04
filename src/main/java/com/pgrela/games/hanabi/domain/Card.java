package com.pgrela.games.hanabi.domain;

import java.util.Collection;
import java.util.stream.Collectors;

public class Card implements KnownCard, UnknownCard {

  private Number number;
  private Color color;

  public static Collection<Card> BASIC_DECK = Color.BASIC_COLORS.stream()
      .flatMap(color -> Number.BASIC_SET.stream().map(number -> new Card(color, number)))
      .collect(Collectors.toList());

  private Card(Color color, Number number) {
    this.color = color;
    this.number = number;
  }

  public Number getNumber() {
    return number;
  }

  public Color getColor() {
    return color;
  }

}
