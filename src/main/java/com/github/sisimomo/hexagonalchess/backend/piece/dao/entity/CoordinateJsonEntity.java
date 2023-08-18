package com.github.sisimomo.hexagonalchess.backend.piece.dao.entity;

import com.github.sisimomo.hexagonalchess.engine.Constant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CoordinateJsonEntity {

  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private int q;
  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private int r;
  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private int s;

}
