package com.github.sisimomo.hexagonalchess.engine.exception;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;

public class PieceNotFoundOnGameBoardException extends Exception {

  public PieceNotFoundOnGameBoardException(Coordinate coordinate) {
    super(String.format("No Piece was found at Coordinate[%s].", coordinate));
  }

}
