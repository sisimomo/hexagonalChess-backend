package com.github.sisimomo.hexagonalchess.engine.piece;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;

public class Queen extends Piece {

  private static final List<PossibleMovement> POSSIBLE_MOVES =
      List.of(new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(2), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(1), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(5), Integer.MAX_VALUE),
          new PossibleMovement(new Coordinate(-1, 1, 0), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(3), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(1), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(0), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(5), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(4), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(3), Integer.MAX_VALUE),
          new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(2), Integer.MAX_VALUE));

  public Queen(PieceSide side, Coordinate coordinate) {
    super(PieceType.QUEEN, side, coordinate);
  }

  public Queen(Queen piece) {
    super(piece);
  }

  @Override
  public List<Coordinate> allPossibleMoves(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    return extractPlayableMoves(POSSIBLE_MOVES, piecesOnGameBoard, lastMove, checkForSelfCheck);
  }

}
