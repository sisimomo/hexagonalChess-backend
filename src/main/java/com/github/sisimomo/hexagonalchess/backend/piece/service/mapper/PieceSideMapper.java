package com.github.sisimomo.hexagonalchess.backend.piece.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceSide;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PieceSideMapper {

  public abstract PieceSide convertToDto(com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide model);

  public abstract com.github.sisimomo.hexagonalchess.engine.enumeration.PieceSide convertToModel(PieceSide entity);

}
