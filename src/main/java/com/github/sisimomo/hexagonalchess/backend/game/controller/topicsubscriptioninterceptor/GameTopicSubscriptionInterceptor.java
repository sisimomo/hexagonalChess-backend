package com.github.sisimomo.hexagonalchess.backend.game.controller.topicsubscriptioninterceptor;

import java.util.List;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;

import com.github.sisimomo.hexagonalchess.backend.commons.stomp.TopicSubscriptionInterceptor;
import com.github.sisimomo.hexagonalchess.backend.game.service.GameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameTopicSubscriptionInterceptor extends TopicSubscriptionInterceptor {

  private final GameService gameService;

  @Override
  protected List<Pair<String, BiConsumer<String, PathMatchInfo>>> getTopicSubscriptionInterceptors() {
    Pair<String, BiConsumer<String, PathMatchInfo>> gameTopic =
        Pair.of("/topic/game/{friendlyId}", (destinationPath, pathMatchInfo) -> {
          gameService.throwExceptionIfLoggedInUserCannotRead(pathMatchInfo.getUriVariables().get("friendlyId"));
        });

    Pair<String, BiConsumer<String, PathMatchInfo>> gameErrorTopic =
        Pair.of("/user/topic/game/{friendlyId}/errors", (destinationPath, pathMatchInfo) -> {
          gameService.throwExceptionIfLoggedInUserCannotRead(pathMatchInfo.getUriVariables().get("friendlyId"));
        });
    return List.of(gameTopic, gameErrorTopic);
  }

}
