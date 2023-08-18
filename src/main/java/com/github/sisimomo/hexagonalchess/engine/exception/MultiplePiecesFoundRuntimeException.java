package com.github.sisimomo.hexagonalchess.engine.exception;

import java.util.List;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

public class MultiplePiecesFoundRuntimeException extends RuntimeException {

  public MultiplePiecesFoundRuntimeException(List<Piece> pieces, PieceType type, PieceSide side,
      Coordinate coordinate) {
    super(String.format("Multiple pieces was found with search parameters [type: %s, side: %s, coordinate: %s]%n%s",
        type, side, coordinate,
        pieces.stream()
            .filter(piece -> (side == null || piece.getSide().equals(side))
                && (type == null || piece.getType().equals(type))
                && (coordinate == null || piece.getCoordinate().equals(coordinate)))
            .toList()));
  }

}
