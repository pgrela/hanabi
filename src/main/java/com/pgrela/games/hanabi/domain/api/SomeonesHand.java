package com.pgrela.games.hanabi.domain.api;

import java.util.List;

public interface SomeonesHand {
  List<KnownCard> getKnownCards();
  KnownCard mostRightHandCard();
  KnownCard mostLeftHandCard();
  int size();
}
