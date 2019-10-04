package com.pgrela.games.hanabi.domain;

public class PlayerWithHand {
  private GamePlayer player;
  private Hand hand;

  public PlayerWithHand(GamePlayer player, Hand hand) {
    this.player = player;
    this.hand = hand;
  }

  public GamePlayer getPlayer() {
    return player;
  }

  public Hand getHand() {
    return hand;
  }
}
