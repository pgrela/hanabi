package com.pgrela.neural.ui.rps.game;

public class Score {
    private int human;
    private int opponent;
    private int bestOf;

    public Score(int bestOf) {
        this.bestOf = bestOf;
    }

    public int getHuman() {
        return human;
    }

    void addHumanPoint() {
        ++human;
    }
    void addOpponentPoint() {
        ++opponent;
    }

    public int getOpponent() {
        return opponent;
    }

    public boolean isFinished() {
        return Math.max(human, opponent) > bestOf/2;
    }

    public boolean didHumanWin() {
        return human>opponent;
    }
}
