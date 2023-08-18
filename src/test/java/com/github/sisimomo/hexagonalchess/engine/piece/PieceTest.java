package com.github.sisimomo.hexagonalchess.engine.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.engine.exception.MultiplePiecesFoundRuntimeException;

class PieceTest {

  private static final List<Piece> INITIAL_PIECES_POSITIONS = List.of(
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

  public static List<Piece> givenPossibleCheckPieces() {
    return List.of(new Queen(PieceSide.WHITE, Coordinate.ORIGIN),
        new Rook(PieceSide.BLACK, Coordinate.DIRECTION_VECTORS.get(0).multiply(3)),
        new King(PieceSide.WHITE, Coordinate.DIRECTION_VECTORS.get(0).multiply(-2)),
        new King(PieceSide.BLACK, new Coordinate(-5, 4, 1)));
  }

  public Pair<Coordinate, Coordinate> givenPossibleCheckLastMove() {
    return Pair.of(Coordinate.DIRECTION_VECTORS.get(3).multiply(4), Coordinate.ORIGIN);
  }

  @Test
  void createPieceTest() {
    // Given
    PieceType pieceType = PieceType.PAWN;
    PieceSide pieceSide = PieceSide.WHITE;
    Coordinate pieceCoordinate = Coordinate.ORIGIN;
    // When
    Piece piece = Piece.createPiece(pieceType, pieceSide, pieceCoordinate);
    // Then
    assertTrue(piece instanceof Pawn);
    assertEquals(piece.getType(), pieceType);
    assertEquals(piece.getSide(), pieceSide);
    assertEquals(piece.getCoordinate(), pieceCoordinate);
  }

  @Test
  void cloneTest() {
    // Given
    Piece piece = Piece.createPiece(PieceType.PAWN, PieceSide.WHITE, Coordinate.ORIGIN);
    // When
    Piece clonedPiece = piece.clone();
    List<Piece> clonedPieces = Piece.clone(INITIAL_PIECES_POSITIONS);
    // Then
    assertClone(piece, clonedPiece);
    for (int i = 0; i < INITIAL_PIECES_POSITIONS.size(); i++) {
      assertClone(INITIAL_PIECES_POSITIONS.get(i), clonedPieces.get(i));
    }
  }

  private void assertClone(Piece piece, Piece clonedPiece) {
    assertEquals(piece, clonedPiece);
    PieceType pieceType = piece.getType();
    PieceSide pieceSide = piece.getSide();
    Coordinate pieceCoordinate = piece.getCoordinate();
    PieceSide clonedPieceSide = pieceSide.inverse();
    Coordinate clonedPieceCoordinate = pieceCoordinate.add(new Coordinate(1, 1, 1));
    clonedPiece.setSide(clonedPieceSide);
    clonedPiece.setCoordinate(clonedPieceCoordinate);
    assertEquals(pieceType, piece.getType());
    assertEquals(pieceSide, piece.getSide());
    assertEquals(pieceCoordinate, piece.getCoordinate());
    assertEquals(clonedPieceSide, clonedPiece.getSide());
    assertEquals(clonedPieceCoordinate, clonedPiece.getCoordinate());
    assertEquals(clonedPiece.getType(), piece.getType());
    assertNotEquals(clonedPiece.getSide(), piece.getSide());
    assertNotEquals(clonedPiece.getCoordinate(), piece.getCoordinate());
    clonedPiece.setSide(piece.getSide());
    clonedPiece.setCoordinate(piece.getCoordinate());
  }

  @Test
  void findPieceTest() {
    // Given
    PieceType pieceType = PieceType.PAWN;
    PieceSide pieceSide = PieceSide.WHITE;
    Coordinate pieceCoordinate = new Coordinate(-4, 5, -1);
    // When
    Optional<Piece> oPiece = Piece.findPiece(INITIAL_PIECES_POSITIONS, pieceType, pieceSide, pieceCoordinate);
    // Then
    assertTrue(oPiece.isPresent());
    assertEquals(pieceType, oPiece.get().getType());
    assertEquals(pieceSide, oPiece.get().getSide());
    assertEquals(pieceCoordinate, oPiece.get().getCoordinate());
    assertThrows(MultiplePiecesFoundRuntimeException.class,
        () -> Piece.findPiece(INITIAL_PIECES_POSITIONS, pieceType, pieceSide, null));
  }

  @Test
  void findPiecesTest() {
    // Given
    List<Piece> pieces = Piece.findPieces(INITIAL_PIECES_POSITIONS, PieceType.KNIGHT, null);
    assertTrue(Piece.equals(List.of(new Knight(PieceSide.WHITE, new Coordinate(-2, 5, -3)),
        new Knight(PieceSide.WHITE, new Coordinate(2, 3, -5))), pieces));
  }

  @Test
  void pieceEqualsTest() {
    // Given
    List<Piece> pieces = List.of(Piece.createPiece(PieceType.PAWN, PieceSide.WHITE, new Coordinate(-4, 5, -1)),
        Piece.createPiece(PieceType.PAWN, PieceSide.WHITE, new Coordinate(-4, 5, -1)),
        Piece.createPiece(PieceType.PAWN, PieceSide.WHITE, new Coordinate(-4, 6, -1)),
        Piece.createPiece(PieceType.PAWN, PieceSide.BLACK, new Coordinate(-4, 5, -1)),
        Piece.createPiece(PieceType.KING, PieceSide.WHITE, new Coordinate(-4, 5, -1)));
    // When/Then
    assertEquals(pieces.get(0), pieces.get(1));
    assertNotEquals(pieces.get(0), pieces.get(2));
    assertNotEquals(pieces.get(0), pieces.get(3));
    assertNotEquals(pieces.get(0), pieces.get(4));
    assertTrue(Piece.equals(pieces.get(1), pieces.get(1)));
    assertFalse(Piece.equals(pieces.get(1), pieces.get(2)));
    assertFalse(Piece.equals(pieces.get(1), pieces.get(3)));
    assertFalse(Piece.equals(pieces.get(1), pieces.get(4)));
  }

  @Test
  void piecesEqualsTest() {
    // Given
    List<Piece> modifiedInitialPiecesPositions = Piece.clone(INITIAL_PIECES_POSITIONS).stream().peek(p -> {
      if (p.getType().equals(PieceType.KING)) {
        p.setSide(p.getSide().inverse());
      }
    }).toList();
    // When/Then
    assertTrue(Piece.equals(INITIAL_PIECES_POSITIONS, INITIAL_PIECES_POSITIONS));
    assertFalse(Piece.equals(INITIAL_PIECES_POSITIONS, modifiedInitialPiecesPositions));
  }

  @Test
  void isMoveValidTest() {
    // Tested by testing {@link Piece#allPossibleMoves}.
  }

  @Test
  void checkIfMoveCauseSelfCheckTest() {
    // Given
    List<Piece> pieces = givenPossibleCheckPieces();
    Pair<Coordinate, Coordinate> lastMove = givenPossibleCheckLastMove();
    // When/Then
    assertFalse(pieces.get(0).checkIfMoveCauseSelfCheck(new Coordinate(-1, 0, 1), pieces, lastMove));
    assertFalse(pieces.get(0).checkIfMoveCauseSelfCheck(new Coordinate(1, 0, -1), pieces, lastMove));
    assertFalse(pieces.get(0).checkIfMoveCauseSelfCheck(new Coordinate(2, 0, -2), pieces, lastMove));
    assertFalse(pieces.get(0).checkIfMoveCauseSelfCheck(new Coordinate(3, 0, -3), pieces, lastMove));
    assertTrue(pieces.get(0).checkIfMoveCauseSelfCheck(new Coordinate(1, -1, 0), pieces, lastMove));
  }

  private Map<String, List<Piece>> givenPiecesExtractPlayableMovesTest() {
    Map<String, List<Piece>> map = new HashMap<>();
    map.put("noObstacle", List.of(Piece.createPiece(PieceType.QUEEN, PieceSide.WHITE, Coordinate.ORIGIN)));
    map.put("obstacle", List.of(new Queen(PieceSide.WHITE, Coordinate.ORIGIN),
        new Pawn(PieceSide.WHITE, Coordinate.DIRECTION_VECTORS.get(0).multiply(3))));
    map.put("captureablePiece", List.of(new Queen(PieceSide.WHITE, Coordinate.ORIGIN),
        new Pawn(PieceSide.BLACK, Coordinate.DIRECTION_VECTORS.get(0).multiply(3))));
    map.put("checkSelfCheck", PieceTest.givenPossibleCheckPieces());
    return map;
  }

  @Test
  void extractPlayableMovesTestMaxRange() {
    // Given
    List<PossibleMovement> possibleMovements =
        List.of(new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), Integer.MAX_VALUE));
    Map<String, List<Piece>> map = givenPiecesExtractPlayableMovesTest();
    Pair<Coordinate, Coordinate> lastMove = givenPossibleCheckLastMove();


