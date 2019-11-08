package com.pgrela.games.hanabi.domain.player;

import com.pgrela.games.hanabi.domain.Color;
import com.pgrela.games.hanabi.domain.Number;
import com.pgrela.games.hanabi.domain.api.KnownCard;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FireworkSimulator {

  List<Number> ORDER = Number.ONE_TO_FIVE;
  Map<Number, Integer> numbers = IntStream.range(0, ORDER.size()).boxed()
      .collect(Collectors.toMap(ORDER::get, i -> i));
  Map<Color, boolean[]> decks = new HashMap<>();

  Deque<List<KnownCard>> nestedSimulation = new ArrayDeque<>();

  public FireworkSimulator() {
    Color.BASIC_COLORS.forEach(c -> decks.put(c, new boolean[10]));
  }

  void simulateAdd(KnownCard card) {
    nestedSimulation.peekLast().add(card);
    add(card);
  }

  void add(KnownCard card) {
    decks.get(card.getColor())[numbers.get(card.getNumber())] = true;
  }

  boolean canAccept(KnownCard card) {
    Integer ordinal = numbers.get(card.getNumber());
    return ordinal == 0 ? !decks.get(card.getColor())[ordinal]
        : decks.get(card.getColor())[ordinal - 1] && !decks.get(card.getColor())[ordinal];
  }

  void remove(KnownCard card) {
    decks.get(card.getColor())[numbers.get(card.getNumber())] = false;
  }

  void startSimulation(){
    nestedSimulation.add(new ArrayList<>());
  }

  void rollback() {
    nestedSimulation.pollLast().forEach(this::remove);
  }

  public void rollbackAll() {
    nestedSimulation.forEach(c->c.forEach(this::remove));
    nestedSimulation.clear();
  }
}
