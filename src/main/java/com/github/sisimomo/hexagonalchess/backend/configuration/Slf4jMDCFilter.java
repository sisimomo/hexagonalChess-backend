package com.github.sisimomo.hexagonalchess.backend.configuration;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;

/**
 * <a href=
 * "https://medium.com/@d.lopez.j/spring-boot-setting-a-unique-id-per-request-dd648efef2b">Source</a>
 */
@EqualsAndHashCode(callSuper = false)
public class Slf4jMDCFilter extends OncePerRequestFilter {

  public static final String DEFAULT_RESPONSE_REQUEST_UUID_HEADER = "Request-Uuid";
  public static final String DEFAULT_MDC_REQUEST_UUID_KEY = "Slf4jMDCFilter.UUID";

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
      final FilterChain chain) throws java.io.IOException, ServletException {
    try {
      final UUID requestUuid = UUID.randomUUID();
      MDC.put(DEFAULT_MDC_REQUEST_UUID_KEY, requestUuid.toString());
      if (!ObjectUtils.isEmpty(DEFAULT_MDC_REQUEST_UUID_KEY)) {
        response.addHeader(DEFAULT_RESPONSE_REQUEST_UUID_HEADER, requestUuid.toString());
      }
      chain.doFilter(request, response);
    } finally {
      MDC.remove(DEFAULT_MDC_REQUEST_UUID_KEY);
    }
  }

}
