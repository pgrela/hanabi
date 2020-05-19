package com.pgrela.neural.ui.rps.game;

import java.util.ArrayList;
import java.util.Collection;

public class RPSGame {
    private final int bestOf;
    private Score score;
    private Collection<RPSGameSpectator> spectators = new ArrayList<>();

    private RPSGame(int bestOf) {
        this.bestOf = bestOf;
        score = new Score(bestOf);
    }

    public void registerSpectator(RPSGameSpectator spectator) {
        spectators.add(spectator);
    }

    public void leaveAudience(RPSGameSpectator spectator) {
        spectators.remove(spectator);
    }

    private void finish() {
        spectators.clear();
    }

    public RPSGame() {
        this(5);
    }

    public void showdown(Figure human, Figure opponent) {
        if(score.isFinished())return;
        spectators.forEach(s->s.turnPlayed(this, human, opponent));
        if (human.lossesTo().equals(opponent)) score.addOpponentPoint();
        if (opponent.lossesTo().equals(human)) score.addHumanPoint();
        spectators.forEach(s -> s.scoreChanged(this));
        if(score.isFinished()){
            spectators.forEach(s -> s.gameFinished(this));
        }
    }

    public Score getScore() {
        return score;
    }

}
