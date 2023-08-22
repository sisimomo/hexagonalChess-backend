package com.github.sisimomo.hexagonalchess.backend.commons.stomp;

import java.util.List;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.tuple.Pair;
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

  private List<Pair<PathPattern, BiConsumer<String, PathMatchInfo>>> getTopicSubscriptionInterceptorsInner() {
    return getTopicSubscriptionInterceptors().stream()
        .map(pair -> Pair.of(PathPatternParser.defaultInstance.parse(pair.getLeft()), pair.getRight())).toList();
  }

  /**
   * Returns a list of pairs, where each pair consists of a string and a bi-consumer, for topic
   * subscription interceptors.
   *
   * @return The method is returning a list of pairs, where each pair consists of a string and a
   *         BiConsumer.
   */
  protected abstract List<Pair<String, BiConsumer<String, PathMatchInfo>>> getTopicSubscriptionInterceptors();

  /**
   * Invoked before the Message is actually sent to the channel. This allows for modification of the
   * Message if necessary. If this method returns null then the actual send invocation will not occur.
   * Check if a STOMP message is a subscription and if so, find if any topic subscription interceptors
   * match the destination path.
   *
   * @param message Represents the message being sent.
   * @param channel The message channel that the message is being sent to.
   * @return A {@link Message}<?> object.
   */
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
      String destinationPath = headerAccessor.getDestination();
      if (destinationPath != null) {
        getTopicSubscriptionInterceptorsInner().forEach(pair -> {
          PathContainer pathContainerDestination = PathContainer.parsePath(destinationPath);
          if (pair.getLeft().matches(pathContainerDestination)) {
            pair.getRight().accept(destinationPath, pair.getLeft().matchAndExtract(pathContainerDestination));
          }
        });
      }
    }
    return message;
  }

}
