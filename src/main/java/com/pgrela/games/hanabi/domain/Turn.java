package com.pgrela.games.hanabi.domain;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Turn {

  private BiConsumer<Game, GamePlayer>[] actions;

  public Turn(BiConsumer<Game, Player>... actions) {
    this.actions = actions;
  }

  public static Turn discard(UnknownCard card) {
    return new Turn(
        (game, player) -> game.discardCard(player, card),
        Turn::drawCard
    );
  }

  public static Turn play(UnknownCard card) {
    return new Turn(
        (game, player) -> game.playCard(player, card),
        Turn::drawCard
    );
  }

  private static void drawCard(Game game, Player player) {
    if (!game.isDeckEmpty()) {
      game.drawCard(player);
    }
  }

  public static Turn hint(SomeonesHand hand, Color color) {
    return new Turn(
        (game, player)->game.giveHint(player, hand, color)
    );
  }

  public static Turn hint(SomeonesHand hand, Number number) {
    return new Turn(
        (game, player)->game.giveHint(player, hand, number)
    );
  }


  public void execute(Game game, Player player) {
    Stream.of(actions).forEach(action -> action.accept(game, player));
  }

}
