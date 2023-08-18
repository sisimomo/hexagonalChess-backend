package com.github.sisimomo.hexagonalchess.backend.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keycloak Admin REST Client configuration.
 */
@Configuration
public class KeycloakConfiguration {

  @Bean
  public Keycloak keycloak(@Value("${keycloak.host}") String host, @Value("${keycloak.username}") String username,
      @Value("${keycloak.password}") String password) {
    return KeycloakBuilder.builder().serverUrl(host).realm("master").clientId("admin-cli")
        .grantType(OAuth2Constants.PASSWORD).username(username).password(password).build();
  }

}
