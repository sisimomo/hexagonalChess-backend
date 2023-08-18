package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.enumeration.GameState;

public class GameOverException extends Exception {

  public GameOverException(GameState gameState) {
    super(String.format("The game is over [%s].", gameState));
  }

}
