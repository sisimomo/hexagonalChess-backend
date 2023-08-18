package com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.converter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sisimomo.hexagonalchess.backend.piece.dao.entity.CoordinateJsonEntity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class CoordinateToJsonConverter implements AttributeConverter<CoordinateJsonEntity, String> {

  private ObjectMapper objectMapper;

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(CoordinateJsonEntity entity) {
    try {
      return objectMapper.writeValueAsString(entity);
    } catch (final JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public CoordinateJsonEntity convertToEntityAttribute(String json) {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, CoordinateJsonEntity.class);
    } catch (final IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
