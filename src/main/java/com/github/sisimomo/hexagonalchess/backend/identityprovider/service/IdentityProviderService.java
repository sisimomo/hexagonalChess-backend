package com.github.sisimomo.hexagonalchess.backend.identityprovider.service;

import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.error.IdentityProviderServiceError;
import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.mapper.UserMapper;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityProviderService {

  private final Keycloak keycloakService;
  private final UserMapper mapper;
  @Value("${keycloak.realm}")
  private String keycloakRealm;

  /**
   * Searches for users by their email in a Keycloak realm.
   *
   * @param email The email to search for.
   * @param exact Determines whether the search for email should be exact or not.
   * @param offset Specify the starting index of the search results.
   * @param maxResults The maximum number of results to be returned in the search.
   * @return A List of {@link UserRepresentation} objects.
   */
  public List<UserRepresentation> searchByEmail(String email, boolean exact, int offset, int maxResults) {
    return keycloakService.realm(keycloakRealm).users().search(null, null, null, email, offset, maxResults, true, null,
        exact);
  }

  /**
   * Retrieves a user by their UUID from a Keycloak realm.
   *
   * @param uuid The uuid of the user.
   * @return A {@link UserRepresentation} object.
   */
  public UserRepresentation getByUid(UUID uuid) {
    List<UserRepresentation> list = keycloakService.realm(keycloakRealm).users().search("id:" + uuid.toString(), 0, 1);
    if (list.isEmpty()) {
      throw new UncheckedException(IdentityProviderServiceError.NOT_FOUND_BY_UUID, log::warn, uuid);
    }
    return list.get(0);
  }

  /**
   * Retrieves the currently logged-in user.
   *
   * @return A {@link UserRepresentation} object.
   */
  public UserRepresentation getLoggedInUser() {
    return getByUid(getLoggedInUserUuid());
  }

  public void setLoggedInUser(JwtAuthenticationToken jwtAuthenticationToken) {
    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
  }

  /**
   * Retrieves the UUID of the currently logged-in user from the authentication context.
   *
   * @return The UUID of the logged-in user.
   */
  public UUID getLoggedInUserUuid() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || (auth.getPrincipal() instanceof String strPrincipal && strPrincipal.equals("anonymousUser"))) {
      return null;
    }
    String uuidString = ((Jwt) auth.getPrincipal()).getSubject();
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      log.error("Auth provider user id is not a uuid");
      return null;
    }
  }

}
