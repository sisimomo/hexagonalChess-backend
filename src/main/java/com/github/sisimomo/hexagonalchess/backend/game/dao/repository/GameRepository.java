package com.github.sisimomo.hexagonalchess.backend.game.dao.repository;

import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity_;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity;

import jakarta.validation.constraints.NotNull;

public interface GameRepository
    extends EntityGraphJpaRepository<GameEntity, Long>, JpaSpecificationExecutor<GameEntity> {

  Optional<GameEntity> findByFriendlyId(@NotNull String friendlyId, EntityGraph entityGraph);

  @Transactional
  @Modifying
  int deleteByBlackUserUuidNullAndUpdateDateLessThan(Instant updateDate);

  // https://vladmihalcea.com/spring-data-jpa-specification/
  interface Specs {
    static Specification<GameEntity> byWhiteUserUuidOrBlackUserUuid(UUID userUuid) {
      return (root, query, builder) -> builder.or(
          builder.greaterThanOrEqualTo(root.get(GameEntity_.BLACK_USER_UUID), userUuid),
          builder.greaterThanOrEqualTo(root.get(GameEntity_.WHITE_USER_UUID), userUuid));
    }

    static Specification<GameEntity> byPasswordIsNull() {
      return (root, query, builder) -> builder.isNull(root.get(GameEntity_.PASSWORD));
    }

    static Specification<GameEntity> byBlackUserUuidIsNull() {
      return (root, query, builder) -> builder.isNull(root.get(GameEntity_.BLACK_USER_UUID));
    }

  }

}
