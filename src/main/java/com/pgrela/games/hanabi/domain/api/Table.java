package com.pgrela.games.hanabi.domain.api;

public interface Table {

  boolean isDeckEmpty();

  Fireworks getFireworks();

  int getAvailableHintTokens();

  int getAvailableBlownTokens();

  int remainingDeckSize();

  boolean areHintTokensAvailable();

  int score();
}
