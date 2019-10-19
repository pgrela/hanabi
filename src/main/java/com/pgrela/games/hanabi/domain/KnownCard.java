package com.pgrela.games.hanabi.domain;

public interface KnownCard {
  Color getColor();
  Number getNumber();
  UnknownCard unknown();
}
