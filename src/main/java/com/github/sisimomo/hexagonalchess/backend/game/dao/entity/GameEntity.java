package com.github.sisimomo.hexagonalchess.backend.game.dao.entity;

import java.util.UUID;

import com.github.sisimomo.hexagonalchess.backend.commons.dao.entity.BaseLongAndLastUpdateEntity;
import com.github.sisimomo.hexagonalchess.backend.commons.dao.entity.converter.StringToEncryptedBytesConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraphs(value = {
    @NamedEntityGraph(
        name = GameEntity.ENTITY_GRAPH_BASE_NAME + "-" + GameEntity.ENTITY_GRAPH_WITH_PIECES_AND_HISTORY_SUFFIX,
        attributeNodes = {@NamedAttributeNode(value = GameEntity_.GAME_SAVE,
            subgraph = GameEntity.ENTITY_GRAPH_BASE_NAME + "-" + GameEntity_.GAME_SAVE)},
        subgraphs = {@NamedSubgraph(name = GameEntity.ENTITY_GRAPH_BASE_NAME + "-" + GameEntity_.GAME_SAVE,
            attributeNodes = {@NamedAttributeNode(GameSaveEntity_.PIECES),
                @NamedAttributeNode(GameSaveEntity_.HISTORY)})}),
    @NamedEntityGraph(
        name = GameEntity.ENTITY_GRAPH_BASE_NAME + "-" + GameEntity.ENTITY_GRAPH_WITHOUT_PIECES_AND_HISTORY_SUFFIX,
        attributeNodes = {@NamedAttributeNode(GameEntity_.GAME_SAVE)})})
@Entity
@Table(name = "game")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameEntity extends BaseLongAndLastUpdateEntity {
  public static final String ENTITY_GRAPH_BASE_NAME =
      "com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity";
  public static final String ENTITY_GRAPH_WITH_PIECES_AND_HISTORY_SUFFIX = "withPiecesAndHistory";
  public static final String ENTITY_GRAPH_WITHOUT_PIECES_AND_HISTORY_SUFFIX = "withoutPiecesAndHistory";

  @NotNull
  @Size(max = 7)
  private String friendlyId;

  @Column(columnDefinition = "tinyblob")
  @Convert(converter = StringToEncryptedBytesConverter.class)
  private String password;

  @NotNull
  private UUID whiteUserUuid;

  private UUID blackUserUuid;

  private boolean publicGame;

  @NotNull
  // A OneToOne field (non-owning side) is always lazily loaded, so to allow control, I used
  // ManyToOne.
  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private @Valid GameSaveEntity gameSave;

}
