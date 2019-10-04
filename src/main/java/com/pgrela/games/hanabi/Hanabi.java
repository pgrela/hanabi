package com.pgrela.games.hanabi;

import com.pgrela.games.hanabi.domain.Game;
import com.pgrela.games.hanabi.domain.GameFactory;
import com.pgrela.games.hanabi.domain.player.MiserablePlayer;

public class Hanabi {

  public static void main(String[] args) {
    int c = 0;
    for (int i = 0; i < 100000; i++) {

      Game game = GameFactory.setupNewGame(MiserablePlayer::new);
      game.start();
      if (game.score() > 19) {
        ++c;
      }
      if (game.score() > 20) {
        System.out.println(game.score());
      }
    }
    System.out.println(c);
  }

}
