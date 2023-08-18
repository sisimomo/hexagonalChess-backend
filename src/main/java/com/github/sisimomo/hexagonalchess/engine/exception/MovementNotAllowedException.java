package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

public class MovementNotAllowedException extends Exception {

  public MovementNotAllowedException(Piece piece, Coordinate coordinate) {
    super(String.format("The Piece[%s] is not allowed to move to Coordinate[%s].", piece.toString(),
        coordinate.toString()));
  }

}
