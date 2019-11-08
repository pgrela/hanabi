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

public class GameFactory {

    public static Game setupNewGame(Supplier<Player> playerProvider, int playersNo) {
        Deck deck = Deck.shuffle(Card.BASIC_DECK);
        int cardsPerPlayer = playersNo>3?4:5;

        LinkedList<ThePlayer> players = new LinkedList<>();
        for (int i = 0; i < playersNo; i++) {
            List<Card> cards = IntStream.range(0, cardsPerPlayer).mapToObj((ignore) -> deck.draw())
                    .collect(Collectors.toList());
            Hand hand = new Hand(cards);
            Player player = playerProvider.get();
            players.add(new ThePlayer(player, hand, "Player " + (i + 1)));
        }
        Printer printer = new Printer();
        List<Spectator> barePlayers = Collections.singletonList(printer);
        Game game = new Game(deck, FireworksImpl.empty(), 8, 0, 3, false, null, false, barePlayers,
                players);
        players.forEach(player -> player.getPlayer().setup(nextPlayers(player, players), player.getHand(), game));
        printer.setGame(game);
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
