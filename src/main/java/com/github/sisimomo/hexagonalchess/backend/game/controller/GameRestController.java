package com.github.sisimomo.hexagonalchess.backend.game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.sisimomo.hexagonalchess.backend.commons.controller.BaseRestController;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.Direction;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.WindowDto;
import com.github.sisimomo.hexagonalchess.backend.commons.service.KeysetPaginationService;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity;
import com.github.sisimomo.hexagonalchess.backend.game.service.GameService;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.GameCreateDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.request.GameJoinUpdateDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.GameDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.message.GameJoinMessageDto;
import com.github.sisimomo.hexagonalchess.backend.game.service.mapper.GameMapper;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.IdentityProviderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Game", description = "Game endpoints")
@RequestMapping("/game")
public class GameRestController extends BaseRestController {

  private final GameMapper mapper;

  private final GameService service;

  private final KeysetPaginationService keysetPaginationService;

  private final IdentityProviderService identityProviderService;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Operation(summary = "Return detailed information about a specific game")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @GetMapping("/{friendlyId}")
  public ResponseEntity<GameDto> get(@PathVariable String friendlyId) {
    return new ResponseEntity<>(mapper.convertToDto(
        service.getByFriendlyIdAndCheckPermission(friendlyId, GameEntity.ENTITY_GRAPH_WITH_PIECES_AND_HISTORY_SUFFIX)),
        HttpStatus.OK);
  }

  @Operation(summary = "Return information of all Games of the logged-in user, paged")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @GetMapping("/logged_in_user")
  public ResponseEntity<WindowDto<GameDto>> getAllLoggedInUserGames(@RequestParam(required = false) String cursor,
      @RequestParam @Min(1) @Max(50) int maxResults, @RequestParam Direction direction) {
    return new ResponseEntity<>(keysetPaginationService.windowToDto(
        service.getAllLoggedInUserGames(cursor, maxResults, direction).map(mapper::convertToDto)), HttpStatus.OK);
  }

  @Operation(summary = "Return information of all ready to join Games, paged")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @GetMapping("/ready_to_join")
  public ResponseEntity<WindowDto<GameDto>> getAllPasswordlessGamesMissingPlayer(
      @RequestParam(required = false) String cursor, @RequestParam @Min(1) @Max(50) int maxResults,
      @RequestParam Direction direction) {
    return new ResponseEntity<>(
        keysetPaginationService.windowToDto(
            service.getAllPasswordlessGamesMissingPlayer(cursor, maxResults, direction).map(mapper::convertToDto)),
        HttpStatus.OK);
  }

  @Operation(summary = "Create a game")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successful operation")})
  @PostMapping("")
  public ResponseEntity<GameDto> create(@Valid @RequestBody GameCreateDto createDto) {
    return new ResponseEntity<>(mapper.convertToDto(service.create(createDto)), HttpStatus.CREATED);
  }

  @Operation(summary = "Join an existing game")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @PutMapping("/{friendlyId}")
  public ResponseEntity<GameDto> update(@PathVariable String friendlyId,
      @Valid @RequestBody GameJoinUpdateDto updateDTO) {
    GameEntity entity = service.joinGame(updateDTO, friendlyId);
    simpMessagingTemplate.convertAndSend("/topic/game/" + friendlyId,
        GameJoinMessageDto.builder().emitter(identityProviderService.getLoggedInUserUuid()).build());
    return new ResponseEntity<>(mapper.convertToDto(entity), HttpStatus.OK);
  }

  @Operation(summary = "Delete an existing game")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @DeleteMapping("/{friendlyId}")
  public ResponseEntity<Void> delete(@PathVariable String friendlyId) {
    service.deleteByFriendlyId(friendlyId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
