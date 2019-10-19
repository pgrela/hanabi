package com.pgrela.games.hanabi.domain;

import java.util.List;

public interface NumberHint {

  Number getNumber();

  Player getFromPlayer();

  Player getToPlayer();

  List<KnownCard> getIndicatedCards();
}
