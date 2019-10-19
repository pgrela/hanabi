package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.NumberHintToMe;
import com.pgrela.games.hanabi.domain.NumberHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.KnownCard;
import com.pgrela.games.hanabi.domain.Player;
import java.util.List;

public class NumberHintAnyone extends HintAnyone implements NumberHintToMe, NumberHintToOtherPlayer {

  private Number number;

  public NumberHintAnyone(Player fromPlayer, Player toPlayer,
      List<KnownCard> indicatedCards, Number number) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.number = number;
  }

  public Number getNumber() {
    return number;
  }
}
