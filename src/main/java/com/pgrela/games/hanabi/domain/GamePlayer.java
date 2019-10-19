package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.hint.ColorHintAnyone;
import java.util.List;

public interface GamePlayer extends Spectator {

  void setup(List<Player> nextPlayers, MyHand myHand, Table table);

  Turn doTheMove();

  default void hintGiven(ColorHintAnyone colorHint) {
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
