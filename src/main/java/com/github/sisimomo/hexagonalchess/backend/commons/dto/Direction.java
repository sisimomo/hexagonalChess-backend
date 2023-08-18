package com.github.sisimomo.hexagonalchess.backend.commons.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Direction {
  FORWARD("forward"),
  BACKWARD("backward");

  @Getter(onMethod_ = @JsonValue)
  private final String value;

}
