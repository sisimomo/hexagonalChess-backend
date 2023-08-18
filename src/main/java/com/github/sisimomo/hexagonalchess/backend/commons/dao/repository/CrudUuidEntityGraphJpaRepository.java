package com.github.sisimomo.hexagonalchess.backend.commons.dao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.github.sisimomo.hexagonalchess.backend.commons.dao.entity.IBaseUuidEntity;

import jakarta.validation.constraints.NotNull;

@NoRepositoryBean
public interface CrudUuidEntityGraphJpaRepository<T extends IBaseUuidEntity, ID>
    extends EntityGraphJpaRepository<T, ID> {

  Optional<T> findByUuid(@NotNull UUID uuid, EntityGraph entityGraph);

  Optional<T> findByUuid(@NotNull UUID uuid);

  boolean existsByUuid(@NotNull UUID uuid);

  long deleteByUuid(UUID uuid);

}
