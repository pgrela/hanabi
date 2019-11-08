package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.CardValue;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.UnknownCard;

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

  @Override
  public UnknownCard unknown() {
    return this;
  }

  @Override
  public CardValue value() {
    return new CardValue(color, number);
  }

  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "Card{" +
        "" + number +
        ", " + color +
        '}';
  }
}
