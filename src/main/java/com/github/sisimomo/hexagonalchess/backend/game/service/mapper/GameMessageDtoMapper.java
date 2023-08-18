package com.github.sisimomo.hexagonalchess.backend.game.service.mapper;

import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateSurrenderMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message.GameBaseMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message.GameMovePieceMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message.GameSurrenderMessageDto;
import java.util.UUID;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateBaseMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateMovePieceMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.error.GameServiceError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GameMessageDtoMapper {

  public GameBaseMessageDto convertToMessageDto(GameUpdateBaseMessageDto messageDto, UUID userUuid)
      throws UncheckedException {
    if (messageDto instanceof GameUpdateMovePieceMessageDto gameUpdateMovePieceMessageDto) {
      return convertToMessageDto(gameUpdateMovePieceMessageDto, userUuid);
    } else if (messageDto instanceof GameUpdateSurrenderMessageDto gameUpdateSurrenderMessageDto) {
      return convertToMessageDto(gameUpdateSurrenderMessageDto, userUuid);
    } else {
      throw new UncheckedException(GameServiceError.UNSUPPORTED_UPDATE, log::error);
    }
  }

  @Mapping(target = "emitter", source = "userUuid")
  protected abstract GameMovePieceMessageDto convertToMessageDto(GameUpdateMovePieceMessageDto entity, UUID userUuid);

  @Mapping(target = "emitter", source = "userUuid")
  protected abstract GameSurrenderMessageDto convertToMessageDto(GameUpdateSurrenderMessageDto entity, UUID userUuid);

}
