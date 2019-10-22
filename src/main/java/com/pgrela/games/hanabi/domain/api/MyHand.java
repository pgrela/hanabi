package com.pgrela.games.hanabi.domain.api;

import com.pgrela.games.hanabi.domain.api.UnknownCard;

import java.util.List;

public interface MyHand {
  List<UnknownCard> getCards();
}
