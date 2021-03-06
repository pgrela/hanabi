package com.pgrela.games.hanabi.domain.api;

import com.pgrela.games.hanabi.domain.Deck;

public interface Spectator {

  default void hintGiven(ColorHintToOtherPlayer colorHint) {
  }

  default void hintGiven(NumberHintToOtherPlayer numberHint) {
  }

  default void cardPlayed(OtherPlayer player, KnownCard card, CardPlayedOutcome outcome) {
  }

  default void cardDiscarded(OtherPlayer player, KnownCard card) {
  }

  default void cardDrawn(OtherPlayer player, KnownCard card) {

  }

  default void theLastCardDrawn(OtherPlayer player) {

  }

  default void gameFinished() {
  }

  default void turnPlayed() {
  }

  default void gameStarts() {
  }

  default void gameInitiated(Deck deck) {
  }
}
