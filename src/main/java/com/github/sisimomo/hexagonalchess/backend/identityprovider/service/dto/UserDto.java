package com.github.sisimomo.hexagonalchess.backend.identityprovider.service.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

  @NotNull
  private UUID uuid;

  @NotNull
  private String email;

}
