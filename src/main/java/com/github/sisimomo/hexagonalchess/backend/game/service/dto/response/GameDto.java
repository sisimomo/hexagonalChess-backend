package com.github.sisimomo.hexagonalchess.backend.game.service.dto.response;

import java.util.UUID;

import com.github.sisimomo.hexagonalchess.backend.commons.dto.BaseLastUpdateDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class GameDto extends BaseLastUpdateDto {

  @NotNull
  @Size(max = 7)
  private String friendlyId;

  @NotNull
  private UUID whiteUserUuid;

  private UUID blackUserUuid;

  private boolean publicGame;

  @Valid
  private GameSaveDto gameSave;

}
