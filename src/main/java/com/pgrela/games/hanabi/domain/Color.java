package com.pgrela.games.hanabi.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Color {

  private static Map<String, Color> COLORS = new HashMap<>();
  private String name;
  public static final Color WHITE = new Color("WHITE");
  public static final Color YELLOW = new Color("YELLOW");
  public static final Color GREEN = new Color("GREEN");
  public static final Color RED = new Color("RED");
  public static final Color BLUE = new Color("BLUE");
  public static final Color RAINBOW = new Color("RAINBOW");

  public static final List<Color> BASIC_COLORS = Arrays.asList(WHITE, YELLOW, GREEN, RED, BLUE);

  public Color(String name) {
    COLORS.put(name, this);
    this.name = name;
  }

  public String serialize(){
    return name;
  }
  public static Color deserialize(String color){
    return COLORS.get(color);
  }

  @Override
  public String toString() {
    return "{" +
        "" + name +
        '}';
  }
}
