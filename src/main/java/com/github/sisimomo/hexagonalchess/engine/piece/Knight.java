package com.github.sisimomo.hexagonalchess.engine.piece;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;

public class Knight extends Piece {

  private static final List<PossibleMovement> POSSIBLE_MOVES =
      List.of(new PossibleMovement(new Coordinate(3, -2, -1), 1), new PossibleMovement(new Coordinate(3, -1, -2), 1),
          new PossibleMovement(new Coordinate(2, 1, -3), 1), new PossibleMovement(new Coordinate(1, 2, -3), 1),
          new PossibleMovement(new Coordinate(-2, 3, -1), 1), new PossibleMovement(new Coordinate(-1, 3, -2), 1),
          new PossibleMovement(new Coordinate(-3, 1, 2), 1), new PossibleMovement(new Coordinate(-3, 2, 1), 1),
          new PossibleMovement(new Coordinate(-1, -2, 3), 1), new PossibleMovement(new Coordinate(-2, -1, 3), 1),
          new PossibleMovement(new Coordinate(1, -3, 2), 1), new PossibleMovement(new Coordinate(2, -3, 1), 1));

  public Knight(PieceSide side, Coordinate coordinate) {
    super(PieceType.KNIGHT, side, coordinate);
  }

  public Knight(Knight piece) {
    super(piece);
  }

  @Override
  public List<Coordinate> allPossibleMoves(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    return extractPlayableMoves(POSSIBLE_MOVES, piecesOnGameBoard, lastMove, checkForSelfCheck);
  }

}
