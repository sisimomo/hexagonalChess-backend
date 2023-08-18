package com.github.sisimomo.hexagonalchess.backend.piece.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PieceType {
  BISHOP("bishop"),
  KING("king"),
  KNIGHT("knight"),
  PAWN("pawn"),
  QUEEN("queen"),
  ROOK("rook");

  @Getter(onMethod_ = @JsonValue)
  private final String value;

}
