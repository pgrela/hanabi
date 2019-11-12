package com.pgrela.games.hanabi.domain.api;

import java.util.List;

public interface MyHand {
  List<UnknownCard> getCards();

    int size();
}
