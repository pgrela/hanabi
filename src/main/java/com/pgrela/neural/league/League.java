package com.pgrela.neural.league;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;


public class League {
    private final Idiot[] idiots;
    private List<Connect4NetworkPlayer> networks;

    private Stadium stadium;
    private final int competitors;
    private List<Entry<Connect4NetworkPlayer, Integer>> ranked;
    private String name;

    public League(Idiot[] idiots, int competitors, int seed, String name) {
        this.idiots = idiots;
        this.competitors = competitors;
        this.name = name;
        networks = new ArrayList<>();
        for (int i = 0; i < competitors; i++) {
            Connect4NetworkPlayer network = new Connect4NetworkPlayer(i + seed);
            networks.add(network);
        }
        stadium = new Stadium();

    }

    public void playSeason(int rounds) {
        ranked = ranked(rounds);
    }

    public void tourWinner() {
        Connect4NetworkPlayer winner = ranked.get(0).getKey();
        int tour = tour(winner);
        System.out.printf("%17s: %20s scored %4d out of %5d\n", name, winner, tour, 6 * idiots.length);
    }

    private int tour(Connect4NetworkPlayer player) {
        int points = 0;
        for (int i = 0; i < idiots.length; i++) {
            stadium.newGame(player, idiots[i]);
            stadium.playForFun();
            points += stadium.points().getPlayer1Points();
            stadium.newGame(idiots[i], player);
            stadium.playForFun();
            points += stadium.points().getPlayer2Points();
        }
        return points;
    }

    private List<Entry<Connect4NetworkPlayer, Integer>> ranked(int rounds) {
        Map<Connect4NetworkPlayer, Integer> scores = new HashMap<>();
        for (int round = 0; round < rounds; round++) {
            for (int i = 0; i < networks.size(); i++) {
                for (int j = 0; j < i; j++) {
                    playRanked(scores, networks.get(i), networks.get(j));
                    playRanked(scores, networks.get(j), networks.get(i));
                }
            }
        }
        Comparator<Entry<Connect4NetworkPlayer, Integer>> comparator = Entry.comparingByValue();
        comparator = comparator.reversed();
        return scores.entrySet().stream().sorted(comparator).collect(Collectors.toList());
    }

    private void playRanked(Map<Connect4NetworkPlayer, Integer> scores, Connect4NetworkPlayer player1, Connect4NetworkPlayer player2) {
        stadium.newGame(player1, player2);
        stadium.playWithLearning();
        GamePoints points = stadium.points();
        scores.put(player1, scores.getOrDefault(player1, 0) + points.getPlayer1Points());
        scores.put(player2, scores.getOrDefault(player2, 0) + points.getPlayer2Points());
    }


    public static void publishBoard(List<Entry<Connect4NetworkPlayer, Integer>> scores) {
        System.out.println("========== LEAGUE RESULTS ==========");
        for (int i = 0; i < scores.size(); i++) {
            Entry<Connect4NetworkPlayer, Integer> score = scores.get(i);
            String rank = i > 0 && scores.get(i - 1).getValue().equals(score.getValue()) ? "" : String.valueOf(i + 1) + ".";
            System.out.println(String.format("%5s [%4d pts] \t%s", rank, score.getValue(), score.getKey()));
        }
        System.out.println("========== ============== ==========");
    }

    public List<Connect4NetworkPlayer> removeWorsts(int amount) {
        List<Connect4NetworkPlayer> worsts = new ArrayList<>();
        for (int count = ranked.size() - 1; worsts.size() < amount; count--) {
            Connect4NetworkPlayer worse = ranked.get(count).getKey();
            networks.remove(worse);
            worsts.add(worse);
        }

        return worsts;
    }

    public List<Connect4NetworkPlayer> removeBests(int amount) {
        List<Connect4NetworkPlayer> bests = new ArrayList<>();
        for (int i = 0; bests.size() < amount; i++) {
            Connect4NetworkPlayer better = ranked.get(i).getKey();
            networks.remove(better);
            bests.add(better);
        }

        return bests;
    }

    public void add(List<Connect4NetworkPlayer> newbies) {
        networks.addAll(newbies);
    }

    public void chaos(Random chaos) {
        for (int i = 0; i < ranked.size(); i++) {
            ranked.get(i).getKey().chaos(chaos, i/2);
        }

    }
}
