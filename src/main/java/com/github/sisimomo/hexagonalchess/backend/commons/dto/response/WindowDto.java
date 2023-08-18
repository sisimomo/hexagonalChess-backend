package com.github.sisimomo.hexagonalchess.backend.commons.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WindowDto<T> {

  private final List<T> content;

  private final boolean hasNextPage;

  private final String startCursor;

  private final String endCursor;

}
