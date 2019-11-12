package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.CardPlayedOutcome;
import com.pgrela.games.hanabi.domain.api.Fireworks;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Spectator;
import com.pgrela.games.hanabi.domain.api.Table;
import com.pgrela.games.hanabi.domain.api.UnknownCard;
import com.pgrela.games.hanabi.domain.hint.ColorHintAnyone;
import com.pgrela.games.hanabi.domain.hint.NumberHintAnyone;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Game implements Table {

    private Deck deck;
    private FireworksImpl fireworks;
    private int availableHintTokens;
    private int usedHintTokens;
    private int availableBlownTokens;
    private boolean lastRound;
    private ThePlayer lastPlayer;
    private boolean gameFinished;

    private List<? extends Spectator> spectators;
    private List<ThePlayer> players;

    private Queue<Consumer<Spectator>> events = new LinkedList<>();
    private Queue<Consumer<ThePlayer>> announcements = new LinkedList<>();

    Game(Deck deck, FireworksImpl fireworks, int availableHintTokens, int usedHintTokens,
         int availableBlownTokens, boolean lastRound, ThePlayer lastPlayer, boolean gameFinished,
         List<? extends Spectator> spectators,
         List<ThePlayer> players) {
        this.deck = deck;
        this.fireworks = fireworks;
        this.availableHintTokens = availableHintTokens;
        this.usedHintTokens = usedHintTokens;
        this.availableBlownTokens = availableBlownTokens;
        this.lastRound = lastRound;
        this.lastPlayer = lastPlayer;
        this.gameFinished = gameFinished;
        this.spectators = spectators;
        this.players = players;
    }

    private boolean isFinished() {
        return gameFinished || fireworks.areFinished() || areBlownTokensExhausted();
    }

    public void start() {
        spectators.forEach(Spectator::gameStarts);
        int currentPlayer = 0;
        while (!isFinished()) {
            ThePlayer player = players.get(currentPlayer++ % players.size());
            Turn turn = player.getPlayer().doTheMove();
            turn.execute(this, player);
            turnPlayed(player);
            while (!events.isEmpty()) {
                spectators.forEach(events.poll());
            }
            while (!announcements.isEmpty()) {
                players.forEach(announcements.poll());
            }
        }
    }

    void giveHint(ThePlayer player, OtherPlayer otherPlayer, Color color) {
        ThePlayer theOtherPlayer = (ThePlayer) otherPlayer;
        List<KnownCard> indicatedCards = theOtherPlayer.getHand().getRealCards().stream()
                .filter(h -> h.getColor().equals(color))
                .collect(Collectors.toList());
        giveHint(new ColorHintAnyone(player, otherPlayer, indicatedCards, color));
    }

    void giveHint(ThePlayer player, OtherPlayer otherPlayer, Number number) {
        ThePlayer theOtherPlayer = (ThePlayer) otherPlayer;
        List<KnownCard> indicatedCards = theOtherPlayer.getHand().getRealCards().stream()
                .filter(h -> h.getNumber().equals(number))
                .collect(Collectors.toList());
        giveHint(new NumberHintAnyone(player, otherPlayer, indicatedCards, number));
    }

    private void giveHint(ColorHintAnyone colorHint) {
        decreaseHintTokens();
        events.add((spectator -> spectator.hintGiven(colorHint)));
        announcements.add((player -> player.dispatchHint(colorHint)));
    }

    private void giveHint(NumberHintAnyone numberHint) {
        decreaseHintTokens();
        events.add((spectator -> spectator.hintGiven(numberHint)));
        announcements.add((player -> player.dispatchHint(numberHint)));
    }

    CardPlayedOutcome playCard(ThePlayer player, UnknownCard unknownCard) {
        KnownCard card = player.getHand().remove(unknownCard);
        if (fireworks.canAccept(card)) {
            fireworks.add(card);
            if(card.getNumber().equals(Number.FIVE)){
                increaseHintTokens();
            }
            events.add((spectator -> spectator.cardPlayed(player, card, CardPlayedOutcome.SUCCESS)));
            announcements.add((aplayer -> aplayer.getPlayer().cardPlayed(player, card, CardPlayedOutcome.SUCCESS)));
            return CardPlayedOutcome.SUCCESS;
        } else {
            blow();
            events.add((spectator -> spectator.cardPlayed(player, card, CardPlayedOutcome.FAIL)));
            announcements.add((aplayer -> aplayer.getPlayer().cardPlayed(player, card, CardPlayedOutcome.FAIL)));
            return CardPlayedOutcome.FAIL;
        }
    }

    void discardCard(ThePlayer player, UnknownCard unknownCard) {
        Card card = (Card) unknownCard;
        if (!player.getHand().contains(unknownCard)) {
            throw new IllegalGameMoveException("");
        }
        player.getHand().remove(card);
        increaseHintTokens();
        events.add((spectator -> spectator.cardDiscarded(player, card)));
        announcements.add((aPlayer -> aPlayer.getPlayer().cardDiscarded(player, card)));
    }

    Card drawCard(ThePlayer player) {
        Card card = deck.draw();
        player.getHand().add(player.getPlayer().acceptDrawnCard(card), card);
        events.add((spectator -> spectator.cardDrawn(player, card)));
        announcements.add((aPlayer -> aPlayer.getPlayer().cardDrawn(player, card)));
        return card;
    }

    private void turnPlayed(ThePlayer player) {
        events.add(Spectator::turnPlayed);
        if (lastRound) {
            if (player.equals(lastPlayer)) {
                gameFinished = true;
                events.add(Spectator::gameFinished);
                announcements.add(aPlayer -> aPlayer.getPlayer().gameFinished());
            }
            return;
        }
        if (isDeckEmpty()) {
            lastRound = true;
            lastPlayer = player;
            events.add((spectator -> spectator.theLastCardDrawn(player)));
            announcements.add((aPlayer -> aPlayer.getPlayer().theLastCardDrawn(player)));
        }
    }

    private boolean areBlownTokensExhausted() {
        return availableBlownTokens == 0;
    }


    private void decreaseHintTokens() {
        if (availableHintTokens == 0) {
            throw new IllegalGameMoveException("No more hint tokens");
        }
        --availableHintTokens;
        ++usedHintTokens;
    }

    private void blow() {
        if (availableBlownTokens == 0) {
            throw new IllegalGameMoveException("No more blown tokens!");
        }
        --availableBlownTokens;
        if (availableBlownTokens == 0) {
            events.add(Spectator::gameFinished);
        }
    }


    private void increaseHintTokens() {
        if (usedHintTokens > 0) {
            ++availableHintTokens;
            --usedHintTokens;
        }
    }

    public boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    public Fireworks getFireworks() {
        return fireworks;
    }

    List<ThePlayer> getPlayers() {
        return players;
    }

    public int getAvailableHintTokens() {
        return availableHintTokens;
    }

    public int getAvailableBlownTokens() {
        return availableBlownTokens;
    }

    public int remainingDeckSize() {
        return deck.size();
    }

    public boolean areHintTokensAvailable() {
        return availableHintTokens > 0;
    }

    public int score() {
        return fireworks.score();
    }

    public List<? extends Spectator> getSpectators() {
        return spectators;
    }

    public Deck copyDeck(){
        return deck.clone();
    }
}
