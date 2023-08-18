package com.github.sisimomo.hexagonalchess.backend.identityprovider.service.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.sisimomo.hexagonalchess.backend.identityprovider.service.dto.UserDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {

  @Mapping(target = "uuid", expression = "java(java.util.UUID.fromString(entity.getId()))")
  public abstract UserDto convertToDto(UserRepresentation entity);

}
