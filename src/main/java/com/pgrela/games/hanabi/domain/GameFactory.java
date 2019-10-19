package com.pgrela.games.hanabi.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameFactory {

  public static Game setupNewGame(Supplier<GamePlayer> playerProvider) {
    Deck deck = Deck.shuffle(Card.BASIC_DECK);

    LinkedList<PlayerWithHand> players = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      List<Card> cards = IntStream.range(0, 4).mapToObj((ignore) -> deck.draw())
          .collect(Collectors.toList());
      Hand hand = new Hand(cards);
      GamePlayer player = playerProvider.get();
      players.add(new PlayerWithHand(player, hand));
    }
    List<Spectator> barePlayers = players.stream().map(PlayerWithHand::getPlayer)
        .collect(Collectors.toList());
    Game game = new Game(deck, Fireworks.empty(), 8, 0, 3, false, null, false, barePlayers,
        players);
    players.forEach(player->player.getPlayer().setup(nextPlayers(player, players), player.getHand(), game));
    return game;
  }

  private static List<Player> nextPlayers(PlayerWithHand player, LinkedList<PlayerWithHand> players) {
    List<Player> hands = new ArrayList<>();
    int i=0;
    while(!players.get(i).equals(player))++i;
    ++i;
    for (int j = i; j < players.size(); j++) {
      Hand hand = players.get(j).getHand();
      hands.add(()-> hand);
    }
    for (int j = 0; j < i; j++) {
      Hand hand = players.get(j).getHand();
      hands.add(()-> hand);
    }
    return hands;
  }

}