    // When
    List<Coordinate> coordinatesResult1 =
        map.get("noObstacle").get(0).extractPlayableMoves(possibleMovements, map.get("noObstacle"), lastMove, false);

    List<Coordinate> coordinatesResult2 =
        map.get("obstacle").get(0).extractPlayableMoves(possibleMovements, map.get("obstacle"), lastMove, false);

    List<Coordinate> coordinatesResult3 = map.get("captureablePiece").get(0).extractPlayableMoves(possibleMovements,
        map.get("captureablePiece"), lastMove, false);


    // Then
    List<Coordinate> coordinatesExpected1 = List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2),
        new Coordinate(3, 0, -3), new Coordinate(4, 0, -4), new Coordinate(5, 0, -5));
    assertTrue(Coordinate.equals(coordinatesExpected1, coordinatesResult1));

    List<Coordinate> coordinatesExpected2 = List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2));
    assertTrue(Coordinate.equals(coordinatesExpected2, coordinatesResult2));

    List<Coordinate> coordinatesExpected3 =
        List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2), new Coordinate(3, 0, -3));
    assertTrue(Coordinate.equals(coordinatesExpected3, coordinatesResult3));
  }

  @Test
  void extractPlayableMovesTest1Range() {
    // Given
    List<PossibleMovement> possibleMovements = List.of(new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), 1));
    Map<String, List<Piece>> map = givenPiecesExtractPlayableMovesTest();
    Pair<Coordinate, Coordinate> lastMove = givenPossibleCheckLastMove();


    // When
    List<Coordinate> coordinatesResult1 =
        map.get("noObstacle").get(0).extractPlayableMoves(possibleMovements, map.get("noObstacle"), lastMove, false);

    List<Coordinate> coordinatesResult2 =
        map.get("obstacle").get(0).extractPlayableMoves(possibleMovements, map.get("obstacle"), lastMove, false);

    List<Coordinate> coordinatesResult3 = map.get("captureablePiece").get(0).extractPlayableMoves(possibleMovements,
        map.get("captureablePiece"), lastMove, false);


    // Then
    List<Coordinate> coordinatesExpected = List.of(new Coordinate(1, 0, -1));
    assertTrue(Coordinate.equals(coordinatesExpected, coordinatesResult1));
    assertTrue(Coordinate.equals(coordinatesExpected, coordinatesResult2));
    assertTrue(Coordinate.equals(coordinatesExpected, coordinatesResult3));
  }

  @Test
  void extractPlayableMovesTest3Range() {
    // Given
    List<PossibleMovement> possibleMovements = List.of(new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), 3));
    Map<String, List<Piece>> map = givenPiecesExtractPlayableMovesTest();
    Pair<Coordinate, Coordinate> lastMove = givenPossibleCheckLastMove();


    // When
    List<Coordinate> coordinatesResult1 =
        map.get("noObstacle").get(0).extractPlayableMoves(possibleMovements, map.get("noObstacle"), lastMove, false);
    System.out.println(coordinatesResult1);

    List<Coordinate> coordinatesResult2 =
        map.get("obstacle").get(0).extractPlayableMoves(possibleMovements, map.get("obstacle"), lastMove, false);
    System.out.println(coordinatesResult2);

    List<Coordinate> coordinatesResult3 = map.get("captureablePiece").get(0).extractPlayableMoves(possibleMovements,
        map.get("captureablePiece"), lastMove, false);
    System.out.println(coordinatesResult3);


    // Then
    List<Coordinate> coordinatesExpected1 =
        List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2), new Coordinate(3, 0, -3));
    assertTrue(Coordinate.equals(coordinatesExpected1, coordinatesResult1));

    List<Coordinate> coordinatesExpected2 = List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2));
    assertTrue(Coordinate.equals(coordinatesExpected2, coordinatesResult2));

    List<Coordinate> coordinatesExpected3 =
        List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2), new Coordinate(3, 0, -3));
    assertTrue(Coordinate.equals(coordinatesExpected3, coordinatesResult3));
  }

  @Test
  void extractPlayableMovesTestCheckForSelfCheck() {
    // Given
    List<PossibleMovement> possibleMovements =
        List.of(new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(0), Integer.MAX_VALUE),
            new PossibleMovement(Coordinate.DIRECTION_VECTORS.get(1), Integer.MAX_VALUE));
    Map<String, List<Piece>> map = givenPiecesExtractPlayableMovesTest();
    Pair<Coordinate, Coordinate> lastMove = givenPossibleCheckLastMove();

    // When
    List<Coordinate> coordinatesResult = map.get("checkSelfCheck").get(0).extractPlayableMoves(possibleMovements,
        map.get("checkSelfCheck"), lastMove, true);


    // Then
    List<Coordinate> coordinatesExpected1 =
        List.of(new Coordinate(1, 0, -1), new Coordinate(2, 0, -2), new Coordinate(3, 0, -3));
    assertTrue(Coordinate.equals(coordinatesExpected1, coordinatesResult));
  }

  @Test
  void reflectIfBlackTest() {
    // Given
    List<Piece> pieces = List.of(Piece.createPiece(PieceType.PAWN, PieceSide.BLACK, new Coordinate(-1, 1, 0)),
        Piece.createPiece(PieceType.PAWN, PieceSide.BLACK, new Coordinate(-1, 1, 0)),
        Piece.createPiece(PieceType.PAWN, PieceSide.WHITE, new Coordinate(-1, 1, 0)));

    // When/Then
    pieces.forEach(piece -> {
      Coordinate coordinate = piece.getSide().equals(PieceSide.WHITE) ? piece.getCoordinate()
          : piece.getCoordinate().horizontalReflection();
      assertEquals(coordinate, piece.reflectIfBlack(piece.getCoordinate()));
    });
  }

}
