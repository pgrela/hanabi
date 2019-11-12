package com.pgrela.games.hanabi;

import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.Printer;
import com.pgrela.games.hanabi.domain.genetic.CheaterGenome;
import com.pgrela.games.hanabi.domain.player.Cheater;

import com.pgrela.games.hanabi.domain.player.PredictiveMultiHinter;
import java.util.ArrayList;
import java.util.List;

public class Hanabi {

    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>();
        int simulations = 10000;
        for (int i = 0; i < simulations; i++) {
            // [1D--,--24,-2-1,1H-H,--1D,---2,11--,-1--,--2H,4---]
            // [15--,1--1,5D--,--3-,2--H,D3--,D--3,---4,-1H-,-3--]
            // [15--,5--3,5D--,--3-,2--H,-5-2,D--3,---4,D3--,-3--]
            //Game game = GameFactory.setupNewGame(CheatingFriends.fromString("[[11--,--5-,3-DD,1---,--4-,-1-H,-45-,-H-2,HD-2,--31];[1-3-,--2H,3--3,-4--,D14H,---3,D53-,---3,4---,--4-];[D5--,-4-3,-11-,--5H,-D1D,-5-2,1-1-,1-52,1---,---2];[5-13,321-,51--,-H45,-2D-,1-D-,D--3,-3HD,4---,---4]]").friends(), Deck.shuffle(Card.BASIC_DECK));
            //Game game = GameFactory.setupNewGame(CheatingFriends.fromString("[[4-4-,2---,--1-,-3--,D-4-,1H32,-DH1,---4,D2--,-3--];[5152,41--,1-1D,5---,D-1H,1--D,--D5,-4--,DD4H,DD14];[-13H,-451,43H-,1---,2HHD,--4-,HH41,DDH5,-1--,--2D];[-HH1,D-2-,31D-,4--H,4HH1,1-HH,D13-,-32-,4-H-,D--2]]").friends(), Deck.shuffle(Card.BASIC_DECK));
            Game game = GameFactory.setupNewGame(()->new Cheater(CheaterGenome.fromString("[3-4-,41--,D4--,D-3-,1--1,--D2,D--2,-H-1,-3--,4---]")), 4);
            //Game game = GameFactory.setupNewGame(()->new Cheater(CheaterGenome.fromString("[4--4,31H-,1-5-,-24-,24--,1D--,H2-D,---2,--1-,-3--]")), 4);
            //Game game = GameFactory.setupNewGame(()->new Cheater(CheaterGenome.fromString("[-12-,-21-,21--,12--,---1,--1-,1---,-1--,--H-,---H]")), 4);
            //Game game = GameFactory.setupNewGame(PredictiveMultiHinter::new, 5);
            game.start();
            scores.add(game.score());
            if (game.score() ==924 || game.getAvailableBlownTokens()<3) {
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
