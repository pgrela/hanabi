package com.pgrela.games.hanabi.domain;

public class IllegalGameMoveException extends RuntimeException {

  public IllegalGameMoveException(String message) {
    super(message);
  }
}
