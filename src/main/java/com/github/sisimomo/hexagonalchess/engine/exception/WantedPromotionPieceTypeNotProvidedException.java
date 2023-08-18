package com.github.sisimomo.hexagonalchess.engine.exception;

public class WantedPromotionPieceTypeNotProvidedException extends Exception {

  public WantedPromotionPieceTypeNotProvidedException() {
    super("The type of the wanted Piece for the promotion was not provided");
  }

}
