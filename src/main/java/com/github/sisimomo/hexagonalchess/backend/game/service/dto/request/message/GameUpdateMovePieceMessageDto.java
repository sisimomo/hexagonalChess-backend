package com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.CoordinateDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("movePiece")
@NoArgsConstructor
@Getter
@Setter
public class GameUpdateMovePieceMessageDto extends GameUpdateBaseMessageDto {

  @NotNull
  private CoordinateDto from;

  @NotNull
  private CoordinateDto to;

  private PieceType wantedPromotionPieceType;

}
