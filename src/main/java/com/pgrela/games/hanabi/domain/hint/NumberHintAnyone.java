package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.api.NumberHintToMe;
import com.pgrela.games.hanabi.domain.api.NumberHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import java.util.List;

public class NumberHintAnyone extends HintAnyone implements NumberHintToMe, NumberHintToOtherPlayer {

  private Number number;

  public NumberHintAnyone(OtherPlayer fromPlayer, OtherPlayer toPlayer,
                          List<KnownCard> indicatedCards, Number number) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.number = number;
  }

  public Number getNumber() {
    return number;
  }
}
