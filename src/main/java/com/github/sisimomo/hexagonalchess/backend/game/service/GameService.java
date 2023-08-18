package com.github.sisimomo.hexagonalchess.backend.game.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Window;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.Direction;
import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;
import com.github.sisimomo.hexagonalchess.backend.commons.service.BaseEntityGraphService;
import com.github.sisimomo.hexagonalchess.backend.commons.service.KeysetPaginationService;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity_;
import com.github.sisimomo.hexagonalchess.backend.game.dao.repository.GameRepository;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.GameCreateDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.GameJoinUpdateDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateBaseMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.message.GameUpdateMovePieceMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.error.GameServiceError;
import com.github.sisimomo.hexagonalchess.backend.game.service.mapper.GameSaveMapper;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.IdentityProviderService;
import com.github.sisimomo.hexagonalchess.backend.piece.service.mapper.CoordinateMapper;
import com.github.sisimomo.hexagonalchess.backend.piece.service.mapper.PieceTypeMapper;
import com.github.sisimomo.hexagonalchess.engine.Coordinate;
import com.github.sisimomo.hexagonalchess.engine.Game;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide;
import com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType;
import com.github.sisimomo.hexagonalchess.engine.exception.GameOverException;
import com.github.sisimomo.hexagonalchess.engine.exception.MovementNotAllowedException;
import com.github.sisimomo.hexagonalchess.engine.exception.NotYourTurnException;
import com.github.sisimomo.hexagonalchess.engine.exception.PieceNotFoundOnGameBoardException;
import com.github.sisimomo.hexagonalchess.engine.exception.PromotionNotAllowedException;
import com.github.sisimomo.hexagonalchess.engine.exception.WantedPromotionPieceTypeNotProvidedException;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService extends BaseEntityGraphService<GameEntity> {

  private final GameRepository repository;
  private final IdentityProviderService identityProviderService;
  private final CoordinateMapper coordinateMapper;
  private final GameSaveMapper gameSaveMapper;
  private final PieceTypeMapper pieceTypeMapper;
  private final KeysetPaginationService keysetPaginationService;
  private final SecureRandom secureRandom = new SecureRandom();
  private final char[] friendlyIdAlphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

  public Window<GameEntity> getAllLoggedInUserGames(String cursor, int maxResults, Direction direction) {
    return keysetPaginationService.getAll(repository,
        GameRepository.Specs.byWhiteUserUuidOrBlackUserUuid(identityProviderService.getLoggedInUserUuid()),
        Sort.by(Order.by(GameEntity_.FRIENDLY_ID)), cursor, maxResults, direction);
  }

  public Window<GameEntity> getAllPasswordlessGamesMissingPlayer(String cursor, int maxResults, Direction direction) {
    return keysetPaginationService.getAll(repository,
        GameRepository.Specs.byBlackUserUuidIsNull().and(GameRepository.Specs.byPasswordIsNull()),
        Sort.by(Order.by(GameEntity_.FRIENDLY_ID)), cursor, maxResults, direction);
  }

  /**
   * Find the entity by its friendlyId and check user permissions, or throw an
   * {@link UncheckedException} if not found. Using {@link #getByFriendlyId(String, EntityGraph)}.
   *
   * @param friendlyId The Unique Identifier (UID) of the entity to be retrieved.
   * @param entityGraphSuffix The entityGraph suffix to use for the query. Using
   *        {@link #getEntityGraphBySuffix}.
   * @return A single entity object.
   */
  public GameEntity getByFriendlyIdAndCheckPermission(@NotNull String friendlyId, String entityGraphSuffix)
      throws UncheckedException {
    GameEntity entity = getByFriendlyId(friendlyId,
        entityGraphSuffix != null ? getEntityGraphBySuffix(entityGraphSuffix) : EntityGraph.NOOP);
    throwExceptionIfLoggedInUserCannotRead(entity);
    return entity;
  }

  /**
   * Find a game by its friendlyId, or throw an {@link UncheckedException} if not found.
   *
   * @param friendlyId The friendlyId of the entity to be retrieved.
   * @return A single entity object.
   */
  private GameEntity getByFriendlyId(@NotNull String friendlyId, EntityGraph entityGraph) throws UncheckedException {
    return repository.findByFriendlyId(friendlyId.toUpperCase(), entityGraph)
        .orElseThrow(() -> new UncheckedException(GameServiceError.NOT_FOUND_BY_FRIENDLY_ID, log::debug, friendlyId));
  }

  /**
   * Deletes all new games that have not been joined after 24 hours.
   */
  @Scheduled(cron = "0 0 */2 * * *")
  public void deleteAllNewGameNotJoinedAfter24Hours() {
    repository.deleteByBlackUserUuidNullAndUpdateDateLessThan(Instant.now().minus(Duration.ofHours(24)));
  }

  /**
   * Deletes a game by its friendlyId. Throw an {@link UncheckedException} if it has been joined by
   * another user.
   *
   * @param friendlyId The friendlyId of the entity to be deleted.
   */
  public void deleteByFriendlyId(String friendlyId) throws UncheckedException {
    GameEntity entity = getByFriendlyId(friendlyId, EntityGraph.NOOP);
    getPlayerSide(entity);
    if (entity.getBlackUserUuid() != null) {
      throw new UncheckedException(GameServiceError.CANNOT_DELETE_ALREADY_JOINED, log::debug, friendlyId);
    }
    repository.delete(entity);
  }

  /**
   * Creates a new {@link GameEntity} with the provided information.
   *
   * @param createDto Contains information needed to create a new game.
   * @return A {@link GameEntity} object.
   */
  public GameEntity create(GameCreateDto createDto) {
    Game g = new Game();
    GameEntity entity = GameEntity.builder().whiteUserUuid(identityProviderService.getLoggedInUserUuid())
        .friendlyId(generateRandomFriendlyId()).password(createDto.getPassword()).publicGame(createDto.isPublicGame())
        .gameSave(gameSaveMapper.convertToDao(g)).build();
    return repository.save(entity);
  }

  /**
   * Generates a random friendly ID by combining a random string of characters with an underscore in
   * the middle.
   *
   * @return A randomly generated friendly ID.
   */
  private String generateRandomFriendlyId() {
    return NanoIdUtils.randomNanoId(secureRandom, friendlyIdAlphabet, 3) + "-"
        + NanoIdUtils.randomNanoId(secureRandom, friendlyIdAlphabet, 3);
  }

  /**
   * Add the logged-in user to the game as the black user.
   *
   * @param updateDto Contains information needed to join a game.
   * @param friendlyId The friendlyId of the game wanting to be joined.
   * @return A {@link GameEntity} object.
   */
  public GameEntity joinGame(GameJoinUpdateDto updateDto, String friendlyId) throws UncheckedException {
    GameEntity entity = getByFriendlyId(friendlyId, EntityGraph.NOOP);
    UUID loggedInUserUuid = identityProviderService.getLoggedInUserUuid();
    if (Stream.of(entity.getWhiteUserUuid(), entity.getBlackUserUuid()).anyMatch(loggedInUserUuid::equals)) {
      throw new UncheckedException(GameServiceError.ALREADY_A_PLAYER, log::warn, entity.getFriendlyId());
    }
    if (entity.getBlackUserUuid() != null) {
      throw new UncheckedException(GameServiceError.GAME_IS_ALREADY_FULL, log::warn, entity.getFriendlyId());
    }
    if (entity.getPassword() != null && !entity.getPassword().equals(updateDto.getPassword())) {
      throw new UncheckedException(GameServiceError.WRONG_PASSWORD, log::warn, entity.getFriendlyId());
    }
    entity.setBlackUserUuid(loggedInUserUuid);
    return repository.save(entity);
  }

  /**
   * Updates the game entity based on the provided update DTO and saves the entity to the database.
   *
   * @param updateDto Contains information needed to update the game.
   * @param friendlyId The friendlyId of the game wanting to be joined.
   */
  public void gameSaveUpdate(GameUpdateBaseMessageDto updateDto, String friendlyId) {
    GameEntity entity;
    if (updateDto instanceof GameUpdateMovePieceMessageDto gameUpdateMovePieceMessageDto) {
      entity =
          getByFriendlyId(friendlyId, getEntityGraphBySuffix(GameEntity.ENTITY_GRAPH_WITH_PIECES_AND_HISTORY_SUFFIX));
      movePiece(entity, gameUpdateMovePieceMessageDto);
    } else {
      throw new UncheckedException(GameServiceError.UNSUPPORTED_UPDATE, log::error);
    }
    repository.save(entity);
  }

  /**
   * Move a piece in the provided game if the logged-in user is one of the players in the game and if
   * it is the user's turn.
   *
   * @param entity The game entity wanted to be joined.
   * @param updateDto Contains information needed to move a piece in a game.
   */
  public void movePiece(GameEntity entity, GameUpdateMovePieceMessageDto updateDto) {
    throwExceptionIfGameIsNotFull(entity);
    PieceSide loggedInUserSide = getPlayerSide(entity);
    Game game = gameSaveMapper.convertToModel(entity.getGameSave());
    Coordinate from = coordinateMapper.convertToModel(updateDto.getFrom());
    Piece pieceToMove = Piece.findPiece(game.getPieces(), null, null, from).orElseThrow(
        () -> new UncheckedException(GameServiceError.NO_PIECE_FOUND_AT_COORDINATE, log::debug, from.toString()));
    if (!pieceToMove.getSide().equals(loggedInUserSide)) {
      throw new UncheckedException(GameServiceError.NOT_YOUR_TURN, log::debug, loggedInUserSide);
    }

    movePiece(game, from, coordinateMapper.convertToModel(updateDto.getTo()),
        pieceTypeMapper.convertToModel(updateDto.getWantedPromotionPieceType()));

    entity.setGameSave(gameSaveMapper.convertToDao(game));
  }

  /**
   * Get the side of the player who is logged in and associated with a given game entity. Throws an
   * exception if the logged-in user is not a player of the provided {@link GameEntity}.
   *
   * @param entity The {@link GameEntity}.
   * @return A {@link PieceSide}.
   */
  public PieceSide getPlayerSide(GameEntity entity) {
    UUID loggedInUserUuid = identityProviderService.getLoggedInUserUuid();
    if (Stream.of(entity.getWhiteUserUuid(), entity.getBlackUserUuid()).noneMatch(loggedInUserUuid::equals)) {
      throw new UncheckedException(GameServiceError.UNAUTHORIZED_PLAY, log::warn, loggedInUserUuid,
          entity.getFriendlyId());
    }
    return identityProviderService.getLoggedInUserUuid().equals(entity.getWhiteUserUuid()) ? PieceSide.WHITE
        : PieceSide.BLACK;
  }

  /**
   * The function moves a piece in a game, handling various exceptions that may occur.
   *
   * @param game The Game model object.
   * @param from The starting coordinate of the piece that is being moved on the game board.
   * @param to The destination coordinate of the piece that is being moved on the game board.
   * @param wantedPromotionPieceType The type of piece that the pawn wants to be promoted to.
   */
  private void movePiece(Game game, Coordinate from, Coordinate to, PieceType wantedPromotionPieceType) {
    try {
      game.movePiece(from, to, wantedPromotionPieceType);
    } catch (PromotionNotAllowedException e) {
      throw new UncheckedException(GameServiceError.PROMOTION_NOT_ALLOWED, log::debug, e, e.getMessage());
    } catch (WantedPromotionPieceTypeNotProvidedException e) {
      throw new UncheckedException(GameServiceError.WANTED_PROMOTION_PIECE_TYPE_NOT_PROVIDED, log::debug, e,
          e.getMessage());
    } catch (MovementNotAllowedException e) {
      throw new UncheckedException(GameServiceError.MOVEMENT_NOT_ALLOWED, log::debug, e, e.getMessage());
    } catch (NotYourTurnException e) {
      throw new UncheckedException(GameServiceError.NOT_YOUR_TURN_ENGINE, log::debug, e, e.getMessage());
    } catch (PieceNotFoundOnGameBoardException e) {
      throw new UncheckedException(GameServiceError.NO_PIECE_FOUND_AT_COORDINATE_ENGINE, log::debug, e, e.getMessage());
    } catch (GameOverException e) {
      throw new UncheckedException(GameServiceError.GAME_IS_OVER, log::debug, e, e.getMessage());
    }
  }

  /**
   * Throws an exception if the game entity does not have both a white user UUID and a black user
   * UUID.
   *
   * @param entity The {@link GameEntity}.
   */
  public void throwExceptionIfGameIsNotFull(GameEntity entity) {
    if (entity.getWhiteUserUuid() == null || entity.getBlackUserUuid() == null) {
      throw new UncheckedException(GameServiceError.WAIT_FOR_OPPONENT_TO_JOIN, log::debug, entity.getFriendlyId());
    }
  }


  /**
   * Throws an exception if the logged-in user does not have permission to read a game entity.
   *
   * @param friendlyId The friendlyId of the game wanting to be joined.
   */
  public void throwExceptionIfLoggedInUserCannotRead(String friendlyId) {
    throwExceptionIfLoggedInUserCannotRead(getByFriendlyId(friendlyId, EntityGraph.NOOP));
  }

  /**
   * Throws an exception if the logged-in user does not have permission to read a game entity.
   *
   * @param entity The {@link GameEntity}.
   */
  private void throwExceptionIfLoggedInUserCannotRead(GameEntity entity) {
    UUID loggedInUserUuid = identityProviderService.getLoggedInUserUuid();
    if (!(entity.isPublicGame() || (loggedInUserUuid != null
        && Stream.of(entity.getWhiteUserUuid(), entity.getBlackUserUuid()).anyMatch(loggedInUserUuid::equals)))) {
      throw new UncheckedException(GameServiceError.UNAUTHORIZED_READ, log::debug, loggedInUserUuid,
          entity.getFriendlyId());
    }
  }

}
