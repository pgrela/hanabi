package com.pgrela.neural.ui.rps;

import com.pgrela.neural.ui.rps.game.Figure;

import java.util.HashMap;
import java.util.Map;

public enum OutputLabels {
    ROCK(Figure.ROCK), PAPER(Figure.PAPER), SCISSORS(Figure.SCISSORS);
    private final Figure figure;
    private static final Map<Figure, OutputLabels> map = new HashMap<>();

    static {
        for (OutputLabels labels : OutputLabels.values()) {
            map.put(labels.getFigure(), labels);
        }
    }

    OutputLabels(Figure figure) {
        this.figure = figure;
    }

    public static OutputLabels from(Figure figure) {
        return map.get(figure);
    }

    public Figure getFigure() {
        return figure;
    }
}
