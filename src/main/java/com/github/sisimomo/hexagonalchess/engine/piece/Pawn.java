package com.github.sisimomo.hexagonalchess.engine.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.github.sisimomo.hexagonalchess.engine.Constant;
import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;

public class Pawn extends Piece {

  private static final List<Coordinate> FORWARD_2_ALLOWED_CELLS = List.of(new Coordinate(-4, 5, -1),
      new Coordinate(-3, 4, -1), new Coordinate(-2, 3, -1), new Coordinate(-1, 2, -1), new Coordinate(0, 1, -1),
      new Coordinate(1, 1, -2), new Coordinate(2, 1, -3), new Coordinate(3, 1, -4), new Coordinate(4, 1, -5));

  public Pawn(PieceSide side, Coordinate coordinate) {
    super(PieceType.PAWN, side, coordinate);
  }

  public Pawn(Pawn piece) {
    super(piece);
  }

  /**
   * Get a list of valid {@link Coordinate}s for promoting a pawn.
   *
   * @param side Represents the side of the chessboard for which we want to get the possible
   *        coordinates to promote a pawn.
   * @return A List of {@link Coordinate} objects.
   */
  public static List<Coordinate> getPossibleCoordinatesToPromote(PieceSide side) {
    List<Coordinate> diagonals = List.of(Coordinate.DIRECTION_VECTORS.get(4), Coordinate.DIRECTION_VECTORS.get(0));
    Coordinate top = new Coordinate(0, (Constant.BOARD_SIDE_LENGTH - 1) * -1, (Constant.BOARD_SIDE_LENGTH - 1));
    List<Coordinate> result = diagonals.stream().flatMap(diagonal -> {
      List<Coordinate> coordinates = new ArrayList<>();
      for (int i = 0; i < Constant.BOARD_SIDE_LENGTH - 1; i++) {
        coordinates.add(coordinates.isEmpty() ? top : coordinates.get(coordinates.size() - 1).add(diagonal));
      }
      return coordinates.stream();
    }).collect(Collectors.toList());
    result.add(top);
    return side.equals(PieceSide.WHITE) ? result : result.stream().map(Coordinate::horizontalReflection).toList();
  }

  @Override
  public List<Coordinate> allPossibleMoves(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    List<Coordinate> allPossibleMovements = new ArrayList<>();
    forward1Move(piecesOnGameBoard, lastMove, checkForSelfCheck).ifPresent(allPossibleMovements::add);
    forward2Move(piecesOnGameBoard, lastMove, checkForSelfCheck).ifPresent(allPossibleMovements::add);
    allPossibleMovements.addAll(captureMoves(piecesOnGameBoard, lastMove, checkForSelfCheck));
    enPassantMove(piecesOnGameBoard, lastMove, checkForSelfCheck).ifPresent(allPossibleMovements::add);
    return allPossibleMovements;
  }

  /**
   * Checks if the pawn can move forward one spaces on a game board.
   *
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return An {@link Optional} object that contains a {@link Coordinate} representing the possible
   *         move.
   */
  private Optional<Coordinate> forward1Move(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    Coordinate forwardVector = reflectIfBlack(Coordinate.DIRECTION_VECTORS.get(2));
    Coordinate forward = super.getCoordinate().add(forwardVector);
    if (piecesOnGameBoard.stream().anyMatch(piece -> piece.getCoordinate().equals(forward))
        || (checkForSelfCheck && checkIfMoveCauseSelfCheck(forward, piecesOnGameBoard, lastMove))) {
      return Optional.empty();
    }
    return Optional.of(forward);
  }

