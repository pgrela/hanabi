package com.pgrela.games.hanabi.domain.api;

import com.pgrela.games.hanabi.domain.Turn;

import java.util.List;

public interface Player {

    void setup(List<OtherPlayer> nextPlayers, MyHand myHand, Table table);

    Turn doTheMove();

    default int acceptDrawnCard(UnknownCard card) {
        return 0;
    }

    default void hintGiven(ColorHintToOtherPlayer colorHint) {
    }

    default void hintGiven(NumberHintToOtherPlayer numberHint) {
    }

    default void receiveHint(ColorHintToMe colorHint) {
    }

    default void receiveHint(NumberHintToMe numberHint) {
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
}
