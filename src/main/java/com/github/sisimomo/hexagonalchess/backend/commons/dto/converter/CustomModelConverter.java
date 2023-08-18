package com.github.sisimomo.hexagonalchess.backend.commons.dto.converter;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Locale;

import com.fasterxml.jackson.databind.type.SimpleType;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

/**
 * Custom ModelConverter that create a custom schema for {@link LocalTime}, {@link ZoneOffset} and
 * {@link Locale}.
 * <p>
 * <a href="https://stackoverflow.com/a/73966705/20452711"><i>Source</i></a>
 */
public class CustomModelConverter implements ModelConverter {
  private static final Schema SCHEMA_LOCAL_TIME = new Schema().type("string").format("time").example("12:59:59");
  private static final Schema SCHEMA_OFFSET_TIME =
      new Schema().type("string").format("offsetTime").example("12:59:59.99Z");
  private static final Schema SCHEMA_ZONE_ID =
      new Schema().type("string").format("timezone_id").example("America/New_York");
  private static final Schema SCHEMA_LOCALE = new Schema().type("string").format("Locale").example("fr-ca");

  @Override
  public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
    Type type = annotatedType.getType();
    if (type instanceof SimpleType simpleType) {

      if (simpleType.getRawClass().isAssignableFrom(LocalTime.class)) {
        return SCHEMA_LOCAL_TIME;
      } else if (simpleType.getRawClass().isAssignableFrom(OffsetTime.class)) {
        return SCHEMA_OFFSET_TIME;
      } else if (simpleType.getRawClass().isAssignableFrom(ZoneId.class)) {
        return SCHEMA_ZONE_ID;
      } else if (simpleType.getRawClass().isAssignableFrom(Locale.class)) {
        return SCHEMA_LOCALE;
      }
    }

    // It's needed to follow chain for unresolved types
    if (chain.hasNext()) {
      return chain.next().resolve(annotatedType, context, chain);
    }
    return null;
  }

}
