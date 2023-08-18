package com.github.sisimomo.hexagonalchess.backend.commons.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class BaseUuidDto implements IBaseUuidDto {

  /**
   * A field representing the uuid of the entity.
   */
  protected UUID uuid;

}
