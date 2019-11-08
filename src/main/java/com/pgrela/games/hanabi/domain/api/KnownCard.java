package com.pgrela.games.hanabi.domain.api;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;

public interface KnownCard {
  Color getColor();
  Number getNumber();
  UnknownCard unknown();
  CardValue value();
}
