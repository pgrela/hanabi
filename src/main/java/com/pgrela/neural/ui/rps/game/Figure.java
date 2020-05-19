package com.pgrela.neural.ui.rps.game;

public enum Figure {
    ROCK, PAPER, SCISSORS;
    private Figure lossesTo;

    static {
        ROCK.setLossesTo(PAPER);
        PAPER.setLossesTo(SCISSORS);
        SCISSORS.setLossesTo(ROCK);
    }

    private void setLossesTo(Figure lossesTo) {
        this.lossesTo = lossesTo;
    }

    public Figure lossesTo() {
        return lossesTo;
    }
    public boolean lossesTo(Figure figure) {
        return lossesTo.equals(figure);
    }
}
