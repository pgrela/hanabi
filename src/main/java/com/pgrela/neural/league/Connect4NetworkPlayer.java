package com.pgrela.neural.league;

import com.pgrela.games.engine.api.Player;
import com.pgrela.neural.fast.Network;
import com.pgrela.neural.utils.NameGenerator;

public class Connect4NetworkPlayer extends Network implements Player {

    public static final int INPUT_SIZE = 42 * 3;
    public static final int OUTPUT_SIZE = 7;

    private String name;

    public Connect4NetworkPlayer(int randomSeed) {
        super(1, 0.01, randomSeed, 1, INPUT_SIZE, 200, OUTPUT_SIZE);
        name = NameGenerator.name(randomSeed);
    }

    protected Connect4NetworkPlayer(String name) {
        super(new int[0],null, null, 1, 1, 1);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void getReady() {}
}
