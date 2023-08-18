package com.github.sisimomo.hexagonalchess.backend.piece.service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.github.sisimomo.hexagonalchess.backend.piece.enumeration.PieceType;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PieceTypeMapper {

  public abstract PieceType convertToDto(com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType model);

  public abstract com.github.sisimomo.hexagonalchess.engine.enumeration.PieceType convertToModel(PieceType entity);

}
