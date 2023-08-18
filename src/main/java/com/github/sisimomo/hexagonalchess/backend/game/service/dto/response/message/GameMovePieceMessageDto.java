package com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message;

import com.github.sisimomo.hexagonalchess.backend.game.service.dto.GameMessageType;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.CoordinateDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GameMovePieceMessageDto extends GameBaseMessageDto {

  @NotNull
  private CoordinateDto from;

  @NotNull
  private CoordinateDto to;

  private PieceType wantedPromotionPieceType;

  @Override
  public GameMessageType getType() {
    return GameMessageType.MOVE_PIECE;
  }

}
