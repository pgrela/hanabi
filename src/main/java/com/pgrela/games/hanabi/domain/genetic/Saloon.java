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
import com.pgrela.games.hanabi.domain.player.Cheater;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Saloon {

    private static final int PLAYERS = 4;

    public static final int VERIFIER_DECKS = 100;

    public static void main(String[] args) {
        List<Deck> decks = Stream.generate(() -> Deck.shuffle(Card.BASIC_DECK)).limit(
            VERIFIER_DECKS).collect(Collectors.toList());
        //= Collections.singletonList(Deck.deserialize("[GREEN-4,RED-3,RED-3,GREEN-1,YELLOW-2,GREEN-2,WHITE-2,GREEN-1,YELLOW-1,WHITE-5,WHITE-3,YELLOW-4,RED-5,RED-4,BLUE-2,YELLOW-3,GREEN-4,RED-1,BLUE-4,YELLOW-5,BLUE-4,GREEN-1,WHITE-2,GREEN-3,YELLOW-3,RED-2,WHITE-3,GREEN-2,GREEN-3,RED-4,WHITE-4,BLUE-2,BLUE-1,YELLOW-4,YELLOW-1,WHITE-1,GREEN-5,BLUE-1,WHITE-1,RED-1,YELLOW-2,RED-2,BLUE-1,RED-1,WHITE-1,BLUE-5,YELLOW-1,WHITE-4,BLUE-3,BLUE-3]"));
        //System.out.println(test(decks, CheaterGenome.fromString("[--43,D41D,D5--,233H,3--4,44-H,--3-,5---,---4,-3-H]")));if(true)return;
        //System.out.println(test(decks, CheaterGenome.fromString("[-12-,-21-,21--,12--,--H-,---H,---1,--1-,1---,-1--]")));if(true)return;
        Pangea pangea = new PangeaBuilder<CheaterGenome>()
                .withHerd(Stream.generate(CheaterGenome::random).limit(1100).collect(Collectors.toList()))
                .withJungle(genome -> verify(decks, genome))
                .withRitual(new MatingSeason<CheaterGenome>()
                        .killRandomlyLast(80, 20, CheaterGenome::random)
                        .preserveBest(1)
                        .breedBest(10, 39)
                        .mutateBest(5)
                        .mutateBest(15)
                        .mutateBest(40)
                )
                .withZoologists(new Zoologist())
                .forGenerations(1000)
                .create();
        pangea.start();
    }

    private static SurvivalSkills verify(List<Deck> decks, CheaterGenome genome) {
        int score = 0;
        for (Deck deck : decks) {
            Game game = GameFactory.setupNewGame(() -> new Cheater(genome), PLAYERS, deck.clone());
            game.start();
            score += game.score()*(game.score()==25?2:1);
            if(score<0){
                ((Printer)game.getSpectators().iterator().next()).print();
                System.exit(0);
            }
        }
        return new DoubleSurvivalSkills(score / (double) decks.size());
    }
}
