package com.github.sisimomo.hexagonalchess.backend.piece.service.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.CoordinateJsonEntity;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.CoordinateDto;
import com.github.sisimomo.hexagonalchess.engine.Coordinate;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CoordinateMapper {

  public abstract CoordinateDto convertToDto(CoordinateJsonEntity entity);

  public abstract CoordinateDto convertToDto(Coordinate model);

  public abstract CoordinateJsonEntity convertToDao(CoordinateDto dto);

  public abstract CoordinateJsonEntity convertToDao(Coordinate model);

  public Pair<CoordinateJsonEntity, CoordinateJsonEntity> convertToDao(Pair<Coordinate, Coordinate> value) {
    return Pair.of(convertToDao(value.getLeft()), convertToDao(value.getRight()));
  }

  @Mapping(target = "add", ignore = true)
  @Mapping(target = "subtract", ignore = true)
  @Mapping(target = "multiply", ignore = true)
  public abstract Coordinate convertToModel(CoordinateJsonEntity entity);

  @Mapping(target = "add", ignore = true)
  @Mapping(target = "subtract", ignore = true)
  @Mapping(target = "multiply", ignore = true)
  public abstract Coordinate convertToModel(CoordinateDto dto);

}
