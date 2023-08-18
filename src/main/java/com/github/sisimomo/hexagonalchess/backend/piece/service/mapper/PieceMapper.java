package com.github.sisimomo.hexagonalchess.backend.piece.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.PieceJsonEntity;
import com.github.sisimomo.hexagonalchess.backend.piece.service.dto.response.PieceDto;
import com.github.sisimomo.hexagonalchess.engine.piece.Piece;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {CoordinateMapper.class, PieceSideMapper.class, PieceTypeMapper.class})
public abstract class PieceMapper {

  @Autowired
  private CoordinateMapper coordinateMapper;

  @Autowired
  private PieceSideMapper pieceSideMapper;

  @Autowired
  private PieceTypeMapper pieceTypeMapper;


  public List<List<PieceDto>> convertToDto(List<List<PieceJsonEntity>> model) {
    return model != null ? model.stream().map(m -> m.stream().map(this::convertToDto).collect(Collectors.toList()))
        .collect(Collectors.toList()) : null;
  }

  public abstract PieceDto convertToDto(PieceJsonEntity entity);

  public abstract PieceDto convertToDto(Piece model);

  public abstract PieceJsonEntity convertToDao(PieceDto dto);

  public abstract PieceJsonEntity convertToDao(Piece model);

  public Piece convertToModel(PieceJsonEntity entity) {
    return Piece.createPiece(pieceTypeMapper.convertToModel(entity.getType()),
        pieceSideMapper.convertToModel(entity.getSide()), coordinateMapper.convertToModel(entity.getCoordinate()));
  }

  public List<List<Piece>> convertToModel(List<List<PieceJsonEntity>> model) {
    return model != null ? model.stream().map(m -> m.stream().map(this::convertToModel).collect(Collectors.toList()))
        .collect(Collectors.toList()) : null;
  }

  public Piece convertToModel(PieceDto dto) {
    return Piece.createPiece(pieceTypeMapper.convertToModel(dto.getType()),
        pieceSideMapper.convertToModel(dto.getSide()), coordinateMapper.convertToModel(dto.getCoordinate()));
  }

  public List<List<PieceJsonEntity>> convertToDao(List<List<Piece>> model) {
    return model != null ? model.stream().map(m -> m.stream().map(this::convertToDao).collect(Collectors.toList()))
        .collect(Collectors.toList()) : null;
  }

}
