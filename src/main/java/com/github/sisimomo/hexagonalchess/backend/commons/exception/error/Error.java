package com.github.sisimomo.hexagonalchess.backend.commons.exception.error;

import org.springframework.http.HttpStatus;

public interface Error {

  String name();

  HttpStatus getResponseCode();

  String getErrorMessage();

  String getSystemErrorMessage();

  String getErrorId();

}
