package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.CardPlayedOutcome;
import com.pgrela.games.hanabi.domain.api.ColorHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.NumberHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Spectator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Printer implements Spectator {

    private StringBuilder transcript = new StringBuilder();

    private static Map<Color, String> colored = new HashMap();
    private Game game;

    {
        colored.put(Color.RED, "\u001B[30m\u001B[41m");
        colored.put(Color.BLUE, "\u001B[30m\u001B[44m");
        colored.put(Color.YELLOW, "\u001B[30m\u001B[43m");
        colored.put(Color.GREEN, "\u001B[30m\u001B[42m");
        colored.put(Color.WHITE, "\u001B[30m\u001B[47m");
    }

    private static Map<Number, String> numbered = new HashMap();

    {
        numbered.put(Number.ONE, " 1 ");
        numbered.put(Number.TWO, " 2 ");
        numbered.put(Number.THREE, " 3 ");
        numbered.put(Number.FOUR, " 4 ");
        numbered.put(Number.FIVE, " 5 ");
    }

    public static final String DEFAULT = "\u001B[0m";

    public void snapshot() {
        for (ThePlayer player : game.getPlayers()) {
            transcript.append(player).append(": ");
            player.getHand().getRealCards().forEach(card -> transcript.append(toString(card) + " "));
            transcript.append('\n');
        }
        game.getFireworks().topCards().forEach(card -> transcript.append(toString(card) + " "));
        transcript.append(String.format(" %s %d %s ", "\u001B[30m\u001B[45m", game.remainingDeckSize(), DEFAULT));
        IntStream.range(0, game.getAvailableHintTokens()).mapToObj(i -> colored.get(Color.BLUE) + i + DEFAULT + " ").forEach(
                transcript::append);
        IntStream.range(0, game.getAvailableBlownTokens()).mapToObj(i -> colored.get(Color.RED) + i + DEFAULT + " ").forEach(
                transcript::append);
        transcript.append('\n');
        transcript.append('\n');
    }

    private String toString(KnownCard card) {
        if (card == null) return " 0 ";
        return colored.get(card.getColor()) + numbered.get(card.getNumber()) + DEFAULT;
    }

    private String toString(Color color) {
        return colored.get(color) + color.toString() + DEFAULT;
    }

    @Override
    public void hintGiven(ColorHintToOtherPlayer colorHint) {
        transcript.append(String.format("%s hinted %s about color %s, indicating %s.\n",
            colorHint.getFromPlayer(),
            colorHint.getToPlayer(),
            toString(colorHint.getColor()),
            toString(colorHint.getIndicatedCards())));
    }

    private String toString(List<KnownCard> cards) {
        return cards.stream().map(this::toString).collect(Collectors.joining());
    }

    @Override
    public void hintGiven(NumberHintToOtherPlayer numberHint) {
        transcript.append(String.format("%s hinted %s about number %s, indicating %s.\n",
            numberHint.getFromPlayer(),
            numberHint.getToPlayer(),
            numberHint.getNumber(),
            toString(numberHint.getIndicatedCards())));
    }

    @Override
    public void cardPlayed(OtherPlayer player, KnownCard card, CardPlayedOutcome outcome) {
        transcript.append(String.format("%s played %s, that resulted in %s.\n",
            player,
            toString(card),
            toString(outcome)));
    }

    private String toString(CardPlayedOutcome outcome) {
        if(outcome.equals(CardPlayedOutcome.SUCCESS)){
            return colored.get(Color.GREEN) + outcome + DEFAULT;
        }
        return colored.get(Color.RED) + outcome + DEFAULT;
    }

    @Override
    public void cardDiscarded(OtherPlayer player, KnownCard card) {
        transcript.append(String.format("%s discarded %s.\n",
            player,
            toString(card)));
    }

    @Override
    public void cardDrawn(OtherPlayer player, KnownCard card) {
        transcript.append(String.format("%s drew %s.\n",
            player,
            toString(card)));
    }

    @Override
    public void theLastCardDrawn(OtherPlayer player) {

    }

    @Override
    public void gameFinished() {
      transcript.append("Scored " + game.score() + " points.\n");
    }

    @Override
    public void turnPlayed() {
        snapshot();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void gameStarts() {
        Deck deck = game.copyDeck();
        transcript.append("DECK: ");
        while(!deck.isEmpty()) {
            transcript.append(toString(deck.draw()));
        }
        transcript.append("\n");
        snapshot();
    }

    @Override
    public void gameInitiated(Deck deck) {
        transcript.append("Deck: ").append(deck.serialize()).append("\n");
    }

    public void print(){
        System.out.println(transcript);
    }
}
