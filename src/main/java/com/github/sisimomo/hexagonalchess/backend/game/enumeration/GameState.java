package com.github.sisimomo.hexagonalchess.backend.game.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GameState {
  IN_PROGRESS("inProgress"),
  WHITE_IN_CHECK("whiteInCheck"),
  BLACK_IN_CHECK("blackInCheck"),
  WHITE_WON("whiteWon"),
  BLACK_WON("blackWon"),
  WHITE_WON_BY_SURRENDER("whiteWonBySurrender"),
  BLACK_WON_BY_SURRENDER("blackWonBySurrender"),
  DRAW_STALEMATE("drawStalemate"),
  DRAW_INSUFFICIENT_MATERIAL("drawInsufficientMaterial"),
  DRAW_THREEFOLD_REPETITION("drawThreefoldRepetition");

  @Getter(onMethod_ = @JsonValue)
  private final String value;

}
