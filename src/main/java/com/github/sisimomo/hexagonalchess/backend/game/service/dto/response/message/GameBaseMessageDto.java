package com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message;

import com.github.sisimomo.hexagonalchess.backend.game.service.dto.GameMessageType;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GameBaseMessageDto {

  private UUID emitter;

  public abstract GameMessageType getType();

}
