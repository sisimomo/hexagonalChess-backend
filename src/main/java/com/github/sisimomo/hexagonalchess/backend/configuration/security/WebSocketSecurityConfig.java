package com.github.sisimomo.hexagonalchess.backend.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import com.github.sisimomo.hexagonalchess.backend.commons.stomp.TopicSubscriptionInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  private final TopicSubscriptionInterceptor[] topicSubscriptionInterceptors;

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.anyMessage().authenticated();
  }

  @Override
  protected void customizeClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(topicSubscriptionInterceptors);
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

}
