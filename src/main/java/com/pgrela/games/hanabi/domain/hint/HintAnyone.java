package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.api.internal.HintToMe;
import com.pgrela.games.hanabi.domain.api.internal.HintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.UnknownCard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

abstract class HintAnyone implements HintToMe, HintToOtherPlayer {

  private final OtherPlayer fromPlayer;
  private final OtherPlayer toPlayer;
  private final List<KnownCard> indicatedCards;
  private final List<UnknownCard> unknownIndicatedCards;

  HintAnyone(OtherPlayer fromPlayer, OtherPlayer toPlayer,
             List<KnownCard> indicatedCards) {
    this.fromPlayer = fromPlayer;
    this.toPlayer = toPlayer;
    this.indicatedCards = Collections.unmodifiableList(new ArrayList<>(indicatedCards));
    this.unknownIndicatedCards = Collections
        .unmodifiableList(indicatedCards.stream().map(KnownCard::unknown)
            .collect(Collectors.toList()));
  }

  public OtherPlayer getFromPlayer() {
    return fromPlayer;
  }

  public OtherPlayer getToPlayer() {
    return toPlayer;
  }

  @Override
  public List<UnknownCard> getMyIndicatedCards() {
    return unknownIndicatedCards;
  }

  @Override
  public List<KnownCard> getIndicatedCards() {
    return indicatedCards;
  }
}
