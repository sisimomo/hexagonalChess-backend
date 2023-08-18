package com.github.sisimomo.hexagonalchess.engine.piece;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;

public class King extends Piece {

  private static final List<PossibleMovement> POSSIBLE_MOVES = List.of(
      new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(2), 1),
      new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(1), 1),
      new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), 1),
      new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(5), 1), new PossibleMovement(new Coordinate(-1, 1, 0), 1),
      new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(3), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(1), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(0), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(5), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(4), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(3), 1),
      new PossibleMovement(Coordinate.DIAGONAL_VECTORS.get(2), 1));

  public King(PieceSide side, Coordinate coordinate) {
    super(PieceType.KING, side, coordinate);
  }

  public King(King piece) {
    super(piece);
  }

  @Override
  public List<Coordinate> allPossibleMoves(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    return extractPlayableMoves(POSSIBLE_MOVES, piecesOnGameBoard, lastMove, checkForSelfCheck);
  }

  public boolean isCheck(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove) {
    return piecesOnGameBoard.stream().filter(piece -> !piece.getSide().equals(super.side)).anyMatch(piece -> piece
        .allPossibleMoves(piecesOnGameBoard, lastMove, false).stream().anyMatch(c -> super.coordinate.equals(c)));
  }

  public boolean isCheckMate(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove) {
    if (!isCheck(piecesOnGameBoard, lastMove)) {
      return false;
    }
    return piecesOnGameBoard.stream().filter(piece -> piece.getSide().equals(super.side))
        .noneMatch(piece -> piece.allPossibleMoves(piecesOnGameBoard, lastMove, false).stream().anyMatch(c -> {
          List<Piece> newBoard = clone(piecesOnGameBoard);
          Piece pieceOfNewBoard = newBoard.stream().filter(piece::equals).findAny().orElseThrow();
          pieceOfNewBoard.setCoordinate(c);
          return !((King) Piece.findPiece(newBoard, type, side, null).orElseThrow()).isCheck(newBoard,
              Pair.of(piece.coordinate, c));
        }));
  }

}
