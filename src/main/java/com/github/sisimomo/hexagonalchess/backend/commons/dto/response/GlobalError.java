package com.github.sisimomo.hexagonalchess.backend.commons.dto.response;

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
public class GlobalError {

  private String objectName;
  private String errorCode;

}
