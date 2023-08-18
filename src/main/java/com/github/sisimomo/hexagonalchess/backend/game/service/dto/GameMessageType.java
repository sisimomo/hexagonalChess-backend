package com.github.sisimomo.hexagonalchess.backend.game.service.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameMessageType {
  JOIN("join"),
  MOVE_PIECE("movePiece"),
  SURRENDER("surrender");

  @Getter(onMethod_ = @JsonValue)
  private final String value;

}
