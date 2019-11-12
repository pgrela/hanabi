package com.pgrela.algorithms.genetic.engine;

import com.pgrela.algorithms.genetic.api.Genome;
import com.pgrela.games.hanabi.domain.Card;
import com.pgrela.games.hanabi.domain.Deck;
import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.genetic.CheaterGenome;
import com.pgrela.games.hanabi.domain.genetic.CheatingFriends;
import com.pgrela.games.hanabi.domain.player.Cheater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Zoologist {
    private static final Deck[] TEST_DECKS = new Deck[500];
    static {
        for (int i = 0; i < TEST_DECKS.length; i++) {
            TEST_DECKS[i]=Deck.shuffle(Card.BASIC_DECK);
        }
    }
    private String leader="";
    public HerdStatistics observe(MatureHerd matureHerd) {
        Collection<MatureGenosaur> members = matureHerd.members();
        HerdStatistics statistics = new HerdStatistics(members.stream().map(MatureGenosaur::survivalSkills).toArray(SurvivalSkills[]::new));
        System.out.print(statistics);
        Comparator<MatureGenosaur> comparing = Comparator.comparing(MatureGenosaur::survivalSkills);
        Genome rejuvenate = members.stream().max(comparing).get().rejuvenate();
        String leader = rejuvenate.serialize();
        if (!leader.equals(this.leader)) {
            System.out.print(" :" + test(leader, rejuvenate.getClass()) + " ");
            System.out.print(leader);
        }
        System.out.println();
        this.leader = leader;

        return statistics;
    }

    private String test(String leader, Class<? extends Genome> aClass) {
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < TEST_DECKS.length; i++) {
            List<Cheater> players = getPlayers(leader, aClass);
            Game game = GameFactory.setupNewGame(players, TEST_DECKS[i].clone());
            game.start();
            scores.add(game.score());
        }
        return String.format("%.2f (%.2f%%)", scores.stream().mapToInt(i->i).average().getAsDouble(), scores.stream().filter(i->i==25).count()*100./TEST_DECKS.length);
    }

    private List<Cheater> getPlayers(String leader, Class<? extends Genome> aClass) {
        if(aClass.isAssignableFrom(CheatingFriends.class))
            return CheatingFriends.fromString(leader).friends();
        if(aClass.isAssignableFrom(CheaterGenome.class))
            return Stream.generate(()->CheaterGenome.fromString(leader)).map(Cheater::new).limit(4).collect(Collectors.toList());
        throw new RuntimeException();
    }
}
