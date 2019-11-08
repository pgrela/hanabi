package com.pgrela.games.hanabi.domain;

import java.util.Arrays;
import java.util.List;

public class Color {

  private String name;
  public static final Color WHITE = new Color("WHITE");
  public static final Color YELLOW = new Color("YELLOW");
  public static final Color GREEN = new Color("GREEN");
  public static final Color RED = new Color("RED");
  public static final Color BLUE = new Color("BLUE");
  public static final Color RAINBOW = new Color("RAINBOW");

  public static final List<Color> BASIC_COLORS = Arrays.asList(WHITE, YELLOW, GREEN, RED, BLUE);

  public Color(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "{" +
        "" + name +
        '}';
  }
}
