package com.github.sisimomo.hexagonalchess.backend.commons.exception;


import com.github.sisimomo.hexagonalchess.backend.commons.exception.error.Error;
import com.github.sisimomo.hexagonalchess.backend.commons.service.mapper.Default;
import java.util.function.Consumer;

import lombok.Getter;

@Getter
public class UncheckedException extends RuntimeException {

  private static final String MESSAGE_FORMAT = "[Status: %s, ErrorId: %s] - %s";

  private final transient Error error;
  private final String formattedUserErrorMessage;

  public UncheckedException(Error error, Consumer<String> log, Object... msgParams) {
    this(error, log, null, msgParams);
  }

  @Default
  public UncheckedException(Error error, Consumer<String> log, Throwable cause, Object... msgParams) {
    super(String.format(MESSAGE_FORMAT, error.getResponseCode().name(), error.getErrorId(),
        (msgParams.length == 0 ? error.getSystemErrorMessage()
            : String.format(error.getSystemErrorMessage(), msgParams))),
        cause);

    if (log != null) {
      log.accept(getMessage());
    }

    this.error = error;
    this.formattedUserErrorMessage =
        (msgParams.length == 0 ? error.getErrorMessage() : String.format(error.getErrorMessage(), msgParams));
  }

  public String getUserMessage() {
    return String.format(MESSAGE_FORMAT, error.getResponseCode().name(), error.getErrorId(), formattedUserErrorMessage);
  }

}
