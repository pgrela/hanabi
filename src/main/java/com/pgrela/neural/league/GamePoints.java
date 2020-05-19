package com.pgrela.neural.league;

public class GamePoints {
    private final int player1Points;
    private final int player2Points;
    public static GamePoints DRAW = new GamePoints(1, 1);
    public static GamePoints PLAYER_1_WINS = new GamePoints(3, 0);
    public static GamePoints PLAYER_2_WINS = new GamePoints(0, 3);

    private GamePoints(int player1Points, int player2Points) {
        this.player1Points = player1Points;
        this.player2Points = player2Points;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }
}
