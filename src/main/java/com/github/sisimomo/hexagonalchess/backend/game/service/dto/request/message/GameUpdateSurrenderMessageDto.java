package com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("surrender")
@NoArgsConstructor
@Getter
@Setter
public class GameUpdateSurrenderMessageDto extends GameUpdateBaseMessageDto {

}
