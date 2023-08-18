package com.github.sisimomo.hexagonalchess.backend.game.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.github.sisimomo.hexagonalchess.backend.game.service.GameService;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateBaseMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message.GameBaseMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.mapper.GameMessageDtoMapper;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.IdentityProviderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameWebsocketController {

  private final GameService gameService;

  private final IdentityProviderService identityProviderService;

  private final GameMessageDtoMapper gameMessageDtoMapper;

  @MessageMapping("/game/{friendlyId}")
  @SendTo("/topic/game/{friendlyId}")
  public GameBaseMessageDto updateGame(@Payload GameUpdateBaseMessageDto gameUpdateMessageDto,
      @DestinationVariable String friendlyId) {
    gameService.gameSaveUpdate(gameUpdateMessageDto, friendlyId);
    return gameMessageDtoMapper.convertToMessageDto(gameUpdateMessageDto,
        identityProviderService.getLoggedInUserUuid());
  }

}
