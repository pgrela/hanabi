package com.pgrela.games.hanabi;

import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.Printer;
import com.pgrela.games.hanabi.domain.player.MultiHinter;
import com.pgrela.games.hanabi.domain.player.PredictiveMultiHinter;
import com.pgrela.games.hanabi.domain.player.SingleHinter;

import java.util.ArrayList;
import java.util.List;

public class Hanabi {

    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>();
        int simulations = 1000;
        for (int i = 0; i < simulations; i++) {
            Game game = GameFactory.setupNewGame(PredictiveMultiHinter::new, 5);
            game.start();
            scores.add(game.score());
            if (game.score() < 10 || game.getAvailableBlownTokens()<3) {
                ((Printer)game.getSpectators().get(0)).print();
                return;
            }
        }
        System.out.println(String.format("Scores: min %d, max %d, avg %f.",
                scores.stream().mapToInt(i -> i).min().getAsInt(),
                scores.stream().mapToInt(i -> i).max().getAsInt(),
                scores.stream().mapToInt(i -> i).average().getAsDouble()
        ));
        int[] s = new int[40];
        scores.forEach(i->++s[i]);
        int c=0;
        for (int i = 0; i < s.length; i++) {
            c+=s[i];
            if(s[i]>0){
                System.out.println(String.format("Score %-2d occurred %-6d times, %-7.2f %-7.2f %s", i, s[i], 100.*c/simulations, 100-100.*(c-s[i])/simulations, "*".repeat((100*s[i]+simulations-1)/simulations)));
            }
        }
    }

}
