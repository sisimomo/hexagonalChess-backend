package com.github.sisimomo.hexagonalchess.backend.game.controller.converter;

import com.github.sisimomo.hexagonalchess.backend.commons.dto.Direction;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;

public class StringToDirection implements Converter<String, Direction> {

  @Override
  public Direction convert(String source) {
    return Stream.of(Direction.values()).filter(t -> t.getValue().equals(source)).findAny().orElse(null);
  }

}
