package com.github.sisimomo.hexagonalchess.backend.commons.service;

import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.WindowDto;
import java.util.Base64;
import java.util.Map;

import javax.crypto.SecretKey;

import org.hibernate.type.descriptor.java.spi.JdbcTypeRecommendationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.Direction;
import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;
import com.github.sisimomo.hexagonalchess.backend.commons.service.error.KeysetPaginationServiceError;
import com.github.sisimomo.hexagonalchess.backend.commons.utils.AesGcmUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeysetPaginationService {
  private final SecretKey key;
  private final ObjectMapper objectMapper;

  public KeysetPaginationService(@Value("${db.encryption.key}") String encodedKey, ObjectMapper objectMapper) {
    this.key = AesGcmUtils.stringToSecretKey(encodedKey);
    this.objectMapper = objectMapper;
  }

  /**
   * Converts a {@link KeysetScrollPosition} object to a JSON encrypted encoded Base64 String.
   *
   * @param position The {@link KeysetScrollPosition} object to be converted.
   * @return A JSON encrypted encoded Base64 String.
   */
  public String keysetScrollPositionToEncryptedString(KeysetScrollPosition position) {
    try {
      String serialized = objectMapper.writeValueAsString(position.getKeys());
      return new String(Base64.getEncoder().encode(AesGcmUtils.encrypt(serialized, key)));
    } catch (Exception e) {
      throw new UncheckedException(KeysetPaginationServiceError.ENCRYPTION_ERROR, log::warn, e);
    }
  }

  /**
   * Converts JSON encrypted decoded Base64 String to a {@link KeysetScrollPosition} object.
   *
   * @param encryptedString The encrypted string that needs to be decrypted and converted to a
   *        {@link KeysetScrollPosition} object.
   * @param direction Specifies the direction of scrolling, either FORWARD or BACKWARD.
   * @return A {@link KeysetScrollPosition} object.
   */
  private KeysetScrollPosition encryptedStringToKeysetScrollPosition(String encryptedString, Direction direction) {
    if (encryptedString == null) {
      return ScrollPosition.keyset();
    }
    try {
      String serialized = AesGcmUtils.decrypt(Base64.getDecoder().decode(encryptedString), key);
      TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
      return ScrollPosition.of(objectMapper.readValue(serialized, typeRef),
          direction.equals(Direction.FORWARD) ? ScrollPosition.Direction.FORWARD : ScrollPosition.Direction.BACKWARD);
    } catch (Exception e) {
      throw new UncheckedException(KeysetPaginationServiceError.DECRYPTION_ERROR, log::warn, e, encryptedString);
    }
  }

  /**
   * Retrieves a {@link Window} of entities from a JPA repository based on provided specification,
   * sorting, and pagination parameters.
   *
   * @param repository A JPA repository that extends JpaSpecificationExecutor interface, which
   *        provides methods for querying the database using specifications.
   * @param specification The criteria for filtering the entities in the repository.
   * @param sort The criteria for sorting the entities in the repository.
   * @param cursor The scroll request cursor.
   * @param maxResults The maximum number of results to be returned in the window.
   * @param direction The direction of the scrolling.
   * @return The returning {@link Window<EntityT>} object.
   */
  public <EntityT> Window<EntityT> getAll(JpaSpecificationExecutor<EntityT> repository,
      Specification<EntityT> specification, Sort sort, String cursor, int maxResults, Direction direction) {
    KeysetScrollPosition scrollPosition = encryptedStringToKeysetScrollPosition(cursor, direction);
    return repository.findBy(specification, q -> {
      try {
        return q.limit(maxResults).sortBy(sort).scroll(scrollPosition);
      } catch (JdbcTypeRecommendationException e) {
        throw new UncheckedException(KeysetPaginationServiceError.BAD_FILTER, log::warn, e);
      }
    });
  }

  /**
   * Converts the content of a {@link Window} object to a {@link WindowDto} object.
   *
   * @param window The {@link Window} object to be converted.
   * @return A {@link WindowDto} object.
   */
  public <T> WindowDto<T> windowToDto(Window<T> window) {
    String startCursor = null;
    String endCursor = null;
    if (!window.getContent().isEmpty()) {
      startCursor =
          keysetScrollPositionToEncryptedString((KeysetScrollPosition) window.positionAt(window.getContent().get(0)));
      endCursor = keysetScrollPositionToEncryptedString(
          (KeysetScrollPosition) window.positionAt(window.getContent().get(window.getContent().size() - 1)));
    }
    return new WindowDto<>(window.getContent(), window.hasNext(), startCursor, endCursor);
  }

}
