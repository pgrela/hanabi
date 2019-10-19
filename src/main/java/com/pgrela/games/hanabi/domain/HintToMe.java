package com.pgrela.games.hanabi.domain;

import java.util.List;

interface HintToMe extends Hint {
  List<UnknownCard> getMyIndicatedCards();
}
