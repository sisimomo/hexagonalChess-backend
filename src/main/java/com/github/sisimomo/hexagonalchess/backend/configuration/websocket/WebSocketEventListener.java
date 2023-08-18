package com.github.sisimomo.hexagonalchess.backend.configuration.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.IdentityProviderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final IdentityProviderService identityProviderService;

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    log.debug("User disconnect! {}", identityProviderService.getLoggedInUserUuid());
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    identityProviderService.setLoggedInUser((JwtAuthenticationToken) event.getUser());
    log.debug("User connected! {}", identityProviderService.getLoggedInUserUuid());
  }

}
