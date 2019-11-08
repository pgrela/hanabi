package com.pgrela.games.hanabi.domain.player;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SingleHinter implements Player {

    private List<OtherPlayer> nextPlayers;
    private MyHand myHand;
    private Table table;

    private LinkedList<UnknownCard> cardsToPlay = new LinkedList<>();

    private Set<KnownCard> discardedCards = new HashSet<>();
    private Set<CardValue> hintedCards = new HashSet<>();

    public SingleHinter() {
    }


    public void setup(List<OtherPlayer> nextPlayers, MyHand myHand, Table table) {
        this.nextPlayers = nextPlayers;
        this.myHand = myHand;
        this.table = table;
    }

    @Override
    public Turn doTheMove() {
        if (!cardsToPlay.isEmpty()) {
            Optional<Turn> first = cardsToPlay.stream()
                    .filter(card -> myHand.getCards().contains(card)).map(Turn::play).findFirst();
            if (first.isPresent()) {
                return first.get();
            }

        }
        if (table.areHintTokensAvailable()) {
            for (OtherPlayer player : nextPlayers) {
                SomeonesHand hand = player.getHand();
                Set<Color> seenColors = new HashSet<>();
                Set<Number> seenNumbers = new HashSet<>();
                for (KnownCard card : hand.getKnownCards()) {
                    if (!hintedCards.contains(card.value()) && table.getFireworks().canAccept(card)) {
                        if (!seenColors.contains(card.getColor())) {
                            return Turn.hint(player, card.getColor());
                        }
                        if (!seenNumbers.contains(card.getNumber())) {
                            return Turn.hint(player, card.getNumber());
                        }
                    }
                    seenColors.add(card.getColor());
                    seenNumbers.add(card.getNumber());
                }
            }
        }
        return Turn.discard(myHand.getCards().get(myHand.getCards().size()-1));
    }

    @Override
    public void receiveHint(ColorHintToMe colorHint) {
        cardsToPlay.add(colorHint.getMyIndicatedCards().get(0));
    }

    @Override
    public void receiveHint(NumberHintToMe numberHint) {
        cardsToPlay.add(numberHint.getMyIndicatedCards().get(0));
    }

    @Override
    public void hintGiven(ColorHintToOtherPlayer colorHint) {
        KnownCard hintedCard = colorHint.getIndicatedCards().get(0);
        if(table.getFireworks().canAccept(hintedCard)) {
            hintedCards.add(hintedCard.value());
        }
    }

    @Override
    public void hintGiven(NumberHintToOtherPlayer numberHint) {
        KnownCard hintedCard = numberHint.getIndicatedCards().get(0);
        if(table.getFireworks().canAccept(hintedCard)) {
            hintedCards.add(hintedCard.value());
        }
    }

    @Override
    public void cardPlayed(OtherPlayer player, KnownCard card, CardPlayedOutcome outcome) {

    }

    @Override
    public void cardDiscarded(OtherPlayer player, KnownCard card) {
        discardedCards.add(card);
    }

    @Override
    public void theLastCardDrawn(OtherPlayer player) {

    }

    @Override
    public int acceptDrawnCard(UnknownCard card) {
        return 0;
    }
}
