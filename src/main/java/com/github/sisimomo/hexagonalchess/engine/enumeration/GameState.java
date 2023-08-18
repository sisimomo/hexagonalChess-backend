package com.github.sisimomo.hexagonalchess.engine.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameState {
  IN_PROGRESS(false),
  WHITE_IN_CHECK(false),
  BLACK_IN_CHECK(false),
  WHITE_WON(true),
  BLACK_WON(true),
  WHITE_WON_BY_SURRENDER(true),
  BLACK_WON_BY_SURRENDER(true),
  DRAW_STALEMATE(true),
  DRAW_INSUFFICIENT_MATERIAL(true),
  DRAW_THREEFOLD_REPETITION(true);

  private final boolean ended;

}
