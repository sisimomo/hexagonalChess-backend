package com.github.sisimomo.hexagonalchess.backend.configuration.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {

  private final AuthenticationEntryPoint authEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults()).authorizeHttpRequests(ahrCustomizer -> {
      ahrCustomizer.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll(); // Swagger
      ahrCustomizer.anyRequest().authenticated();
    }).exceptionHandling(ehCustomizer -> ehCustomizer.authenticationEntryPoint(authEntryPoint))
        .oauth2Login(Customizer.withDefaults()).oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
        .csrf(Customizer.withDefaults()).headers(c -> c.frameOptions(FrameOptionsConfig::sameOrigin));
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(@Value("${frontend.host}") String frontendHost) {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(frontendHost));
    configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
    configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // For WebSocket
  @Bean
  public BearerTokenResolver bearerTokenResolver() {
    DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
    defaultBearerTokenResolver.setAllowUriQueryParameter(true);
    return defaultBearerTokenResolver;
  }

}
