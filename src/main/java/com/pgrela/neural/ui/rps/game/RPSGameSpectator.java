package com.pgrela.neural.ui.rps.game;

public interface RPSGameSpectator {
    default void scoreChanged(RPSGame game){}
    default void gameFinished(RPSGame rpsGame){}
    default void turnPlayed(RPSGame game, Figure human, Figure opponent){}
}
