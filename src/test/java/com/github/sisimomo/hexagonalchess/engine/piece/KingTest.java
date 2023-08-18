package com.github.sisimomo.hexagonalchess.engine.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;

class KingTest {

  @Test
  void isCheckTest() {
    IntStream.range(0, 5).mapToObj(i -> Pair.of(i, PieceTest.givenPossibleCheckPieces())).forEach(pair -> {
      // Given
      Pair<Boolean, Coordinate> moveWhiteQueenTo = switch (pair.getLeft()) {
        case 0 -> Pair.of(true, new Coordinate(1, -1, 0));
        case 1 -> Pair.of(true, new Coordinate(-1, 1, 0));
        case 2 -> Pair.of(true, new Coordinate(0, -1, 1));
        case 3 -> Pair.of(false, new Coordinate(-1, 0, 1));
        case 4 -> Pair.of(false, new Coordinate(2, 0, -2));
        default -> throw new IllegalStateException("Unexpected value: " + pair.getLeft());
      };

      // When
      Queen whiteQueen = (Queen) Piece.findPiece(pair.getRight(), PieceType.QUEEN, PieceSide.WHITE, null).orElseThrow();
      King whiteKing = (King) Piece.findPiece(pair.getRight(), PieceType.KING, PieceSide.WHITE, null).orElseThrow();
      Coordinate whiteQueenFrom = whiteQueen.getCoordinate();
      whiteQueen.setCoordinate(moveWhiteQueenTo.getRight());

      // Then
      assertEquals(moveWhiteQueenTo.getLeft(),
          whiteKing.isCheck(pair.getRight(), Pair.of(whiteQueenFrom, moveWhiteQueenTo.getRight())));

    });

  }

  @Test
  void isCheckMateTest() {
    // TODO
  }

}
