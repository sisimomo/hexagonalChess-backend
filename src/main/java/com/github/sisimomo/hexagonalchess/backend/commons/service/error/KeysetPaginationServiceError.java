package com.github.sisimomo.hexagonalchess.backend.commons.service.error;


import org.springframework.http.HttpStatus;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.error.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KeysetPaginationServiceError implements Error {
  BAD_FILTER(HttpStatus.INTERNAL_SERVER_ERROR, "System error. Please try again later",
      "Provided specification caused an error", "PG7BCS"),
  ENCRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "System error. Please try again later",
      "An error occurred while trying to encrypt the KeySet cursor", "83YA07"),
  DECRYPTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "The provided cursor [%s] is not a valid cursor",
      "An error occurred while trying to decrypt the KeySet cursor", "Y9M0N8");

  private final HttpStatus responseCode;
  private final String errorMessage;
  private final String systemErrorMessage;
  private final String errorId;

}
