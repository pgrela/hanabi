package com.pgrela.neural.league;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Federation {

    private Idiot[] idiots;
    private Random chaos = new Random(56444);

    private League[] leagues;

    private int newbies;

    public Federation(int leagues, int competitorsPerLeague, int idiotsCount) {
        this.leagues = new League[leagues];
        idiots = new Idiot[idiotsCount];
        for (int i = 0; i < idiotsCount; i++) {
            idiots[i] = new Idiot(i + 347444);
        }
        for (int i = 0; i < leagues; i++) {
            String name = i == 0 ? "Diamond" : "League " + i;
            int seed = i * competitorsPerLeague;
            this.leagues[i] = new League(idiots, competitorsPerLeague, seed, name);
        }
        newbies = leagues * competitorsPerLeague;
    }

    public static void main(String[] args) {
        if (false) {
            Stadium stadium = new Stadium();
            Connect4NetworkPlayer player = new Connect4NetworkPlayer(3);
            for (int j = 0; j < 2000; j++) {
                Idiot idiot = new Idiot(j);
                for (int i = 0; i < 1000000; i++) {
                    stadium.newGame(idiot, player);
                    stadium.playWithLearning();
                    if (player.equals(stadium.winner())) {
                        if (i > 20) {
                            System.out.println(i);
                        }
                        break;
                    }
                }
            }
        }

        Federation federation = new Federation(10, 30, 100);
        int seasons = 1000;
        for (int i = 0; i < seasons; i++) {
            long start = System.nanoTime();
            System.out.println("Season " + (i + 1));
            federation.playSeason(2);
            federation.exchangeCompetitors(2);
            System.out.printf("Took %dms\n", (System.nanoTime()-start)/1_000_000);
        }
    }

    private void exchangeCompetitors(int amount) {
        for (int i = 1; i < leagues.length; i++) {
            exchangeCompetitors(leagues[i - 1], leagues[i], amount);
        }
//        League worstLeague = leagues[leagues.length - 1];
//        worstLeague.removeWorsts(amount);
//        worstLeague.add(Stream.generate(() -> new Connect4NetworkPlayer(++newbies)).limit(amount).collect(Collectors.toList()));

    }

    private void exchangeCompetitors(League better, League worse, int amount) {
        List<Connect4NetworkPlayer> worsts = better.removeWorsts(amount);
        List<Connect4NetworkPlayer> bests = worse.removeBests(amount);
        better.add(bests);
        worse.add(worsts);
    }

    private void playSeason(int rounds) {
        Arrays.stream(leagues).parallel().forEach(league -> {
            league.playSeason(rounds);
            league.tourWinner();
            league.chaos(chaos);
        });
    }
}
