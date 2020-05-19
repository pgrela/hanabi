package com.pgrela.neural.ui.rps;

import com.pgrela.neural.fast.Main;
import com.pgrela.neural.fast.Network;
import com.pgrela.neural.ui.rps.game.Figure;
import com.pgrela.neural.ui.rps.game.RPSGame;
import com.pgrela.neural.ui.rps.game.RPSGameSpectator;
import com.pgrela.neural.ui.rps.game.Score;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Game {

    public static final double LEARNING_FACTOR = .15;
    private Network network;
    private RPSGame rpsGame;
    private double[] gameState;
    private RPSGameSpectator spectator;

    public Game(RPSGameSpectator spectator) {
        this.spectator = spectator;
        network = new Network(1, LEARNING_FACTOR, 5, 1, InputLabels.values().length, 16, 16, OutputLabels.values().length);
        newGame();
    }

    public RPSGame newGame() {
        if (rpsGame != null) rpsGame.leaveAudience(spectator);
        rpsGame = new RPSGame();
        gameState = new double[InputLabels.values().length];
        rpsGame.registerSpectator(spectator);
        return rpsGame;
    }

    public void humanPlays(Figure actuallyPlayed) {
        double[] process = network.process(gameState);
        Figure predictedHumanFigure = interpret(process);
        Figure nnActuallyPlayed = predictedHumanFigure.lossesTo();
        network.learn(gameState, encode(actuallyPlayed));
        updateGameState(actuallyPlayed, nnActuallyPlayed);
        rpsGame.showdown(actuallyPlayed, nnActuallyPlayed);
    }

    private void updateGameState(Figure actuallyPlayed, Figure nnActuallyPlayed) {
        for (int i = gameState.length - 1; i >= 6; i--) {
            gameState[i] = gameState[i - 6];
        }
        for (int i = 0; i < 6; i++) {
            gameState[i]=0;
        }
        gameState[actuallyPlayed.ordinal()] = 1;
        gameState[nnActuallyPlayed.ordinal() + Figure.values().length] = 1;
    }

    private double[] encode(Figure figure) {
        double[] doubles = new double[OutputLabels.values().length];
        doubles[OutputLabels.from(figure).ordinal()] = 1;
        return doubles;
    }

    private Figure interpret(double[] o) {
        int m = 0;
        if (o[1] >= o[2] && o[1] >= o[0]) m = 1;
        if (o[2] >= o[1] && o[2] >= o[0]) m = 2;
        return OutputLabels.values()[m].getFigure();
    }

    public Score getScore() {
        return this.rpsGame.getScore();
    }

    public Map<Figure, Double> getPredictions() {
        double[] o = network.process(gameState);
        double sum = o[0] + o[1] + o[2];
        Map<Figure, Double> map = new HashMap<>(3);
        for (Figure value : Figure.values()) {
            map.put(value, o[OutputLabels.from(value).ordinal()] / sum);
        }
        return map;
    }

    public void loadNetwork(String filename) {
        network = Network.fromFile(new File(Main.SAVE_DIR.toFile(), filename));
        network.setLearningFactor(LEARNING_FACTOR);
    }

    public void saveNN(String filename) {
        network.toFile(new File(Main.SAVE_DIR.toFile(), filename));
    }

    public void setLearningFactor(double factor) {
        network.setLearningFactor(factor);
    }
}
