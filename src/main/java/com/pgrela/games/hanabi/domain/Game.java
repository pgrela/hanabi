package com.pgrela.games.hanabi.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Game {

  private Deck deck;
  private Fireworks fireworks;
  private int availableHintTokens;
  private int usedHintTokens;
  private int availableBlownTokens;
  private boolean lastRound;
  private GamePlayer lastPlayer;
  private boolean gameFinished;
  private List<? extends PlayAware> playAwares;
  private List<PlayerWithHand> players;
  private Map<GamePlayer, Hand> hands;
  private Map<SomeonesHand, GamePlayer> playersByHands;

  private Queue<Consumer<PlayAware>> events = new LinkedList<>();

  Game(Deck deck, Fireworks fireworks, int availableHintTokens, int usedHintTokens,
      int availableBlownTokens, boolean lastRound, GamePlayer lastPlayer, boolean gameFinished,
      List<? extends PlayAware> playAwares,
      List<PlayerWithHand> players) {
    this.deck = deck;
    this.fireworks = fireworks;
    this.availableHintTokens = availableHintTokens;
    this.usedHintTokens = usedHintTokens;
    this.availableBlownTokens = availableBlownTokens;
    this.lastRound = lastRound;
    this.lastPlayer = lastPlayer;
    this.gameFinished = gameFinished;
    this.playAwares = playAwares;
    this.players = players;

    hands = players.stream()
        .collect(Collectors.toMap(PlayerWithHand::getPlayer, PlayerWithHand::getHand));
    playersByHands = players.stream()
        .collect(Collectors.toMap(PlayerWithHand::getHand, PlayerWithHand::getPlayer));
  }

  public boolean isFinished() {
    return gameFinished || fireworks.areFinished() || areBlownTokensExhausted();
  }

  public void start() {
    int currentPlayer = 0;
    while (!isFinished()) {
      GamePlayer player = players.get(currentPlayer++ % players.size()).getPlayer();
      Turn turn = player.doTheMove();
      turn.execute(this, player);
      turnPlayed(player);
      while (!events.isEmpty()) {
        playAwares.forEach(events.poll());
      }
      //new Printer().print(this);
    }
  }

  void giveHint(Player player, SomeonesHand hand, Color color) {
    Hand theHand = (Hand) hand;
    List<UnknownCard> indicatedCards = theHand.getRealCards().stream()
        .filter(h -> h.getColor().equals(color))
        .collect(Collectors.toList());
    giveHint(new ColorHint(player, playersByHands.get(hand), indicatedCards, color));
  }

  void giveHint(Player player, SomeonesHand hand, Number number) {
    Hand theHand = (Hand) hand;
    List<UnknownCard> indicatedCards = theHand.getRealCards().stream()
        .filter(h -> h.getNumber().equals(number))
        .collect(Collectors.toList());
    giveHint(new NumberHint(player, playersByHands.get(hand), indicatedCards, number));
  }

  void giveHint(ColorHint colorHint) {
    decreaseHintTokens();
    events.add((playAware -> playAware.hintGiven(colorHint)));
  }

  void giveHint(NumberHint numberHint) {
    decreaseHintTokens();
    events.add((playAware -> playAware.hintGiven(numberHint)));
  }

  CardPlayedOutcome playCard(Player player, UnknownCard unknownCard) {
    Card card = (Card) unknownCard;
    hands.get(player).remove(card);
    if (fireworks.canAccept(card)) {
      fireworks.add(card);
      events.add((playAware -> playAware.cardPlayed(player, card, CardPlayedOutcome.SUCCESS)));
      return CardPlayedOutcome.SUCCESS;
    } else {
      blow();
      events.add((playAware -> playAware.cardPlayed(player, card, CardPlayedOutcome.FAIL)));
      return CardPlayedOutcome.FAIL;
    }
  }

  void discardCard(Player player, UnknownCard unknownCard) {
    Card card = (Card) unknownCard;
    if(!hands.get(player).getCards().contains(unknownCard)){
      throw new IllegalGameMoveException("");
    }
    hands.get(player).remove(card);
    increaseHintTokens();
    events.add((playAware -> playAware.cardDiscarded(player, card)));
  }

  Card drawCard(Player player) {
    Card card = deck.draw();
    hands.get(player).add(card);
    events.add((playAware -> playAware.cardDiscarded(player, card)));
    return card;
  }

  void turnPlayed(GamePlayer player) {
    if (lastRound) {
      if (player.equals(lastPlayer)) {
        gameFinished = true;
      }
      return;
    }
    if (isDeckEmpty()) {
      lastRound = true;
      lastPlayer = player;
      events.add((playAware -> playAware.lastCardDrawn(player)));
    }
  }

  public boolean areBlownTokensExhausted() {
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
      events.add(PlayAware::gameFinished);
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

  List<PlayerWithHand> getPlayers() {
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
}
