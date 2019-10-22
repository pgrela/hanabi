package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.UnknownCard;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Turn {

  private BiConsumer<Game, ThePlayer>[] actions;

  public Turn(BiConsumer<Game, ThePlayer>... actions) {
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

  private static void drawCard(Game game, ThePlayer player) {
    if (!game.isDeckEmpty()) {
      game.drawCard(player);
    }
  }

  public static Turn hint(OtherPlayer ohterPlayer, Color color) {
    return new Turn(
        (game, player)->game.giveHint(player, ohterPlayer, color)
    );
  }

  public static Turn hint(OtherPlayer ohterPlayer, Number number) {
    return new Turn(
        (game, player)->game.giveHint(player, ohterPlayer, number)
    );
  }


  public void execute(Game game, ThePlayer player) {
    Stream.of(actions).forEach(action -> action.accept(game, player));
  }

}
