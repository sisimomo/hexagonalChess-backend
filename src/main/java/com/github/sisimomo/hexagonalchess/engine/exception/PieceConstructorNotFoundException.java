package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

public class PieceConstructorNotFoundException extends RuntimeException {

  public PieceConstructorNotFoundException(Class<? extends Piece> clazz) {
    super(String.format("No constructor for Piece[Class: %s] was found matching provided initargs.", clazz));
  }

}
