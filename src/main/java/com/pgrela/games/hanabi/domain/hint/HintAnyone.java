package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.HintToMe;
import com.pgrela.games.hanabi.domain.HintToOtherPlayer;
import com.pgrela.games.hanabi.domain.KnownCard;
import com.pgrela.games.hanabi.domain.Player;
import com.pgrela.games.hanabi.domain.UnknownCard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

abstract class HintAnyone implements HintToMe, HintToOtherPlayer {

  private final Player fromPlayer;
  private final Player toPlayer;
  private final List<KnownCard> indicatedCards;
  private final List<UnknownCard> unknownIndicatedCards;

  HintAnyone(Player fromPlayer, Player toPlayer,
      List<KnownCard> indicatedCards) {
    this.fromPlayer = fromPlayer;
    this.toPlayer = toPlayer;
    this.indicatedCards = Collections.unmodifiableList(new ArrayList<>(indicatedCards));
    this.unknownIndicatedCards = Collections
        .unmodifiableList(indicatedCards.stream().map(KnownCard::unknown)
            .collect(Collectors.toList()));
  }

  public Player getFromPlayer() {
    return fromPlayer;
  }

  public Player getToPlayer() {
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
