package com.github.sisimomo.hexagonalchess.backend.piece.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PieceSide {
  WHITE("white"),
  BLACK("black");

  @Getter(onMethod_ = @JsonValue)
  private final String value;

}
