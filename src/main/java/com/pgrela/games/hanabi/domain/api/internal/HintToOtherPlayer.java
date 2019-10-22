package com.pgrela.games.hanabi.domain.api.internal;

import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;

import java.util.List;

public interface HintToOtherPlayer extends Hint {
  List<KnownCard> getIndicatedCards();
  OtherPlayer getToPlayer();
}
