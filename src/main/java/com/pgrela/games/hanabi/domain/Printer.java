package com.pgrela.games.hanabi.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Printer {

  private static Map<Color, String> colored = new HashMap();

  {
    colored.put(Color.RED, "\u001B[30m\u001B[41m");
    colored.put(Color.BLUE, "\u001B[30m\u001B[44m");
    colored.put(Color.YELLOW, "\u001B[30m\u001B[43m");
    colored.put(Color.GREEN, "\u001B[30m\u001B[42m");
    colored.put(Color.WHITE, "\u001B[30m\u001B[47m");
  }

  private static Map<Number, String> numbered = new HashMap();

  {
    numbered.put(Number.ONE, " 1 ");
    numbered.put(Number.TWO, " 2 ");
    numbered.put(Number.THREE, " 3 ");
    numbered.put(Number.FOUR, " 4 ");
    numbered.put(Number.FIVE, " 5 ");
  }

  public static final String DEFAULT = "\u001B[0m";

  public void print(Game game) {
    for (PlayerWithHand player : game.getPlayers()) {
      System.out.print("Player: ");
      player.getHand().getRealCards().forEach(card -> System.out.print(toString(card) + " "));
      System.out.println();
    }
    game.getFireworks().topCards().forEach(card -> System.out.print(toString(card)+" "));
    System.out.print(String.format(" %s %d %s ", "\u001B[30m\u001B[45m", game.remainingDeckSize(), DEFAULT));
    IntStream.range(0,game.getAvailableHintTokens()).mapToObj(i->colored.get(Color.BLUE)+i+DEFAULT+" ").forEach(
        System.out::print);
    IntStream.range(0,game.getAvailableBlownTokens()).mapToObj(i->colored.get(Color.RED)+i+DEFAULT+" ").forEach(
        System.out::print);
    System.out.println();
    System.out.println();
  }

  private String toString(Card card) {
    if(card==null)return " 0 ";
    return colored.get(card.getColor()) + numbered.get(card.getNumber()) + DEFAULT;
  }
}
