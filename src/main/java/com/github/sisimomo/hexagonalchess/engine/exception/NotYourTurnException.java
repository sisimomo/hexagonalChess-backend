package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;

public class NotYourTurnException extends Exception {

  public NotYourTurnException(PieceSide side) {
    super(String.format("It's not the %s side turn", side.name().toLowerCase()));
  }

}
