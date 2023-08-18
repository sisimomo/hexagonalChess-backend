package com.github.sisimomo.hexagonalchess.engine.enumeration;


import com.github.sisimomo.hexagonalchess.engine.piece.Bishop;
import com.github.sisimomo.hexagonalchess.engine.piece.King;
import com.github.sisimomo.hexagonalchess.engine.piece.Knight;
import com.github.sisimomo.hexagonalchess.engine.piece.Pawn;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;
import com.github.sisimomo.hexagonalchess.engine.piece.Queen;
import com.github.sisimomo.hexagonalchess.engine.piece.Rook;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PieceType {
  BISHOP(Bishop.class, 'B'),
  KING(King.class, 'K'),
  KNIGHT(Knight.class, 'N'),
  PAWN(Pawn.class, 'P'),
  QUEEN(Queen.class, 'Q'),
  ROOK(Rook.class, 'R');

  private final Class<? extends Piece> clazz;

  private final char pieceAbbreviation;

}
