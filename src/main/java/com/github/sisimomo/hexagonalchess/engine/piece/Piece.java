package com.github.sisimomo.hexagonalchess.engine.piece;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.github.sisimomo.hexagonalchess.engine.Constant;
import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.Game;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.engine.exception.MultiplePiecesFoundRuntimeException;
import com.github.sisimomo.hexagonalchess.engine.exception.PieceConstructorNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class Piece implements Cloneable {

  @NotNull
  protected final PieceType type;
  @NotNull
  protected PieceSide side;
  @Valid
  @NotNull
  protected Coordinate coordinate;

  protected Piece(Piece piece) {
    this.type = piece.type;
    this.side = piece.side;
    this.coordinate = piece.coordinate;
  }

  /**
   * Creates a new instance of a chess piece based on the given piece type, side, and coordinate.
   *
   * @param pieceType The type of the chess piece that needs to be created.
   * @param side The side/color of the piece.
   * @param coordinate The position of the piece on a chessboard.
   * @return A new {@link Piece} object.
   */
  public static Piece createPiece(PieceType pieceType, PieceSide side, Coordinate coordinate) {
    try {
      return pieceType.getClazz().getConstructor(PieceSide.class, Coordinate.class).newInstance(side, coordinate);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new PieceConstructorNotFoundException(pieceType.getClazz());
    }
  }

  /**
   * Takes a list of {@link Piece} objects and returns a new list containing clones of each object.
   *
   * @param pieces The list of {@link Piece}s.
   * @return A new list of cloned {@link Piece}s.
   */
  public static List<Piece> clone(List<Piece> pieces) {
    return pieces.stream().map(Piece::clone).collect(Collectors.toList());
  }

  /**
   * Finds a {@link Piece} by a specific coordinate, type and/or side.
   *
   * @param pieces A list of {@link Piece} objects.
   * @param type The type of the piece you are looking for.
   * @param side The side of the piece you are looking for.
   * @param coordinate The coordinate of the piece you are looking for.
   * @return An {@link Optional} object that contains a {@link Piece}.
   */
  public static Optional<Piece> findPiece(List<Piece> pieces, PieceType type, PieceSide side, Coordinate coordinate) {
    return findPieces(pieces, type, side).stream()
        .filter(piece -> coordinate == null || piece.getCoordinate().equals(coordinate)).reduce((a, b) -> {
          throw new MultiplePiecesFoundRuntimeException(pieces, type, side, coordinate);
        });
  }

  /**
   * Finds a list of {@link Piece}s by a specific type and/or side.
   *
   * @param pieces A list of {@link Piece} objects.
   * @param type The type of the piece you are looking for.
   * @param side The side of the piece you are looking for.
   * @return A {@link List} of {@link Piece} objects.
   */
  public static List<Piece> findPieces(List<Piece> pieces, PieceType type, PieceSide side) {
    return pieces.stream()
        .filter(
            piece -> (side == null || piece.getSide().equals(side)) && (type == null || piece.getType().equals(type)))
        .toList();
  }

  /**
   * Checks if two lists of {@link Piece}s are equal.
   *
   * @param piecesA A list of {@link Piece} objects.
   * @param piecesB A second list of {@link Piece} objects.
   * @return A boolean value.
   */
  public static boolean equals(List<Piece> piecesA, List<Piece> piecesB) {
    if (piecesA.size() != piecesB.size()) {
      return false;
    }
    Comparator<Coordinate> coordinateComparator =
        Comparator.comparing(Coordinate::getQ).thenComparing(Coordinate::getR).thenComparing(Coordinate::getS);
    Comparator<Piece> pieceComparator = Comparator.comparing(Piece::getCoordinate, coordinateComparator);
    List<Piece> sortedPiecesA = Piece.clone(piecesA).stream().sorted(pieceComparator).toList();
    List<Piece> sortedPiecesB = Piece.clone(piecesB).stream().sorted(pieceComparator).toList();

    for (int i = 0; i < sortedPiecesA.size(); i++) {
      if (!Objects.equals(sortedPiecesA.get(i), sortedPiecesB.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if two {@link Piece} objects are equal.
   *
   * @param piecesA A {@link Piece} object.
   * @param piecesB A second {@link Piece} object.
   * @return A boolean value.
   */
  public static boolean equals(Piece piecesA, Piece piecesB) {
    return Objects.equals(piecesA, piecesB);
  }


  /**
   * The function checks if a given move is valid using {@link #allPossibleMoves}.
   *
   * @param to Represents the coordinate where the player wants to move a piece to on the game board.
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param lastMove Represents the last move made in the game.
   * @return The method is returning a boolean value.
   */
  public boolean isMoveValid(Coordinate to, List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove) {
    return allPossibleMoves(piecesOnGameBoard, lastMove, true).stream().anyMatch(c -> c.equals(to));
  }

  /**
   * Checks all the possible movements the piece can do on a game board.
   *
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param lastMove Represents the last move made in the game.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return A list of {@link Coordinate}s representing possible moves.
   */
  public abstract List<Coordinate> allPossibleMoves(List<Piece> piecesOnGameBoard,
      Pair<Coordinate, Coordinate> lastMove, boolean checkForSelfCheck);

  /**
   * Checks all the possible movements the piece can do on a game board.
   *
   * @param game The {@link Game}.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return A list of {@link Coordinate}s representing possible moves.
   */
  public List<Coordinate> allPossibleMovesFromGame(Game game, boolean checkForSelfCheck) {
    return this.allPossibleMoves(game.getPieces(), game.getLastMove(), checkForSelfCheck);
  }

  /**
   * The function checks if a move causes the player's own king to be in check.
   *
   * @param to The coordinate where the piece is being moved to on the game board.
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param lastMove Represents the last move made in the game.
   * @return {@code true} if the move would cause the player's own king to be in check.
   */
  protected boolean checkIfMoveCauseSelfCheck(Coordinate to, List<Piece> piecesOnGameBoard,
      Pair<Coordinate, Coordinate> lastMove) {
    List<Piece> newBoard = clone(piecesOnGameBoard);
    Piece newPiece = newBoard.stream().filter(this::equals).findAny().orElseThrow();
    Pair<Coordinate, Coordinate> newLastMove = Pair.of(newPiece.getCoordinate(), to);
    newBoard = newBoard.stream().filter(piece -> !piece.getCoordinate().equals(to)).toList();
    newPiece.setCoordinate(to);
    return ((King) newBoard.stream()
        .filter(piece -> piece.getSide().equals(side) && piece.getType().equals(PieceType.KING)).findAny()
        .orElseThrow()).isCheck(newBoard, newLastMove);
  }

  /**
   * Extracts a list of playable moves from a list of possible movements, given the pieces on the game
   * board.
   *
   * @param possibleMovements A list of {@link PossibleMovement} objects, which represent the possible
   *        movements that the piece can make.
   * @param piecesOnGameBoard The list of {@link Piece}s currently on the game board.
   * @param lastMove Represents the last move made in the game.
   * @param checkForSelfCheck If it is set to true, the method should consider the possibility of the
   *        move resulting in a check condition for the player making the move.
   * @return The method is returning a List of {@link Coordinate}.
   */
  protected List<Coordinate> extractPlayableMoves(List<PossibleMovement> possibleMovements,
      List<Piece> piecesOnGameBoard, Pair<Coordinate, Coordinate> lastMove, boolean checkForSelfCheck) {
    return possibleMovements.stream().flatMap(possibleMovement -> {
      List<Coordinate> moves = new ArrayList<>();
      Coordinate vector = reflectIfBlack(possibleMovement.getVector());
      Coordinate c = coordinate;
      for (int i = 0; i < Math.min(possibleMovement.getMaxRange(), (Constant.BOARD_SIDE_LENGTH - 1) * 2); i++) {
        c = c.add(vector);
        if (c.distance(Coordinate.ORIGIN) < Constant.BOARD_SIDE_LENGTH) {
          Coordinate finalC = c;
          Optional<Piece> oPiece =
              piecesOnGameBoard.stream().filter(piece -> piece.getCoordinate().equals(finalC)).findAny();
          if (oPiece.isPresent()) {
            if (!oPiece.get().getSide().equals(side)
                && (!checkForSelfCheck || !checkIfMoveCauseSelfCheck(c, piecesOnGameBoard, lastMove))) {
              moves.add(c);
            }
            break;
          }
          if (!checkForSelfCheck || !checkIfMoveCauseSelfCheck(c, piecesOnGameBoard, lastMove)) {
            moves.add(c);
          }
        }
      }
      return moves.stream();
    }).toList();
  }

  /**
   * Reflects the {@link Coordinate} if the {@link Piece#side piece side} is black.
   *
   * @param coordinate The {@link Coordinate} to reflect.
   * @return The {@link Coordinate} possibly reflected.
   */
  protected Coordinate reflectIfBlack(Coordinate coordinate) {
    return side.equals(PieceSide.WHITE) ? coordinate : coordinate.horizontalReflection();
  }

  /**
   * Reflects the {@link Coordinate}s if the {@link Piece#side piece side} is black.
   *
   * @param coordinates The {@link Coordinate}s to reflect.
   * @return The {@link Coordinate}s possibly reflected.
   */
  protected List<Coordinate> reflectIfBlack(List<Coordinate> coordinates) {
    return coordinates.stream().map(this::reflectIfBlack).toList();
  }

  /**
   * Create a clone of an {@link Piece} by using the specific copy constructor of the Piece.
   *
   * @return A clone of the {@link Piece} object.
   */
  @Override
  public Piece clone() {
    return Piece.createPiece(type, side, coordinate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, side, coordinate);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Piece other = (Piece) obj;
    return Objects.equals(type, other.type) && Objects.equals(side, other.side)
        && Objects.equals(coordinate, other.coordinate);
  }

}
