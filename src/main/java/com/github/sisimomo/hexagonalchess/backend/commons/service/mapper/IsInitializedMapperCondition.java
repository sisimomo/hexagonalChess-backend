package com.github.sisimomo.hexagonalchess.backend.commons.service.mapper;

import org.hibernate.Hibernate;
import org.mapstruct.Condition;
import org.springframework.stereotype.Component;

@Component
public class IsInitializedMapperCondition {

  @Condition
  public boolean isInitialized(Object sourceField) {
    return Hibernate.isInitialized(sourceField);
  }

}
