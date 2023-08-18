package com.github.sisimomo.hexagonalchess.backend.commons.service;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.github.sisimomo.hexagonalchess.backend.commons.utils.GenericTypeResolverUtils;

import jakarta.persistence.NamedEntityGraphs;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseEntityGraphService<Entity> {

  /**
   * Get a map of EntityGraph suffix and their corresponding EntityGraph for the Entity.
   *
   * @return A map of EntityGraph.
   */
  private Map<String, EntityGraph> getEntityEntityGraphs() {
    return getEntityGraphsOfClass(GenericTypeResolverUtils.getGenericType(this.getClass(), 0));
  }

  /**
   * Get the EntityGraph of the Entity by its suffix. Using {@link #getEntityEntityGraphs}.
   *
   * @param suffix The suffix of the entity graph.
   * @return The EntityGraph object that is associated with the suffix.
   */
  public EntityGraph getEntityGraphBySuffix(@NotBlank String suffix) {
    Map<String, EntityGraph> entityEntityGraphs = getEntityEntityGraphs();
    if (entityEntityGraphs.containsKey(suffix)) {
      return entityEntityGraphs.get(suffix);
    }
    log.debug(
        "No EntityGraph with suffix \"{}\" was found on entity class \"{}\". Returning an EntityGraph that will have zero effect on queries.",
        suffix, GenericTypeResolverUtils.getGenericType(this.getClass(), 0).getCanonicalName());
    return EntityGraph.NOOP;
  }

  /**
   * It returns an instance of {@link EntityGraph} that will be used to fetch the EntityGraph with the
   * given name.
   *
   * @param name The name of the entityGraph.
   * @return A {@link EntityGraph}.
   */
  protected EntityGraph getEntityGraphFromName(String name) {
    return com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph.fetching(name);
  }

  /**
   * Get a map of EntityGraph suffix and their corresponding EntityGraph names for the provided class.
   *
   * @param clazz The class of the entity.
   * @return A map of EntityGraph names.
   */
  protected Map<String, EntityGraph> getEntityGraphsOfClass(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(NamedEntityGraphs.class)) {
      return Collections.emptyMap();
    }
    NamedEntityGraphs namedEntityGraphs = clazz.getAnnotation(NamedEntityGraphs.class);
    return Stream.of(namedEntityGraphs.value())
        .collect(Collectors.toMap(namedEntityGraph -> namedEntityGraph.name().split("-")[1],
            namedEntityGraph -> getEntityGraphFromName(namedEntityGraph.name())));
  }

}
