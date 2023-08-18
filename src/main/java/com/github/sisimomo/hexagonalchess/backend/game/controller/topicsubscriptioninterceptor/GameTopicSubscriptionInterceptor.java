package com.github.sisimomo.hexagonalchess.backend.game.controller.topicsubscriptioninterceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;

import com.github.sisimomo.hexagonalchess.backend.configuration.websocket.TopicSubscriptionInterceptor;
import com.github.sisimomo.hexagonalchess.backend.game.service.GameService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GameTopicSubscriptionInterceptor extends TopicSubscriptionInterceptor {

  private final GameService gameService;

  public GameTopicSubscriptionInterceptor(GameService gameService) {
    super("/topic/game/{friendlyId}");
    this.gameService = gameService;
  }

  @Override
  protected void throwExceptionIfInvalidSubscription(String destinationPath, PathMatchInfo pathMatchInfo) {
    gameService.throwExceptionIfLoggedInUserCannotRead(pathMatchInfo.getUriVariables().get("friendlyId"));
  }

}
