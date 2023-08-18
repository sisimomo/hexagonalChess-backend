package com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.GameMessageType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = GameUpdateMovePieceMessageDto.class, name = "movePiece"),
    @JsonSubTypes.Type(value = GameUpdateSurrenderMessageDto.class, name = "surrender")})

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class GameUpdateBaseMessageDto {

  private GameMessageType type;

}
