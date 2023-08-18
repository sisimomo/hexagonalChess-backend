package com.github.sisimomo.hexagonalchess.backend.game.service.dto.response;

import java.util.List;

import com.github.sisimomo.hexagonalchess.backend.game.enumeration.GameState;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.CoordinateDto;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.response.PieceDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameSaveDto {

  @NotNull
  private GameState state;
  @NotNull
  private PieceSide sideTurn;
  private @Valid CoordinateDto lastMoveFrom;
  private @Valid CoordinateDto lastMoveTo;
  private List<@NotNull @Valid PieceDto> pieces;
  private List<@NotEmpty List<@NotNull @Valid PieceDto>> history;

}
