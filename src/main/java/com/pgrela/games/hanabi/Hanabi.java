package com.pgrela.games.hanabi;

import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.player.MiserablePlayer;

import java.util.ArrayList;
import java.util.List;

public class Hanabi {

    public static void main(String[] args) {
        int c = 0;
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            Game game = GameFactory.setupNewGame(MiserablePlayer::new);
            game.start();
            scores.add(game.score());
            if (game.score() > 19) {
                ++c;
            }
            if (game.score() > 20) {
                //System.out.println(game.score());
            }
            //System.out.println(game.score());
        }
        System.out.println(String.format("Scores: min %d, max %d, avg %f.",
                scores.stream().mapToInt(i -> i).min().getAsInt(),
                scores.stream().mapToInt(i -> i).max().getAsInt(),
                scores.stream().mapToInt(i -> i).average().getAsDouble()
        ));
        //System.out.println(c);
    }

}
