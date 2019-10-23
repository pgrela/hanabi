package com.pgrela.algorithms.genetic.engine;

public class Pangea {
    public static void main(String[] args) {
        Zoologist zoologist = new Zoologist();

        JuvenileHerd herd = new JuvenileHerd();
        Jungle jungle = new Jungle();
        MatureHerd matureHerd = herd.seekAdventures(jungle);

        for (int year = 0; year < 100; year++) {
            herd = matureHerd.matingSeason(new Ritual());
            matureHerd = herd.seekAdventures(jungle);
        }

        HerdStatistics statistics = zoologist.observe(matureHerd);
        System.out.println(statistics);
    }
}
