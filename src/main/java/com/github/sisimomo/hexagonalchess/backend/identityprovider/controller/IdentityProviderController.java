package com.github.sisimomo.hexagonalchess.backend.identityprovider.controller;

import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.IdentityProviderService;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.dto.UserDto;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.mapper.UserMapper;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.sisimomo.hexagonalchess.backend.commons.controller.BaseRestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "Identity Provider", description = "Identity Provider endpoints")
@RequestMapping("/identity_provider")
public class IdentityProviderController extends BaseRestController {

  private final UserMapper mapper;

  private final IdentityProviderService service;

  @Operation(summary = "Search users by their email address paged")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
  @GetMapping("/search/email")
  public ResponseEntity<List<UserDto>> searchByEmail(@RequestParam @Size(min = 4, max = 320) String email,
      @RequestParam(defaultValue = "true") boolean exact, @RequestParam @Min(0) Integer offset,
      @Min(1) @Max(50) Integer maxResults) {
    return new ResponseEntity<>(
        service.searchByEmail(email, exact, offset, maxResults).stream().map(mapper::convertToDto).toList(),
        HttpStatus.OK);
  }

}
