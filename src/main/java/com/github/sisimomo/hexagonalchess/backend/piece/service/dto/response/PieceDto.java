package com.github.sisimomo.hexagonalchess.backend.piece.service.dto.response;

import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.CoordinateDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PieceDto {

  @NotNull
  private PieceType type;
  @NotNull
  private PieceSide side;
  @Valid
  @NotNull
  private CoordinateDto coordinate;

}