  /**
   * Checks if the Pawn can move forward two spaces on a game board.
   *
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return An {@link Optional} object that contains a {@link Coordinate} representing the possible
   *         move.
   */
  private Optional<Coordinate> forward2Move(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    if (!reflectIfBlack(FORWARD_2_ALLOWED_CELLS).contains(super.coordinate)) {
      return Optional.empty();
    }
    Coordinate forwardVector = reflectIfBlack(Coordinate.DIRECTION_VECTORS.get(2));
    Coordinate forward = super.coordinate.add(forwardVector);
    Coordinate forward2 = super.coordinate.add(forwardVector.multiply(2));
    if (piecesOnGameBoard.stream()
        .anyMatch(piece -> piece.getCoordinate().equals(forward) || piece.getCoordinate().equals(forward2))
        || (checkForSelfCheck && checkIfMoveCauseSelfCheck(forward2, piecesOnGameBoard, lastMove))) {
      return Optional.empty();
    }
    return Optional.of(forward2);
  }

  /**
   * Checks if the Pawn can eat other pieces on a game board.
   *
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return A list of {@link Coordinate}s representing possible moves.
   */
  private List<Coordinate> captureMoves(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    List<Coordinate> diagonalVectors =
        Stream.of(Coordinate.DIRECTION_VECTORS.get(1), Coordinate.DIRECTION_VECTORS.get(3)).map(this::reflectIfBlack)
            .toList();
    return diagonalVectors.stream().map(v -> super.coordinate.add(v))
        .filter(c -> piecesOnGameBoard.stream()
            .anyMatch(piece -> piece.getCoordinate().equals(c) && !piece.getSide().equals(super.side))
            && (!checkForSelfCheck || !checkIfMoveCauseSelfCheck(c, piecesOnGameBoard, lastMove)))
        .toList();
  }

  /**
   * Checks if the Pawn can eat other pieces on a game board using the "en passant" move.
   *
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param lastMove Represents the last move made in the game.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return An {@link Optional} object that contains a {@link Coordinate} representing the possible
   *         move.
   */
  public Optional<Coordinate> enPassantMove(List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove,
      boolean checkForSelfCheck) {
    if (lastMove == null) {
      return Optional.empty();
    }
    Piece lastMovePiece = findPiece(piecesOnGameBoard, null, side.inverse(), lastMove.getRight()).orElse(null);
    if (lastMovePiece == null) {
      return Optional.empty();
    }
    if (!lastMovePiece.type.equals(PieceType.PAWN) || lastMove.getLeft().distance(lastMove.getRight()) != 2) {
      return Optional.empty();
    }
    List<Coordinate> diagonalDownVectors =
        Stream.of(Coordinate.DIRECTION_VECTORS.get(0), new Coordinate(-1, 1, 0)).map(this::reflectIfBlack).toList();
    return diagonalDownVectors.stream()
        .filter(v -> super.coordinate.add(v).equals(lastMove.getRight())
            && (!checkForSelfCheck || !checkIfMoveCauseSelfCheck(super.coordinate.add(v), piecesOnGameBoard, lastMove)))
        .findAny().map(v -> super.coordinate.add(v.horizontalReflection()));
  }

  @Override
  protected boolean checkIfMoveCauseSelfCheck(Coordinate to, List<Piece> piecesOnGameBoard,
      Pair<Coordinate, Coordinate> lastMove) {
    List<Piece> newBoard = clone(piecesOnGameBoard);
    Pawn newPiece = (Pawn) newBoard.stream().filter(this::equals).findAny().orElseThrow();
    Pair<Coordinate, Coordinate> newLastMove = Pair.of(newPiece.getCoordinate(), to);
    Coordinate pieceCoordinateToRemove;
    if (to.equals(newPiece.enPassantMove(piecesOnGameBoard, lastMove, false).orElse(null))) {
      pieceCoordinateToRemove = to.add(this.getSide().equals(PieceSide.WHITE) ? Coordinate.DIRECTION_VECTORS.get(5)
          : Coordinate.DIRECTION_VECTORS.get(2));
    } else {
      pieceCoordinateToRemove = to;
    }
    newBoard = newBoard.stream().filter(piece -> !piece.getCoordinate().equals(pieceCoordinateToRemove)).toList();
    newPiece.setCoordinate(to);
    return ((King) newBoard.stream()
        .filter(piece -> piece.getSide().equals(side) && piece.getType().equals(PieceType.KING)).findAny()
        .orElseThrow()).isCheck(newBoard, newLastMove);
  }

}
