package com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.converter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.PieceJsonEntity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class PieceListListToJsonConverter implements AttributeConverter<List<List<PieceJsonEntity>>, String> {

  private ObjectMapper objectMapper;

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(List<List<PieceJsonEntity>> entity) {
    try {
      return objectMapper.writeValueAsString(entity);
    } catch (final JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<List<PieceJsonEntity>> convertToEntityAttribute(String json) {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, new TypeReference<List<List<PieceJsonEntity>>>() {});
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
