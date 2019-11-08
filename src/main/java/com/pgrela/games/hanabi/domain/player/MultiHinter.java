package com.pgrela.games.hanabi.domain.player;

import com.pgrela.games.hanabi.domain.Card;
import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.Turn;
import com.pgrela.games.hanabi.domain.api.CardPlayedOutcome;
import com.pgrela.games.hanabi.domain.api.CardValue;
import com.pgrela.games.hanabi.domain.api.ColorHintToMe;
import com.pgrela.games.hanabi.domain.api.ColorHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import com.pgrela.games.hanabi.domain.api.MyHand;
import com.pgrela.games.hanabi.domain.api.NumberHintToMe;
import com.pgrela.games.hanabi.domain.api.NumberHintToOtherPlayer;
import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Player;
import com.pgrela.games.hanabi.domain.api.SomeonesHand;
import com.pgrela.games.hanabi.domain.api.Table;
import com.pgrela.games.hanabi.domain.api.UnknownCard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MultiHinter implements Player {

  private List<OtherPlayer> nextPlayers;
  private MyHand myHand;
  private Table table;

  private LinkedList<UnknownCard> cardsToPlay = new LinkedList<>();

  private Set<KnownCard> discardedCards = new HashSet<>();
  private Set<CardValue> hintedCards = new HashSet<>();
  private Map<OtherPlayer, LinkedList<KnownCard>> cardsToBePlayed;
  private FireworkSimulator simulator = new FireworkSimulator();

  public MultiHinter() {
  }


  public void setup(List<OtherPlayer> nextPlayers, MyHand myHand, Table table) {
    this.nextPlayers = nextPlayers;
    this.myHand = myHand;
    this.table = table;
  }

  @Override
  public Turn doTheMove() {
    if (!cardsToPlay.isEmpty()) {
      Optional<Turn> first = cardsToPlay.stream()
          .filter(card -> myHand.getCards().contains(card)).map(Turn::play).findFirst();
      if (first.isPresent()) {
        return first.get();
      }

    }
    if (table.areHintTokensAvailable()) {
      for (OtherPlayer player : nextPlayers) {
        SomeonesHand hand = player.getHand();
        Map<Turn, Integer> turns = new HashMap<>();
        turns.putAll(simulateHints(hand, Number.ONE_TO_FIVE, KnownCard::getNumber,
            number -> Turn.hint(player, number)));
        turns.putAll(simulateHints(hand, Color.BASIC_COLORS, KnownCard::getColor,
            color -> Turn.hint(player, color)));
        if(!turns.isEmpty()){
          return Collections.max(turns.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        }
      }
    }
    return Turn.discard(myHand.getCards().get(myHand.getCards().size() - 1));
  }

  private <T> Map<Turn, Integer> simulateHints(SomeonesHand hand, Iterable<T> items,
      Function<KnownCard, T> accessor, Function<T, Turn> turn) {
    Map<Turn, Integer> turns = new HashMap<>();
    outerLoop:
    for (T number : items) {
      List<KnownCard> cardsToBeHinted = new ArrayList<>();
      simulator.startSimulation();
      for (KnownCard card : hand.getKnownCards()) {
        if (accessor.apply(card).equals(number)) {
          if (simulator.canAccept(card) && !hintedCards.contains(card.value())) {
            simulator.simulateAdd(card);
            cardsToBeHinted.add(card);
          } else {
            simulator.rollback();
            continue outerLoop;
          }
        }
      }
      simulator.rollback();
      if (!cardsToBeHinted.isEmpty()) {
        turns.put(turn.apply(number), cardsToBeHinted.size());
      }
    }
    return turns;
  }

  @Override
  public void receiveHint(ColorHintToMe colorHint) {
    cardsToPlay.addAll(colorHint.getMyIndicatedCards());
  }

  @Override
  public void receiveHint(NumberHintToMe numberHint) {
    cardsToPlay.addAll(numberHint.getMyIndicatedCards());
  }

  @Override
  public void hintGiven(ColorHintToOtherPlayer colorHint) {
    colorHint.getIndicatedCards().stream().map(KnownCard::value).forEach(hintedCards::add);
  }

  @Override
  public void hintGiven(NumberHintToOtherPlayer numberHint) {
    numberHint.getIndicatedCards().stream().map(KnownCard::value).forEach(hintedCards::add);
  }

  @Override
  public void cardPlayed(OtherPlayer player, KnownCard card, CardPlayedOutcome outcome) {
    if (outcome.equals(CardPlayedOutcome.SUCCESS)) {
      simulator.add(card);
    }
  }

  @Override
  public void cardDiscarded(OtherPlayer player, KnownCard card) {
    discardedCards.add(card);
  }

  @Override
  public void theLastCardDrawn(OtherPlayer player) {

  }

  @Override
  public int acceptDrawnCard(UnknownCard card) {
    return 0;
  }
}
