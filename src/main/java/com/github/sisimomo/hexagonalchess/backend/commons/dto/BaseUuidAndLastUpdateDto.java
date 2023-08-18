package com.github.sisimomo.hexagonalchess.backend.commons.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class BaseUuidAndLastUpdateDto extends BaseUuidDto implements IBaseUuidDto, IBaseLastUpdateDto {

  /**
   * The date and time the entity was added to the system.
   */
  protected Instant createDate;

  /**
   * The date and time the entity was added/updated to the system.
   */
  protected Instant updateDate;

}
