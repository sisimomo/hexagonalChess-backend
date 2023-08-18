package com.github.sisimomo.hexagonalchess.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

import com.github.sisimomo.hexagonalchess.engine.enumeration.GameState;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.engine.exception.GameOverException;
import com.github.sisimomo.hexagonalchess.engine.exception.MovementNotAllowedException;
import com.github.sisimomo.hexagonalchess.engine.exception.NotYourTurnException;
import com.github.sisimomo.hexagonalchess.engine.exception.PieceNotFoundOnGameBoardException;
import com.github.sisimomo.hexagonalchess.engine.exception.PromotionNotAllowedException;
import com.github.sisimomo.hexagonalchess.engine.exception.WantedPromotionPieceTypeNotProvidedException;
import com.github.sisimomo.hexagonalchess.engine.piece.Bishop;
import com.github.sisimomo.hexagonalchess.engine.piece.King;
import com.github.sisimomo.hexagonalchess.engine.piece.Knight;
import com.github.sisimomo.hexagonalchess.engine.piece.Pawn;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;
import com.github.sisimomo.hexagonalchess.engine.piece.Queen;
import com.github.sisimomo.hexagonalchess.engine.piece.Rook;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor(onConstructor_ = @Default)
@Getter
@Slf4j
public class Game {

  private static final int BOARD_TO_STRING_PIECE_SPACING = 8;
  private static final String BOARD_TO_STRING_EMPTY_CELL_PLACE_HOLDER = "*";

  @NotNull
  private final List<@NotEmpty List<@NotNull @Valid Piece>> history;

  @NotNull
  private GameState state;

  @NotNull
  private PieceSide sideTurn;

  private Pair<@Valid Coordinate, @Valid Coordinate> lastMove;

  @NotEmpty
  private List<@NotNull @Valid Piece> pieces;

  public Game() {
    this.state = GameState.IN_PROGRESS;
    this.sideTurn = PieceSide.WHITE;
    this.pieces = Game.getInitialPiecesPositions();
    this.history = new ArrayList<>();
  }

  public Game(Game game) {
    this(game.state, game.sideTurn, game.lastMove, game.pieces,
        game.getHistory() != null ? game.getHistory() : new ArrayList<>());
  }

  public Game(@NotNull GameState state, @NotNull PieceSide sideTurn, Pair<Coordinate, Coordinate> lastMove,
      @NotEmpty List<@NotNull @Valid Piece> pieces, @NotNull List<@NotEmpty List<@NotNull @Valid Piece>> history) {
    this.state = state;
    this.sideTurn = sideTurn;
    this.lastMove = lastMove;
    this.pieces = Piece.clone(pieces);
    this.history = history.stream().map(Piece::clone).collect(Collectors.toList());
  }

  /**
   * Get the list of initial positions for both white and black chess pieces.
   *
   * @return A list of {@link Piece} objects.
   */
  private static List<Piece> getInitialPiecesPositions() {
    List<Piece> whites = List.of(
        // Pawn
        new Pawn(PieceSide.WHITE, new Coordinate(-4, 5, -1)), new Pawn(PieceSide.WHITE, new Coordinate(-3, 4, -1)),
        new Pawn(PieceSide.WHITE, new Coordinate(-2, 3, -1)), new Pawn(PieceSide.WHITE, new Coordinate(-1, 2, -1)),
        new Pawn(PieceSide.WHITE, new Coordinate(0, 1, -1)), new Pawn(PieceSide.WHITE, new Coordinate(1, 1, -2)),
        new Pawn(PieceSide.WHITE, new Coordinate(2, 1, -3)), new Pawn(PieceSide.WHITE, new Coordinate(3, 1, -4)),
        new Pawn(PieceSide.WHITE, new Coordinate(4, 1, -5)),
        // Bishop
        new Bishop(PieceSide.WHITE, new Coordinate(0, 5, -5)), new Bishop(PieceSide.WHITE, new Coordinate(0, 4, -4)),
        new Bishop(PieceSide.WHITE, new Coordinate(0, 3, -3)),
        // Knight
        new Knight(PieceSide.WHITE, new Coordinate(-2, 5, -3)), new Knight(PieceSide.WHITE, new Coordinate(2, 3, -5)),
        // Rook
        new Rook(PieceSide.WHITE, new Coordinate(-3, 5, -2)), new Rook(PieceSide.WHITE, new Coordinate(3, 2, -5)),
        // King
        new King(PieceSide.WHITE, new Coordinate(-1, 5, -4)),
        // Queen
        new Queen(PieceSide.WHITE, new Coordinate(1, 4, -5)));
    List<Piece> blacks = Piece.clone(whites);
    blacks.forEach(piece -> {
      piece.setSide(PieceSide.BLACK);
      piece.setCoordinate(piece.getCoordinate().horizontalReflection());
    });
    return Stream.concat(whites.stream(), blacks.stream()).collect(Collectors.toList());
  }

