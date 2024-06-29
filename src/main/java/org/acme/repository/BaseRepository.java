package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public interface BaseRepository<Entity, Id> extends PanacheRepositoryBase<Entity, Id> {
}
