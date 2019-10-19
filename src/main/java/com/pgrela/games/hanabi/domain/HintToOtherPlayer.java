package com.pgrela.games.hanabi.domain;

import java.util.List;

public interface HintToOtherPlayer extends Hint {
  List<KnownCard> getIndicatedCards();
}
