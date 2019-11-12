package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Player;
import com.pgrela.games.hanabi.domain.api.Spectator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameFactory {

    public static Game setupNewGame(Supplier<Player> playerProvider, int playersNo) {
        return setupNewGame(playerProvider, playersNo, Deck.shuffle(Card.BASIC_DECK));
    }
    public static Game setupNewGame(Supplier<Player> playerProvider, int playersNo, Deck deck) {
        return setupNewGame(Stream.generate(playerProvider).limit(playersNo).collect(Collectors.toList()), deck);
    }
    public static Game setupNewGame(List<? extends Player> players, Deck deck) {
        Deck copiedDeck = deck.clone();
        int cardsPerPlayer = players.size()>3?4:5;

        LinkedList<ThePlayer> thePlayers = new LinkedList<>();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            List<Card> cards = IntStream.range(0, cardsPerPlayer).mapToObj((ignore) -> deck.draw())
                    .collect(Collectors.toList());
            Hand hand = new Hand(cards);
            thePlayers.add(new ThePlayer(player, hand, "Player " + i));
        }
        Printer printer = new Printer();
        List<Spectator> spectators = Collections.singletonList(printer);
        Game game = new Game(deck, FireworksImpl.empty(), 8, 0, 3, false, null, false, spectators,
                thePlayers);
        thePlayers.forEach(player -> player.getPlayer().setup(nextPlayers(player, thePlayers), player.getHand(), game));
        printer.setGame(game);
        printer.gameInitiated(copiedDeck);
        return game;
    }

    private static List<OtherPlayer> nextPlayers(ThePlayer player, LinkedList<ThePlayer> players) {
        List<OtherPlayer> otherPlayers = new ArrayList<>();
        int i = 0;
        while (!players.get(i).equals(player)) ++i;
        ++i;
        for (int j = i; j < players.size(); j++) {
            otherPlayers.add(players.get(j));
        }
        --i;
        for (int j = 0; j < i; j++) {
            otherPlayers.add(players.get(j));
        }
        return otherPlayers;
    }

}
