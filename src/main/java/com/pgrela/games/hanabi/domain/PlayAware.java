package com.pgrela.games.hanabi.domain;

public interface PlayAware {

  default void hintGiven(ColorHint colorHint) {
  }

  default void hintGiven(NumberHint numberHint) {
  }

  default void cardPlayed(Player player, Card card, CardPlayedOutcome outcome) {
  }

  default void cardDiscarded(Player player, Card card) {
  }

  default void cardDrawn(Player player) {

  }
  default void lastCardDrawn(Player player) {

  }

  default void gameFinished(){}
}
