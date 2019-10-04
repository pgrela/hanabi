package com.pgrela.games.hanabi.domain;

import java.util.List;

public interface MyHand {

  UnknownCard getFirstCard();
  UnknownCard getMostRecentCard();
  List<UnknownCard> getCards();
}
