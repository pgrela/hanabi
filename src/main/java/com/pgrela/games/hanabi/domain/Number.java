package com.pgrela.games.hanabi.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Number implements Comparable<Number> {

  private static final Map<Integer, Number> NUMBERS = new HashMap<>();
  public static final Number ONE = new Number(1);
  public static final Number TWO = new Number(2);
  public static final Number THREE = new Number(3);
  public static final Number FOUR = new Number(4);
  public static final Number FIVE = new Number(5);

  private int number;

  public static final List<Number> ONE_TO_FIVE = Arrays.asList(ONE, TWO, THREE, FOUR, FIVE);
  public static final List<Number> BASIC_SET = Arrays
      .asList(ONE, ONE, ONE, TWO, TWO, THREE, THREE, FOUR, FOUR, FIVE);

  private Number(int number) {
    this.number = number;
    NUMBERS.put(number, this);
  }

  public String serialize(){
    return String.valueOf(number);
  }
  public static Number deserialize(String number){
    return NUMBERS.get(Integer.valueOf(number));
  }
  @Override
  public String toString() {
    return "{" + number + '}';
  }

  @Override
  public int compareTo(Number o) {
    return Integer.compare(number, o.number);
  }
}
