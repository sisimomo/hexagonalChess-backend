package com.github.sisimomo.hexagonalchess.backend.piece.dao.entity;

import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PieceJsonEntity {

  @NotNull
  private PieceType type;
  @NotNull
  private PieceSide side;
  @Valid
  @NotNull
  private CoordinateJsonEntity coordinate;

}
