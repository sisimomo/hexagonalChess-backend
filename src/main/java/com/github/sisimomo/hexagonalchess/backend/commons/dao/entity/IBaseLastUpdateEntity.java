package com.github.sisimomo.hexagonalchess.backend.commons.dao.entity;

import java.time.Instant;

public interface IBaseLastUpdateEntity {

  Instant getCreateDate();

  Instant getUpdateDate();

}
