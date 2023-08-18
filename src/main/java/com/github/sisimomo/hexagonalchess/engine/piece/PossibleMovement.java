package com.github.sisimomo.hexagonalchess.engine.piece;

import com.github.sisimomo.hexagonalchess.engine.Coordinate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
class PossibleMovement {
  @NotNull
  private Coordinate vector;
  @Min(1)
  private int maxRange;

}
