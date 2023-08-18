package com.github.sisimomo.hexagonalchess.backend.commons.dto;

import java.time.Instant;

public interface IBaseLastUpdateDto {

  Instant getCreateDate();

  Instant getUpdateDate();

}