  public @NotEmpty List<@NotNull @Valid Piece> getPieces() {
    return Piece.clone(this.pieces);
  }

  public @NotNull List<@NotEmpty List<@NotNull @Valid Piece>> getHistory() {
    return this.history.stream().map(Piece::clone).collect(Collectors.toList());
  }

  /**
   * Moves a piece from one coordinate to another, performing necessary checks and updates.
   *
   * @param from The current {@link Coordinate} of the {@link Piece} that needs to be moved.
   * @param to The destination {@link Coordinate} where the {@link Piece} is being moved to.
   * @param wantedPromotionPieceType The type of piece that the pawn wants to be promoted to.
   */
  public void movePiece(@Valid Coordinate from, @Valid Coordinate to, PieceType wantedPromotionPieceType)
      throws PromotionNotAllowedException, WantedPromotionPieceTypeNotProvidedException, MovementNotAllowedException,
      NotYourTurnException, PieceNotFoundOnGameBoardException, GameOverException {
    if (state.isEnded()) {
      throw new GameOverException(state);
    }
    Piece piece =
        Piece.findPiece(pieces, null, null, from).orElseThrow(() -> new PieceNotFoundOnGameBoardException(from));
    if (!piece.getSide().equals(sideTurn)) {
      throw new NotYourTurnException(piece.getSide());
    }
    if (!piece.isMoveValid(to, pieces, lastMove)) {
      throw new MovementNotAllowedException(piece, to);
    }
    if (piece instanceof Pawn pawn && Pawn.getPossibleCoordinatesToPromote(piece.getSide()).contains(to)) {
      piece = promotesPawn(pawn, wantedPromotionPieceType);
    } else if (wantedPromotionPieceType != null) {
      throw new PromotionNotAllowedException(piece, to);
    } else {
      addCurrentPiecesToHistory();
    }
    removeCapturedPiece(piece, to);
    piece.setCoordinate(to);
    lastMove = Pair.of(from, to);
    updateGameState();
    sideTurn = sideTurn.inverse();
  }

  /**
   * Promotes a pawn to a desired piece type and returns the new piece.
   *
   * @param pawn Represents the pawn piece that needs to be promoted.
   * @param wantedPromotionPieceType The type of piece that the pawn wants to be promoted to.
   * @return The newly created {@link Piece}.
   */
  private Piece promotesPawn(Pawn pawn, PieceType wantedPromotionPieceType)
      throws WantedPromotionPieceTypeNotProvidedException {
    if (wantedPromotionPieceType == null) {
      throw new WantedPromotionPieceTypeNotProvidedException();
    }
    addCurrentPiecesToHistory();
    pieces.remove(pawn);
    Piece newPiece = Piece.createPiece(wantedPromotionPieceType, pawn.getSide(), pawn.getCoordinate());
    pieces.add(newPiece);
    return newPiece;
  }

  /**
   * Set and determines the current state of the game.
   */
  private void updateGameState() {
    King king = (King) Piece.findPiece(pieces, PieceType.KING, sideTurn.inverse(), null).orElseThrow();
    if (king.isCheck(pieces, null)) {
      updateGameStateCheck();
    } else if (isDrawStalemate()) {
      state = GameState.DRAW_STALEMATE;
    } else if (isDrawInsufficientMaterial()) {
      state = GameState.DRAW_INSUFFICIENT_MATERIAL;
    } else if (isDrawThreefoldRepetition()) {
      state = GameState.DRAW_THREEFOLD_REPETITION;
    } else {
      state = GameState.IN_PROGRESS;
    }
  }

