package com.pgrela.neural.league;

import com.pgrela.games.engine.connect4.Connect4Move;

public class HistoricalMove {
    double input[] = new double[Connect4NetworkPlayer.INPUT_SIZE];
    Connect4Move move;
    Connect4NetworkPlayer player;

    public void input(double[] input) {
        System.arraycopy(input, 0, this.input, 0, input.length);
    }

    @Override
    public String toString() {
        return "HistoricalMove{" +
                ", move=" + move +
                ", player=" + player +
                '}';
    }
}
