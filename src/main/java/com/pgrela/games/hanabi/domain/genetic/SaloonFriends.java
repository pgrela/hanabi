package com.pgrela.games.hanabi.domain.genetic;

import com.pgrela.algorithms.genetic.engine.DoubleSurvivalSkills;
import com.pgrela.algorithms.genetic.engine.MatingSeason;
import com.pgrela.algorithms.genetic.engine.Pangea;
import com.pgrela.algorithms.genetic.engine.PangeaBuilder;
import com.pgrela.algorithms.genetic.engine.SurvivalSkills;
import com.pgrela.algorithms.genetic.engine.Zoologist;
import com.pgrela.games.hanabi.domain.Card;
import com.pgrela.games.hanabi.domain.Deck;
import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.Printer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaloonFriends {
    private static final int PLAYERS = 4;

    public static final int TESTING_DECKS = 100;

    public static void main(String[] args) {
        List<Deck> decks = Stream.generate(() -> Deck.shuffle(Card.BASIC_DECK)).limit(TESTING_DECKS).collect(Collectors.toList());
        //= Collections.singletonList(Deck.deserialize("[GREEN-4,RED-3,RED-3,GREEN-1,YELLOW-2,GREEN-2,WHITE-2,GREEN-1,YELLOW-1,WHITE-5,WHITE-3,YELLOW-4,RED-5,RED-4,BLUE-2,YELLOW-3,GREEN-4,RED-1,BLUE-4,YELLOW-5,BLUE-4,GREEN-1,WHITE-2,GREEN-3,YELLOW-3,RED-2,WHITE-3,GREEN-2,GREEN-3,RED-4,WHITE-4,BLUE-2,BLUE-1,YELLOW-4,YELLOW-1,WHITE-1,GREEN-5,BLUE-1,WHITE-1,RED-1,YELLOW-2,RED-2,BLUE-1,RED-1,WHITE-1,BLUE-5,YELLOW-1,WHITE-4,BLUE-3,BLUE-3]"));
        //System.out.println(test(decks, CheatingFriends.fromString("[--43,D41D,D5--,233H,3--4,44-H,--3-,5---,---4,-3-H]")));if(true)return;
        //System.out.println(test(decks, CheatingFriends.fromString("[-12-,-21-,21--,12--,--H-,---H,---1,--1-,1---,-1--]")));if(true)return;
        Pangea pangea = new PangeaBuilder<CheatingFriends>()
                .withHerd(Stream.generate(() -> CheatingFriends.random(PLAYERS)).limit(1000).collect(Collectors.toList()))
                .withJungle(genome -> test(decks, genome))
                .withRitual(new MatingSeason<CheatingFriends>()
                        .killRandomlyLast(80, 10, () -> CheatingFriends.random(PLAYERS))
                        .preserveBest(5)
                        .breedBest(20, 40)
                        .mutateBest(10)
                        .mutateBest(15)
                        .mutateBest(30)
                        //.shuffleBest(20)
                )
                .withZoologists(new Zoologist())
                .forGenerations(300)
                .create();
        pangea.start();
    }

    private static SurvivalSkills test(List<Deck> decks, CheatingFriends genome) {
        int score = 0;
        for (Deck deck : decks) {
            Game game = GameFactory.setupNewGame(genome.friends(), deck.clone());
            game.start();
            score += -(25-game.score())*(25-game.score());
            if(score>0){
                ((Printer)game.getSpectators().iterator().next()).print();
                System.exit(0);
            }
        }
        return new DoubleSurvivalSkills(25-Math.sqrt(-score / (double) decks.size()));
    }
}
