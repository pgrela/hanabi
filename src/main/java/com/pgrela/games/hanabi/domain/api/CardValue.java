package com.pgrela.games.hanabi.domain.api;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;
import java.util.Objects;

public class CardValue {
  private Color color;
  private Number number;

  public CardValue(Color color, Number number) {
    this.color = color;
    this.number = number;
  }

  public Color getColor() {
    return color;
  }

  public Number getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardValue cardValue = (CardValue) o;
    return color.equals(cardValue.color) &&
        number.equals(cardValue.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, number);
  }
}