  /**
   * Set and determines the game state to indicate whether the white or black side has won, or if
   * either side is in check.
   */
  private void updateGameStateCheck() {
    King king = (King) Piece.findPiece(pieces, PieceType.KING, sideTurn.inverse(), null).orElseThrow();
    if (king.isCheckMate(pieces, null)) {
      state = king.getSide().equals(PieceSide.WHITE) ? GameState.BLACK_WON : GameState.WHITE_WON;
    } else {
      state = king.getSide().equals(PieceSide.WHITE) ? GameState.WHITE_IN_CHECK : GameState.BLACK_IN_CHECK;
    }
  }

  /**
   * Checks if there is a stalemate in the game.
   *
   * @return A boolean value.
   */
  private boolean isDrawStalemate() {
    return Piece.findPieces(pieces, null, sideTurn.inverse()).stream()
        .allMatch(piece -> piece.allPossibleMoves(pieces, lastMove, true).isEmpty());
  }

  /**
   * Checks if there is insufficient material in the game.
   *
   * @return A boolean value.
   */
  private boolean isDrawInsufficientMaterial() {
    return isDrawInsufficientMaterialSide(PieceSide.WHITE) && isDrawInsufficientMaterialSide(PieceSide.BLACK);
  }

  /**
   * Checks if a given side is insufficient material in the game.
   *
   * @param side Represents the side of the chessboard for which we want to check if there is
   *        insufficient material.
   * @return A boolean value.
   */
  private boolean isDrawInsufficientMaterialSide(PieceSide side) {
    List<Piece> sidePieces = Piece.findPieces(pieces, null, side);
    return sidePieces.size() <= 2 && sidePieces.stream().noneMatch(piece -> piece.getType().equals(PieceType.PAWN))
        && sidePieces.stream()
            .filter(piece -> List.of(PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT).contains(piece.getType()))
            .count() == sidePieces.size();
  }

  /**
   * Checks if there is a threefold repetition in a game of chess.
   *
   * @return A boolean value.
   */
  private boolean isDrawThreefoldRepetition() {
    if (history.size() > 3) {
      int count = 0;
      for (int i = history.size() - 1; i > 0; i--) {
        if (Piece.equals(history.get(history.size() - 1), history.get(i))) {
          count++;
        }
      }
      return count >= 3;
    }
    return false;
  }

  /**
   * Adds a clone of the current pieces to the history list.
   */
  private void addCurrentPiecesToHistory() {
    history.add(Piece.clone(pieces));
  }

  /**
   * Removes the captured piece from the game based on the type of piece and the move made.
   *
   * @param piece Represents the chess piece that is being moved.
   * @param to Represents the coordinate where the piece is being moved to.
   */
  private void removeCapturedPiece(@Valid Piece piece, @Valid Coordinate to) {
    Coordinate pieceCoordinateToRemove;
    if (piece.getType().equals(PieceType.PAWN) // En Passant
        && to.equals(((Pawn) piece).enPassantMove(pieces, lastMove, true).orElse(null))) {
      pieceCoordinateToRemove = to.add(piece.getSide().equals(PieceSide.WHITE) ? Coordinate.DIRECTION_VECTORS.get(5)
          : Coordinate.DIRECTION_VECTORS.get(2));
    } else {
      pieceCoordinateToRemove = to;
    }
    pieces = pieces.stream().filter(p -> !p.getCoordinate().equals(pieceCoordinateToRemove)).toList();
  }

  /**
   * Generates a string representation of the game object, including its state, side turn, last move,
   * pieces, and the game board.
   *
   * @return A {@link String}.
   */
  public String toString() {
    return "Game(state=" + this.getState() + ", sideTurn=" + this.getSideTurn() + ", lastMove=" + this.getLastMove()
        + ", pieces=" + this.getPieces() + ")" + System.lineSeparator() + System.lineSeparator() + gameBoardToString();
  }

