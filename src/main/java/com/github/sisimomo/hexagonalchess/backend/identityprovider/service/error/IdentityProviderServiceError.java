package com.github.sisimomo.hexagonalchess.backend.identityprovider.service.error;

import org.springframework.http.HttpStatus;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.error.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdentityProviderServiceError implements Error {
  NOT_FOUND_BY_UUID(HttpStatus.NOT_FOUND, "User [UUID: %s] not found", "S857BC");

  private final HttpStatus responseCode;
  private final String errorMessage;
  private final String systemErrorMessage;
  private final String errorId;

  IdentityProviderServiceError(HttpStatus serviceResponseCode, String errorMessage, String errorId) {
    this(serviceResponseCode, errorMessage, errorMessage, errorId);
  }

}
