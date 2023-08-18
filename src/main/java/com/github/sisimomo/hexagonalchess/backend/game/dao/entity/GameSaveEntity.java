package com.github.sisimomo.hexagonalchess.backend.game.dao.entity;

import java.util.List;

import com.github.sisimomo.hexagonalchess.backend.commons.dao.entity.BaseLongEntity;
import com.github.sisimomo.hexagonalchess.backend.game.enumeration.GameState;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.CoordinateJsonEntity;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.PieceJsonEntity;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.converter.CoordinateToJsonConverter;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.converter.PieceListListToJsonConverter;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.converter.PieceListToJsonConverter;
import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceSide;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_save")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameSaveEntity extends BaseLongEntity {

  @NotNull
  @Enumerated
  private GameState state;

  @NotNull
  @Enumerated
  private PieceSide sideTurn;

  @Valid
  @Convert(converter = CoordinateToJsonConverter.class)
  private CoordinateJsonEntity lastMoveFrom;

  @Valid
  @Convert(converter = CoordinateToJsonConverter.class)
  private CoordinateJsonEntity lastMoveTo;

  @Valid
  @NotEmpty
  @Basic(fetch = FetchType.LAZY)
  @Column(columnDefinition = "json")
  @Convert(converter = PieceListToJsonConverter.class)
  private List<@NotNull @Valid PieceJsonEntity> pieces;

  @Valid
  @NotNull
  @Basic(fetch = FetchType.LAZY)
  @Column(columnDefinition = "json")
  @Convert(converter = PieceListListToJsonConverter.class)
  private List<@NotEmpty List<@NotNull @Valid PieceJsonEntity>> history;

}
