package com.github.sisimomo.hexagonalchess.backend.configuration;

import java.io.Serial;
import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;

public class ImplicitNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

  @Serial
  private static final long serialVersionUID = -328055100801826055L;

  @Override
  public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
    Identifier userProvidedIdentifier = source.getUserProvidedIdentifier();
    return userProvidedIdentifier != null ? userProvidedIdentifier
        : toIdentifier(
            String.join("_", "FK", source.getTableName().getText(),
                source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.joining("_"))),
            source.getBuildingContext());
  }

  @Override
  public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
    Identifier userProvidedIdentifier = source.getUserProvidedIdentifier();
    return userProvidedIdentifier != null ? userProvidedIdentifier
        : toIdentifier(
            String.join("_", "UK", source.getTableName().getText(),
                source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.joining("_"))),
            source.getBuildingContext());
  }

  @Override
  public Identifier determineIndexName(ImplicitIndexNameSource source) {
    Identifier userProvidedIdentifier = source.getUserProvidedIdentifier();
    return userProvidedIdentifier != null ? userProvidedIdentifier
        : toIdentifier(
            String.join("_", "IDX", source.getTableName().getText(),
                source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.joining("_"))),
            source.getBuildingContext());
  }

}
