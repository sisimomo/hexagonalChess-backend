package com.github.sisimomo.hexagonalchess.backend.commons.controller;

import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.ErrorResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
@ApiResponse(responseCode = "error", description = "An error occurred",
    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class BaseRestController {

}
