package com.pgrela.games.hanabi.domain.hint;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.ColorHintToMe;
import com.pgrela.games.hanabi.domain.ColorHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.KnownCard;
import com.pgrela.games.hanabi.domain.Player;
import java.util.List;

public class ColorHintAnyone extends HintAnyone implements ColorHintToMe, ColorHintToOtherPlayer {

  private Color color;

  public ColorHintAnyone(Player fromPlayer, Player toPlayer,
      List<KnownCard> indicatedCards, Color color) {
    super(fromPlayer, toPlayer, indicatedCards);
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}
