package com.github.sisimomo.hexagonalchess.backend.commons.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "ErrorResponse")
public class ErrorResponse {

  @NotNull
  private final String msg;

  private String errorId;

  private List<FieldError> fieldErrors;
  private List<GlobalError> globalErrors;

  public ErrorResponse(@NotNull String msg, @NotNull String errorId) {
    this(msg);
    this.errorId = errorId;
  }

  public ErrorResponse(@NotNull String msg, List<FieldError> fieldErrors, List<GlobalError> globalErrors) {
    this(msg);
    this.fieldErrors = fieldErrors;
    this.globalErrors = globalErrors;
  }

}
