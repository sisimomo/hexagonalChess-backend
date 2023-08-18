package com.github.sisimomo.hexagonalchess.backend.game.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.github.sisimomo.hexagonalchess.backend.commons.service.mapper.CentralJpaEntityMapperConfig;
import com.github.sisimomo.hexagonalchess.backend.game.dao.entity.GameEntity;
import com.github.sisimomo.hexagonalchess.backend.game.service.dto.response.GameDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Mapper(config = CentralJpaEntityMapperConfig.class, uses = {GameSaveMapper.class})
public abstract class GameMapper {

  public abstract List<GameDto> convertToDto(List<GameEntity> entities);

  public abstract GameDto convertToDto(GameEntity entity);

}
