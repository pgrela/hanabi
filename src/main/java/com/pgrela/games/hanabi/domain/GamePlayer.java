package com.pgrela.games.hanabi.domain;

import java.util.List;

public interface GamePlayer extends Player, PlayAware {

  void setup(List<SomeonesHand> nextPlayers, MyHand myHand, Game game);

  Turn doTheMove();
}
