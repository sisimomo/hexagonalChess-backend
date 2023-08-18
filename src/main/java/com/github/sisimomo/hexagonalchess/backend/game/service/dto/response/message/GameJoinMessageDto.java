package com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message;

import com.github.sisimomo.hexagonalchess.backend.game.service.dto.GameMessageType;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GameJoinMessageDto extends GameBaseMessageDto {

  @Override
  public GameMessageType getType() {
    return GameMessageType.JOIN;
  }

}
