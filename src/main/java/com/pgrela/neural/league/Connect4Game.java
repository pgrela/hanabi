package com.pgrela.neural.league;

import com.pgrela.games.engine.api.Player;
import com.pgrela.games.engine.connect4.Connect4Move;
import com.pgrela.games.engine.connect4.game.MagicBoard;
import com.pgrela.games.engine.connect4.game.MagicBoardEvaluator;
import com.pgrela.neural.utils.Utils;

import static com.pgrela.neural.league.Connect4NetworkPlayer.INPUT_SIZE;

public class Connect4Game {
    public static final int ROWS = 6;
    private final Player player1;
    public static final int PLAYER_1_SYMBOL = (int) MagicBoard.CROSS;
    public static final int PLAYER_2_SYMBOL = (int) MagicBoard.CIRCLE;
    private final Player player2;
    private MagicBoard board;
    public static final int BOTTOM_ROW = ROWS - 1;
    private int playedMoves = 0;
    private boolean finished = false;
    private Player winner;
    private int COLUMNS = 7;
    double[] inputForPlayer1;
    double[] inputForPlayer2;

    public Connect4Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new MagicBoard();
        inputForPlayer1 = new double[ROWS * COLUMNS * 3];
        inputForPlayer2 = new double[ROWS * COLUMNS * 3];
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                inputForPlayer1[(row * COLUMNS + column) * 3] = 1;
                inputForPlayer2[(row * COLUMNS + column) * 3] = 1;
            }
        }
        if (inputForPlayer1.length != INPUT_SIZE) {
            throw new RuntimeException("Input does not much nn size");
        }
    }

    public boolean isLegal(Connect4Move move) {
        return board.getCode(0, move.getColumn()) == MagicBoard.BLANK;
    }

    public void play(Player player, Connect4Move move) {
        if (!isLegal(move)) {
            throw new IllegalArgumentException("Illegal move");
        }
        int symbol = PLAYER_1_SYMBOL;
        if (player.equals(player2)) {
            symbol = PLAYER_2_SYMBOL;
        }
        for (int row = BOTTOM_ROW; row >= 0; row--) {
            if (board.getCode(row, move.getColumn()) == MagicBoard.BLANK) {
                setSymbol(symbol, row, move.getColumn());
                ++playedMoves;
                break;
            }
        }
        long winner = MagicBoardEvaluator.evaluateBoard(board);
        if (winner == MagicBoard.BLANK) {
            if (playedMoves == 42) {
                finished = true;
            }
        } else {
            finished = true;
            this.winner = player2;
            if (winner == PLAYER_1_SYMBOL) {
                this.winner = player1;
            }
        }
    }

    private void setSymbol(int symbol, int row, int column) {
        board.setCode(row, column, symbol);
        inputForPlayer1[(row * COLUMNS + column) * 3] = 0;
        inputForPlayer2[(row * COLUMNS + column) * 3] = 0;
        inputForPlayer1[(row * COLUMNS + column) * 3 + symbol] = 1;
        inputForPlayer2[(row * COLUMNS + column) * 3 + (3 - symbol) - 1] = 1;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isWinner(Player player) {
        return player.equals(winner);
    }

    public double[] asInputForPlayer1() {
        return inputForPlayer1;
    }

    public double[] asInputForPlayer2() {
        return inputForPlayer2;
    }

    public Connect4Move asMove(double[] output) {
        return Connect4Move.getMove(Utils.maxIndex(output));
    }

    public int playedMoves() {
        return playedMoves;
    }

    public void giveUp(Connect4NetworkPlayer player) {
        finished = true;
        winner = player1.equals(player) ? player2 : player1;
    }

    public double[] asInput(Connect4NetworkPlayer player) {
        return player.equals(player1) ? asInputForPlayer1() : asInputForPlayer2();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, inputForPlayer1, "X", "O");
        append(builder, inputForPlayer2, "O", "X");
        return builder.toString();
    }

    private void append(StringBuilder builder, double[] input, String x, String o) {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                int i = (row * COLUMNS + column) * 3;
                if (input[i] == 0 && input[i + 1] == 0) {
                    builder.append(".");
                } else {
                    if (input[i] != 0 && input[i + 1] != 0) {
                        throw new IllegalStateException();
                    }
                    if (input[i] > .5)
                        builder.append(x);

                    if (input[i+1] > .5)
                        builder.append(o);
                }
            }
            builder.append("\n");
        }
        builder.append("\n");
    }
}
