package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

public class PromotionNotAllowedException extends Exception {

  public PromotionNotAllowedException(Piece piece, Coordinate coordinate) {
    super(String.format("The Piece[%s] is not allowed to be promoted at Coordinate[%s].", piece.toString(),
        coordinate.toString()));
  }

}