  /**
   * Generates a string representation of a game board.
   *
   * @return A string representation of the game board.
   */
  public String gameBoardToString() {
    StringBuilder str = new StringBuilder();

    Coordinate lastRowStartingCoordinate =
        new Coordinate(1, Constant.BOARD_SIDE_LENGTH * -1, Constant.BOARD_SIDE_LENGTH - 1);
    // Top
    int piecesInRow = 1;
    for (; piecesInRow < Constant.BOARD_SIDE_LENGTH + 1; piecesInRow++) {
      str.append(rowToString(piecesInRow, lastRowStartingCoordinate, 1));
      lastRowStartingCoordinate = lastRowStartingCoordinate.add(getGrowingDirectionVector(1));
    }
    // Middle
    piecesInRow = Constant.BOARD_SIDE_LENGTH;
    for (int row = 1; row < Constant.BOARD_SIDE_LENGTH * 2 - 2; row++) {
      int growingDirection = row % 2 == 0 ? 1 : -1;
      piecesInRow += growingDirection;
      str.append(rowToString(piecesInRow, lastRowStartingCoordinate, growingDirection));
      lastRowStartingCoordinate = lastRowStartingCoordinate.add(getGrowingDirectionVector(growingDirection));
    }
    // Bottom
    for (piecesInRow = Constant.BOARD_SIDE_LENGTH; piecesInRow > 0; piecesInRow--) {
      int growingDirection = piecesInRow == Constant.BOARD_SIDE_LENGTH ? 1 : -1;
      str.append(rowToString(piecesInRow, lastRowStartingCoordinate, growingDirection));
      lastRowStartingCoordinate = lastRowStartingCoordinate.add(getGrowingDirectionVector(growingDirection));
    }

    return str.toString();
  }

  private @Valid Coordinate getGrowingDirectionVector(int growingDirection) {
    return growingDirection == 1 ? new Coordinate(-1, 1, 0) : Coordinate.DIRECTION_VECTORS.get(0);
  }

  /**
   * Generates a string representation of a row of the game board.
   *
   * @param piecesInRow Represents the number of pieces in the row.
   * @param lastRowStartingCoordinate Represents the starting coordinate of the last row.
   * @param growingDirection The `growingDirection` parameter represents the direction in which the
   *        row is growing.
   * @return A string representation of a row of the game board.
   */
  private String rowToString(int piecesInRow, @Valid Coordinate lastRowStartingCoordinate, int growingDirection) {
    List<Piece> row = new ArrayList<>();
    lastRowStartingCoordinate = lastRowStartingCoordinate.add(getGrowingDirectionVector(growingDirection));
    for (int i = 0; i < piecesInRow; i++) {
      Coordinate finalCurrentCoordinate = lastRowStartingCoordinate;
      row.add(
          pieces.stream().filter(piece -> piece.getCoordinate().equals(finalCurrentCoordinate)).findAny().orElse(null));
      lastRowStartingCoordinate = lastRowStartingCoordinate.add(Coordinate.DIAGONAL_VECTORS.get(0));
    }
    return rowPiecesToString(row) + System.lineSeparator();
  }

  /**
   * A helper method to generate a string representation of a row of the game board.
   *
   * @param pieces A list of Piece objects representing the pieces in a row on a chessboard.
   * @return A string representation of a row of the game board.
   */
  private String rowPiecesToString(@NotNull List<@NotNull Piece> pieces) {
    int width = (Constant.BOARD_SIDE_LENGTH - 1) * BOARD_TO_STRING_PIECE_SPACING + Constant.BOARD_SIDE_LENGTH;
    int padding = (width - (pieces.size() - 1) * BOARD_TO_STRING_PIECE_SPACING) / 2;
    String paddingStr = StringUtils.repeat("", " ", padding);
    return pieces.stream().map(piece -> {
      if (piece == null) {
        return BOARD_TO_STRING_EMPTY_CELL_PLACE_HOLDER;
      }
      Ansi ansi = Ansi.ansi();
      if (lastMove != null && piece.getCoordinate().equals(lastMove.getRight())) {
        ansi = ansi.a(Attribute.INTENSITY_BOLD).a(Attribute.UNDERLINE_DOUBLE);
      }
      if (piece.getSide().equals(PieceSide.WHITE)) {
        ansi = ansi.bgBright(Color.WHITE).fg(Color.BLACK);
      } else {
        ansi = ansi.bg(Color.BLACK).fgBright(Color.WHITE);
      }
      return ansi.a(piece.getType().getPieceAbbreviation()).reset().toString();
    }).collect(Collectors.joining(StringUtils.repeat("", " ", BOARD_TO_STRING_PIECE_SPACING), paddingStr, ""));
  }

}
