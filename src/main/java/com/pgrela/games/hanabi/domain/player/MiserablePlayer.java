package com.pgrela.games.hanabi.domain.player;

import com.pgrela.games.hanabi.domain.Card;
import com.pgrela.games.hanabi.domain.CardPlayedOutcome;
import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.hint.ColorHintAnyone;
import com.pgrela.games.hanabi.domain.GamePlayer;
import com.pgrela.games.hanabi.domain.KnownCard;
import com.pgrela.games.hanabi.domain.MyHand;
import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.Spectator;
import com.pgrela.games.hanabi.domain.Player;
import com.pgrela.games.hanabi.domain.SomeonesHand;
import com.pgrela.games.hanabi.domain.Table;
import com.pgrela.games.hanabi.domain.Turn;
import com.pgrela.games.hanabi.domain.UnknownCard;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MiserablePlayer implements Spectator, GamePlayer {

  private List<Player> nextPlayers;
  private MyHand myHand;
  private Table table;

  private LinkedList<Turn> goodTurns = new LinkedList<>();
  private LinkedList<UnknownCard> cardsToPlay = new LinkedList<>();

  public MiserablePlayer() {
  }


  public void setup(List<Player> nextPlayers, MyHand myHand, Table table) {
    this.nextPlayers = nextPlayers;
    this.myHand = myHand;
    this.table = table;
  }

  @Override
  public Turn doTheMove() {
    if (goodTurns.isEmpty()) {
      if (!cardsToPlay.isEmpty()) {
        Optional<Turn> first = cardsToPlay.stream()
            .filter(card -> myHand.getCards().contains(card)).map(Turn::play).findFirst();
        if (first.isPresent()) {
          return first.get();
        }

      }
      if (table.areHintTokensAvailable()) {
        for (Player player : nextPlayers) {
          SomeonesHand hand = player.getHand();
          Set<Color> seenColors = new HashSet<>();
          Set<Number> seenNumbers = new HashSet<>();
          for (KnownCard card : hand.getKnownCards()) {
            if (table.getFireworks().canAccept(card)) {
              if (!seenColors.contains(card.getColor())) {
                return Turn.hint(hand, card.getColor());
              }
              if (!seenNumbers.contains(card.getNumber())) {
                return Turn.hint(hand, card.getNumber());
              }
            }
            seenColors.add(card.getColor());
            seenNumbers.add(card.getNumber());
          }
        }
      }
    } else {
      return goodTurns.pollFirst();
    }
    return Turn.discard(myHand.getFirstCard());
  }

  @Override
  public void hintGiven(ColorHintAnyone colorHint) {
    if (colorHint.getToPlayer().equals(this)) {
      myHand.getCards().stream().filter(card -> colorHint.getIndicatedCards().contains(card))
          .limit(1).forEach(cardsToPlay::add);
    }
  }

  @Override
  public void hintGiven(NumberHint numberHint) {
    if (numberHint.getToPlayer().equals(this)) {
      myHand.getCards().stream().filter(card -> numberHint.getIndicatedCards().contains(card))
          .limit(1).forEach(cardsToPlay::add);
    }

  }

  @Override
  public void cardPlayed(Player player, Card card, CardPlayedOutcome outcome) {

  }

  @Override
  public void cardDiscarded(Player player, Card card) {

  }

  @Override
  public void cardDrawn(Player player) {

  }

  @Override
  public void lastCardDrawn(Player player) {

  }
}
