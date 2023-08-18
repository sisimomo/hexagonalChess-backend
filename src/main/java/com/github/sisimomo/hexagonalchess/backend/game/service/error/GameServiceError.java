package com.github.sisimomo.hexagonalchess.backend.game.service.error;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.error.Error;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameServiceError implements Error {
  UNSUPPORTED_UPDATE(HttpStatus.INTERNAL_SERVER_ERROR, "System error. Please try again later",
      "Missing if case for update subclass. System error. Please try again later", "U2ASLR"),
  UNAUTHORIZED_PLAY(HttpStatus.UNAUTHORIZED, "You [uuid: %s] are not authorized to play in the game [FriendlyId: %s]",
      "H5P26X"),
  UNAUTHORIZED_READ(HttpStatus.UNAUTHORIZED, "You [uuid: %s] are not authorized to see the game [FriendlyId: %s]",
      "8FHU4G"),
  WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "You have to provide the right password to join the game [FriendlyId: %s]",
      "O76YMB"),
  ALREADY_A_PLAYER(HttpStatus.UNAUTHORIZED, "You are already a player of the Game [friendlyId: %s]", "6CH2T5"),
  GAME_IS_ALREADY_FULL(HttpStatus.UNAUTHORIZED, "The Game [friendlyId: %s] is already full", "38T3IU"),
  NOT_FOUND_BY_FRIENDLY_ID(HttpStatus.NOT_FOUND, "Game [friendlyId: %s] not found", "QDT071"),
  NO_PIECE_FOUND_AT_COORDINATE(HttpStatus.NOT_FOUND, "No Piece was found at Coordinate[%s]", "IR86BZ"),
  NOT_YOUR_TURN(HttpStatus.BAD_REQUEST, "It's not the %s side turn", "7I0SE4"),
  CANNOT_DELETE_ALREADY_JOINED(HttpStatus.BAD_REQUEST,
      "Could not delete the Game [friendlyId: %s], it has already been joined by another user", "L761B6"),
  WAIT_FOR_OPPONENT_TO_JOIN(HttpStatus.BAD_REQUEST,
      "You must wait for a opponent to join the Game [friendlyId: %s] before moving a piece", "NTSI5J"),
  NO_PIECE_FOUND_AT_COORDINATE_ENGINE(HttpStatus.NOT_FOUND, "%s", "IR87BZ"),
  NOT_YOUR_TURN_ENGINE(HttpStatus.BAD_REQUEST, "%s", "7I0SE5"),
  MOVEMENT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "%s", "CJ71SU"),
  PROMOTION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "%s", "7R7JZS"),
  WANTED_PROMOTION_PIECE_TYPE_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "%s", "IQX6ZS"),
  GAME_IS_OVER(HttpStatus.BAD_REQUEST, "%s", "X6SF17");

  private final HttpStatus responseCode;
  private final String errorMessage;
  private final String systemErrorMessage;
  private final String errorId;

  GameServiceError(HttpStatus serviceResponseCode, String errorMessage, String errorId) {
    this(serviceResponseCode, errorMessage, errorMessage, errorId);
  }

}
