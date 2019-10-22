package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.api.ColorHintToMe;
import com.pgrela.games.hanabi.domain.api.ColorHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import java.util.List;

public class ColorHintAnyone extends HintAnyone implements ColorHintToMe, ColorHintToOtherPlayer {

  private Color color;

  public ColorHintAnyone(OtherPlayer fromPlayer, OtherPlayer toPlayer,
                         List<KnownCard> indicatedCards, Color color) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}
