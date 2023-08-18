package com.github.sisimomo.hexagonalchess.backend.configuration.websocket;

import org.springframework.http.server.PathContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public abstract class TopicSubscriptionInterceptor implements ChannelInterceptor {

  private final PathPattern pathPattern;

  protected TopicSubscriptionInterceptor(String strPathPattern) {
    this.pathPattern = PathPatternParser.defaultInstance.parse(strPathPattern);
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
      String destinationPath = headerAccessor.getDestination();
      PathContainer pathContainerDestination =
          destinationPath != null ? PathContainer.parsePath(destinationPath) : null;
      if (destinationPath != null && pathPattern.matches(pathContainerDestination)) {
        throwExceptionIfInvalidSubscription(destinationPath, pathPattern.matchAndExtract(pathContainerDestination));
      }
    }
    return message;
  }

  protected abstract void throwExceptionIfInvalidSubscription(String destinationPath, PathMatchInfo pathMatchInfo);

}
