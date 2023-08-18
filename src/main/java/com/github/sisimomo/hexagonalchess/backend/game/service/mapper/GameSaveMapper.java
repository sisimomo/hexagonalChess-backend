package com.github.sisimomo.hexagonalchess.backend.game.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.sisimomo.hexagonalchess.backend.commons.service.mapper.CentralJpaEntityMapperConfig;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameSaveEntity;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.GameSaveDto;
import com.github.sisimomo.hexagonalchess.backend.piece.service.mapper.CoordinateMapper;
import com.github.sisimomo.hexagonalchess.backend.piece.service.mapper.PieceMapper;
import com.github.sisimomo.hexagonalchess.backend.piece.service.mapper.PieceSideMapper;
import com.github.sisimomo.hexagonalchess.engine.Game;

@Mapper(config = CentralJpaEntityMapperConfig.class,
    uses = {CoordinateMapper.class, PieceSideMapper.class, PieceMapper.class})
public abstract class GameSaveMapper {

  // IsInitializedMapperCondition only work on xTox relations.
  @Mapping(target = "pieces",
      conditionExpression = "java(jakarta.persistence.Persistence.getPersistenceUtil().isLoaded(entity, \"pieces\"))")
  @Mapping(target = "history",
      conditionExpression = "java(jakarta.persistence.Persistence.getPersistenceUtil().isLoaded(entity, \"history\"))")
  public abstract GameSaveDto convertToDto(GameSaveEntity entity);

  @Mapping(target = "lastMoveFrom",
      expression = "java(model.getLastMove() != null ? coordinateMapper.convertToDto(model.getLastMove().getLeft()) : null)")
  @Mapping(target = "lastMoveTo",
      expression = "java(model.getLastMove() != null ? coordinateMapper.convertToDto(model.getLastMove().getRight()) : null)")
  public abstract GameSaveDto convertToDto(Game model);

  @Mapping(target = "lastMoveFrom",
      expression = "java(model.getLastMove() != null ? coordinateMapper.convertToDao(model.getLastMove().getLeft()) : null)")
  @Mapping(target = "lastMoveTo",
      expression = "java(model.getLastMove() != null ? coordinateMapper.convertToDao(model.getLastMove().getRight()) : null)")
  public abstract GameSaveEntity convertToDao(Game model);

  @Mapping(target = "lastMove",
      expression = "java(entity.getLastMoveFrom() != null ? org.apache.commons.lang3.tuple.Pair.of(coordinateMapper.convertToModel(entity.getLastMoveFrom()), coordinateMapper.convertToModel(entity.getLastMoveTo())) : null)")
  public abstract Game convertToModel(GameSaveEntity entity);

}
