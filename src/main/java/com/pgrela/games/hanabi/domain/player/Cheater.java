package com.pgrela.games.hanabi.domain.player;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.ThePlayer;
import com.pgrela.games.hanabi.domain.Turn;
import com.pgrela.games.hanabi.domain.api.CardPlayedOutcome;
import com.pgrela.games.hanabi.domain.api.CardValue;
import com.pgrela.games.hanabi.domain.api.ColorHintToMe;
import com.pgrela.games.hanabi.domain.api.ColorHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.MyHand;
import com.pgrela.games.hanabi.domain.api.NumberHintToMe;
import com.pgrela.games.hanabi.domain.api.NumberHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Player;
import com.pgrela.games.hanabi.domain.api.SomeonesHand;
import com.pgrela.games.hanabi.domain.api.Table;
import com.pgrela.games.hanabi.domain.api.UnknownCard;
import com.pgrela.games.hanabi.domain.genetic.CheaterGene;
import com.pgrela.games.hanabi.domain.genetic.CheaterGene.Action;
import com.pgrela.games.hanabi.domain.genetic.CheaterGenome;
import com.pgrela.games.hanabi.domain.genetic.CheaterNucleotide;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cheater implements Player, GeneticCheater {

    private List<OtherPlayer> nextPlayers;
    private MyHand myHand;
    private Table table;

    private LinkedList<UnknownCard> cardsToPlay = new LinkedList<>();

    private Set<CardValue> discardedCards = new HashSet<>();
    private Set<CardValue> hintedCards = new HashSet<>();
    private Map<OtherPlayer, LinkedList<KnownCard>> cardsToBePlayed;
    private FireworkSimulator simulator = new FireworkSimulator();
    private CheaterGenome genome;
    private Collection<UnknownCard> hold = new HashSet<>();
    private Collection<KnownCard> held = new HashSet<>();
    private Collection<KnownCard> discarding = new HashSet<>();
    private Deque<UnknownCard> cardsToDiscard = new ArrayDeque<>();

    public Cheater(CheaterGenome genome) {
        this.genome = genome;
    }


    public void setup(List<OtherPlayer> nextPlayers, MyHand myHand, Table table) {
        this.nextPlayers = nextPlayers;
        this.myHand = myHand;
        this.table = table;

        cardsToBePlayed = nextPlayers.stream()
                .collect(Collectors.toMap(p -> p, (p) -> new LinkedList<>()));
    }

    @Override
    public Turn doTheMove() {
        CheaterGene[] genes = genome.genes();
        if (!cardsToPlay.isEmpty()) {
            UnknownCard card = cardsToPlay.pop();
            cardsToDiscard.remove(card);
            return Turn.play(card);
        }
        if (table.areHintTokensAvailable()) {
            simulator.startSimulation();
            for (OtherPlayer player : nextPlayers) {
                if(cardsToBePlayed.get(player).isEmpty() && !canAnyDiscard(player.getHand().getKnownCards())) {
                    for (int i = 0; i < genes.length; i++) {
                        if (applies(player.getHand(), genes[i])) {
                            simulator.rollbackAll();
                            return turn(i, player);
                        }
                    }
                }
                if (cardsToBePlayed.get(player).size() > 0) {
                    simulator.simulateAdd(cardsToBePlayed.get(player).peekFirst());
                }
            }
            simulator.rollbackAll();
            simulator.startSimulation();
            for (OtherPlayer player : nextPlayers) {
                for (int i = 0; i < genes.length; i++) {
                    if (applies(player.getHand(), genes[i])) {
                        simulator.rollbackAll();
                        return turn(i, player);
                    }
                }
                if (cardsToBePlayed.get(player).size() > 0) {
                    simulator.simulateAdd(cardsToBePlayed.get(player).peekFirst());
                }
            }
            simulator.rollbackAll();
        }
        if (!cardsToDiscard.isEmpty()) {
            return Turn.discard(cardsToDiscard.pop());
        }
        return Turn.discard(IntStream.rangeClosed(1, myHand.size()).mapToObj(i -> myHand.getCards().get(myHand.size() - i))
                .filter(card -> !hold.contains(card))
                .filter(card -> !cardsToPlay.contains(card))
                .findFirst()
                .orElse(myHand.getCards().iterator().next())
        );
    }

    private boolean canAnyDiscard(List<KnownCard> cards) {
        return cards.stream().anyMatch(discarding::contains);
    }

    private Turn turn(int gene, OtherPlayer player) {
        if (gene < Color.BASIC_COLORS.size()) {
            return Turn.hint(player, Color.BASIC_COLORS.get(gene));
        }
        return Turn.hint(player, Number.ONE_TO_FIVE.get(gene - Color.BASIC_COLORS.size()));
    }

    private int gene(Color color) {
        return Color.BASIC_COLORS.indexOf(color);
    }

    private int gene(Number number) {
        return Number.ONE_TO_FIVE.indexOf(number) + Color.BASIC_COLORS.size();
    }

    private int applies(SomeonesHand hand, CheaterGene gene) {
        int newInformation = 0;
        simulator.startSimulation();
        List<KnownCard> cards = hand.getKnownCards();
        for (CheaterNucleotide playing : CheaterGene.PLAYS) {
            for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
                KnownCard card = cards.get(cardIndex);
                if (playing.equals(gene.nucleotide(cardIndex))) {
                    if (simulator.canAccept(card) && !hintedCards.contains(card.value())) {
                        simulator.simulateAdd(card);
                        ++newInformation;
                    } else {
                        simulator.rollback();
                        return 0;
                    }
                }
            }
        }
        simulator.rollback();
        for (int i = 0; i < hand.size(); i++) {
            if (gene.nucleotide(i).equals(Action.HOLD)) {
                if (!cards.get(i).getNumber().equals(Number.ONE)) {
                    if (cards.get(i).getNumber().equals(Number.FIVE)) {
                        ++newInformation;
                        continue;
                    }
                    if (!discardedCards.contains(cards.get(i).value())) {
                        return 0;
                    }
                    if(!held.contains(cards.get(i)))
                        ++newInformation;
                }
            }
        }
        for (int i = 0; i < hand.size(); i++) {
            if (gene.nucleotide(i).equals(Action.DISCARD)) {
                KnownCard card = cards.get(i);
                if (!table.getFireworks().contains(card)) {
                    return 0;
                }
                if(!discarding.contains(cards.get(i)))
                    ++newInformation;
            }
        }
        return newInformation;
    }

    @Override
    public void receiveHint(ColorHintToMe colorHint) {
        acceptHint(((GeneticCheater)((ThePlayer)colorHint.getFromPlayer()).getPlayer()).genome().genes()[gene(colorHint.getColor())]);
    }

    @Override
    public void receiveHint(NumberHintToMe numberHint) {
        acceptHint(((GeneticCheater)((ThePlayer)numberHint.getFromPlayer()).getPlayer()).genome().genes()[gene(numberHint.getNumber())]);
    }

    private void acceptHint(CheaterGene gene) {
        List<UnknownCard> cards = myHand.getCards();
        for (CheaterNucleotide playing : CheaterGene.PLAYS) {
            for (int cardIndex = 0; cardIndex < myHand.size(); cardIndex++) {
                UnknownCard card = cards.get(cardIndex);
                if (playing.equals(gene.nucleotide(cardIndex))) {
                    cardsToPlay.add(card);
                }
            }
        }
        for (int i = 0; i < myHand.size(); i++) {
            if (gene.nucleotide(i).equals(Action.HOLD)) {
                hold.add(cards.get(i));
            }
            if (gene.nucleotide(i).equals(Action.DISCARD)) {
                if (!cardsToDiscard.contains(cards.get(i))) cardsToDiscard.add(cards.get(i));
            }
        }
    }

    private void processHint(CheaterGene gene, OtherPlayer player) {
        List<KnownCard> cards = player.getHand().getKnownCards();
        for (CheaterNucleotide playing : CheaterGene.PLAYS) {
            for (int cardIndex = 0; cardIndex < player.getHand().size(); cardIndex++) {
                KnownCard card = cards.get(cardIndex);
                if (playing.equals(gene.nucleotide(cardIndex))) {
                    cardsToBePlayed.get(player).add(card);
                    hintedCards.add(card.value());
                }
            }
        }
        for (int i = 0; i < cards.size(); i++) {
            if (gene.nucleotide(i).equals(Action.HOLD)) {
                held.add(cards.get(i));
            }
            if (gene.nucleotide(i).equals(Action.DISCARD)) {
                discarding.add(cards.get(i));
            }
        }
    }

    @Override
    public void hintGiven(ColorHintToOtherPlayer colorHint) {
        processHint(((GeneticCheater)((ThePlayer)colorHint.getFromPlayer()).getPlayer()).genome().genes()[gene(colorHint.getColor())], colorHint.getToPlayer());
    }

    @Override
    public void hintGiven(NumberHintToOtherPlayer numberHint) {
        processHint(((GeneticCheater)((ThePlayer)numberHint.getFromPlayer()).getPlayer()).genome().genes()[gene(numberHint.getNumber())], numberHint.getToPlayer());
    }

    @Override
    public void cardPlayed(OtherPlayer player, KnownCard card, CardPlayedOutcome outcome) {
        if (outcome.equals(CardPlayedOutcome.SUCCESS)) {
            simulator.add(card);
        }
        if (cardsToBePlayed.containsKey(player)) {
            if (!cardsToBePlayed.get(player).poll().equals(card)) {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void cardDiscarded(OtherPlayer player, KnownCard card) {
        discardedCards.add(card.value());
    }

    @Override
    public void theLastCardDrawn(OtherPlayer player) {

    }

    @Override
    public int acceptDrawnCard(UnknownCard card) {
        return 0;
    }

    @Override
    public CheaterGenome genome() {
        return genome;
    }
}
