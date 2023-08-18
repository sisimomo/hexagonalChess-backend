package com.github.sisimomo.hexagonalchess.engine.enumeration;

public enum PieceSide {
  WHITE,
  BLACK;

  public PieceSide inverse() {
    return this.equals(WHITE) ? BLACK : WHITE;
  }

}
