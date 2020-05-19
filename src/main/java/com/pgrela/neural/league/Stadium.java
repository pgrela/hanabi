package com.pgrela.neural.league;

import com.pgrela.games.engine.connect4.Connect4Move;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.pgrela.neural.league.Connect4NetworkPlayer.OUTPUT_SIZE;

public class Stadium {

    private static final int MAX_MOVES = 42;
    private Connect4NetworkPlayer player1;
    private Connect4NetworkPlayer player2;

    private HistoricalMove[] moves;

    private Connect4Game game;
    private int movesRecorded = 0;
    private boolean learning = true;

    public Stadium() {
        moves = Stream.generate(HistoricalMove::new).limit(MAX_MOVES).toArray(HistoricalMove[]::new);
    }

    public void newGame(Connect4NetworkPlayer player1, Connect4NetworkPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1.getReady();
        player2.getReady();
        game = new Connect4Game(player1, player2);
        movesRecorded = 0;
    }

    public void playWithLearning() {
        learning = true;
        play();
    }

    public void playForFun() {
        learning = false;
        play();
    }

    private void play() {
        Connect4NetworkPlayer player = player1;
        Connect4NetworkPlayer opponent = player2;
        while (!game.isFinished()) {
            takeTurn(player, opponent);

            Connect4NetworkPlayer tmp = player;
            player = opponent;
            opponent = tmp;
        }

    }

    private void takeTurn(Connect4NetworkPlayer player, Connect4NetworkPlayer opponent) {
        double[] input = game.asInput(player);
        double[] output = player.process(input);
        Connect4Move move = game.asMove(output);
        recordMove(player, input, move);

        if (!game.isLegal(move)) {
            teachTragicLooser(player, game, input, output);
            game.giveUp(player);
            return;
        }
        game.play(player, move);
        if (game.isFinished()) {
            if (game.isWinner(player)) {
                teachWinner(player);
            } else {
                teachLooser(player);
            }
            if (game.isWinner(opponent)) {
                teachWinner(opponent);
            } else {
                teachLooser(opponent);
            }
        }
    }

    private void recordMove(Connect4NetworkPlayer player, double[] input, Connect4Move connect4Move) {
        moves[movesRecorded].player = player;
        moves[movesRecorded].input(input);
        moves[movesRecorded].move = connect4Move;
        ++movesRecorded;
    }

    private void teachLooser(Connect4NetworkPlayer player) {
        if (!learning) return;
        double[] output = new double[OUTPUT_SIZE];
        for (int i = 0; i < movesRecorded; i++) {
            HistoricalMove move = moves[i];
            if (move.player.equals(player)) {
                prepareOutput(output, move.move);
                negate(output);
                double learningFactor = significance(movesRecorded - i);
                if(learningFactor>=LOWEST_LEARNING_FACTOR)
                    teach(player, output, move.input, learningFactor);
            }
        }
    }

    private void teachTragicLooser(Connect4NetworkPlayer player, Connect4Game game, double[] input, double[] output) {
        if (!learning) return;
        teachLooser(player);
        if(true)return;
        double[] correctOutput = new double[OUTPUT_SIZE];
        for (int i = 0; i < OUTPUT_SIZE; i++) {
            correctOutput[i] = game.isLegal(Connect4Move.getMove(i)) ? output[i] : 0;
        }
        teach(player, correctOutput, input, significance(0));
    }

    private void teach(Connect4NetworkPlayer player, double[] output, double[] input, double learningFactor) {
        player.setLearningFactor(learningFactor);
        player.learn(input, output);
    }

    private void negate(double[] output) {
        for (int i = 0; i < output.length; i++) {
            output[i] = 1 - output[i];
        }
    }

    private void teachWinner(Connect4NetworkPlayer player) {
        if (!learning) return;
        double[] output = new double[OUTPUT_SIZE];
        for (int i = 0; i < movesRecorded; i++) {
            HistoricalMove move = moves[i];
            if (move.player.equals(player)) {
                prepareOutput(output, move.move);
                teach(player, output, move.input, significance(movesRecorded - i));
            }
        }
    }

    public static final double LOWEST_LEARNING_FACTOR = .00001;
    public static final double HIGHEST_LEARNING_FACTOR = .01;
    public static final double LEARNING_FACTOR_DESCENT = .6;
    private static final double[] SIGNIFICANCES = IntStream.range(0, MAX_MOVES).mapToDouble(i -> Math.max(LOWEST_LEARNING_FACTOR, HIGHEST_LEARNING_FACTOR * Math.pow(LEARNING_FACTOR_DESCENT, i))).toArray();

    private double significance(int move) {
        return SIGNIFICANCES[move];
    }

    private void prepareOutput(double[] output, Connect4Move move) {
        Arrays.fill(output, 0);
        output[move.getColumn()] = 1;
    }

    public boolean isDrawn() {
        return game.isFinished() && game.isWinner(player1) == game.isWinner(player2);
    }

    public Connect4NetworkPlayer winner() {
        return game.isWinner(player1) ? player1 : player2;
    }

    public GamePoints points() {
        if (isDrawn()) {
            return GamePoints.DRAW;
        } else {
            if (game.isWinner(player1)) return GamePoints.PLAYER_1_WINS;
            return GamePoints.PLAYER_2_WINS;
        }
    }
}
