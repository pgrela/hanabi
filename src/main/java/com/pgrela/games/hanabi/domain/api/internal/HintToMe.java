package com.pgrela.games.hanabi.domain.api.internal;

import com.pgrela.games.hanabi.domain.api.UnknownCard;

import java.util.List;

public interface HintToMe extends Hint {
  List<UnknownCard> getMyIndicatedCards();
}
