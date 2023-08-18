package com.github.sisimomo.hexagonalchess.backend.commons.dao.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseUuidEntity implements IBaseUuidEntity {

  /**
   * A column representing the uuid of the entity, that is 16 bytes long, cannot be null, and cannot
   * be updated.
   */
  @Column(name = "uuid", nullable = false, updatable = false)
  protected UUID uuid;

  /**
   * Generates a random UUID when the entity is persisted for the first.
   */
  @PrePersist
  protected void onCreate() {
    this.uuid = UUID.randomUUID();
  }

}
