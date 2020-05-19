package com.pgrela.neural.ui;

import com.pgrela.neural.ui.rps.Game;
import com.pgrela.neural.ui.rps.RockPaperScissorsUI;
import com.pgrela.neural.ui.rps.game.Figure;
import com.pgrela.neural.ui.rps.game.RPSGame;
import com.pgrela.neural.ui.rps.game.RPSGameSpectator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RPSTrainer implements RPSGameSpectator {

    private final Game game;
    private Random random = new Random();
    private Figure[] figures = Figure.values();
    private int figuresSize = figures.length;
    private Figure opponentPlayed;
    private List<Runnable> strategies = new ArrayList<>();

    {
        strategies.add(this::winAgainstPrevious);
        strategies.add(this::looseAgainstPrevious);
        strategies.add(this::doubleFigure);
        strategies.add(this::tripleFigure);
        strategies.add(this::previousFigure);
        strategies.add(this::nextFigure);
        strategies.add(this::mirror);
        strategies.add(this::single);
        strategies.add(this::random);
        strategies.add(this::random);
    }

    public RPSTrainer() {
        game = new Game(this);
        //game.loadNetwork(RockPaperScissorsUI.RPS_NN);
        game.setLearningFactor(0.01);
        IntStream.range(0,100_000).forEach(i->this.random());
        IntStream.range(0,100_000).forEach(i->this.play());
        game.saveNN(RockPaperScissorsUI.RPS_NN);
    }

    private void play() {
        game.newGame();
        strategies.get(random.nextInt(strategies.size())).run();
    }

    private void single() {
        Figure first = randomFigure();
        while (!game.getScore().isFinished()) {
            game.humanPlays(first);
        }
    }

    private void winAgainstPrevious() {
        Figure first = randomFigure();
        game.humanPlays(first);
        while (!game.getScore().isFinished()) {
            game.humanPlays(opponentPlayed.lossesTo());
        }
    }

    private void looseAgainstPrevious() {
        Figure first = randomFigure();
        game.humanPlays(first);
        while (!game.getScore().isFinished()) {
            game.humanPlays(opponentPlayed.lossesTo().lossesTo());
        }
    }

    private void doubleFigure() {
        while (!game.getScore().isFinished()) {
            Figure first = randomFigure();
            game.humanPlays(first);
            if (!game.getScore().isFinished())
                game.humanPlays(first);
        }
    }
    private void tripleFigure() {
        while (!game.getScore().isFinished()) {
            Figure first = randomFigure();
            game.humanPlays(first);
            if (!game.getScore().isFinished())
                game.humanPlays(first);
            game.humanPlays(first);
            if (!game.getScore().isFinished())
                game.humanPlays(first);
        }
    }

    private void nextFigure() {
        int i=random.nextInt(figuresSize);
        while (!game.getScore().isFinished()) {
            game.humanPlays(figures[i=(i+1)%figuresSize]);
        }
    }

    private void mirror() {
        game.humanPlays(randomFigure());
        while (!game.getScore().isFinished()) {
            game.humanPlays(opponentPlayed);
        }
    }

    private void random() {
        while (!game.getScore().isFinished()) {
            game.humanPlays(randomFigure());
        }
    }

    private void previousFigure() {
        int i=random.nextInt(figuresSize);
        while (!game.getScore().isFinished()) {
            game.humanPlays(figures[i=(i+2)%figuresSize]);
        }
    }

    private Figure randomFigure() {
        return figures[random.nextInt(figuresSize)];
    }

    public static void main(String[] args) {
        new RPSTrainer();
    }

    @Override
    public void turnPlayed(RPSGame game, Figure human, Figure opponent) {
        this.opponentPlayed = opponent;
    }
}
