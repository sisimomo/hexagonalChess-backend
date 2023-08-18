package com.github.sisimomo.hexagonalchess.backend.game.service.dto.request;

import com.github.sisimomo.hexagonalchess.backend.commons.validation.NullOrNotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "JoinGame")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameJoinUpdateDto {

  @NullOrNotBlank
  private String password;

}
