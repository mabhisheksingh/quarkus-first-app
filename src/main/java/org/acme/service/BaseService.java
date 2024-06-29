package org.acme.service;

import jakarta.enterprise.context.Dependent;
import org.acme.model.BaseModel;
import org.acme.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

@Dependent
public interface BaseService<Entity extends BaseModel, Id extends Long> extends BaseRepository<Entity, Id> {
    default void addUser(Entity entity) {
        persistAndFlush(entity);
    }
    default void addBulkUser(List<Entity> entity) {
        persist(entity);
    }

    default Boolean isExist(Entity entity) {
        return isPersistent(entity);
    }

    default List<Entity> getAll() {
        return findAll().stream().parallel().toList();
    }

    default Boolean removeById(Id id) {
        return deleteById(id);
    }

    default Optional<Entity> getByIdOptional(Id id) {
        return findByIdOptional(id);
    }

    default Optional<Entity> getByEmailOptional(String email) {
        return find("email", email).firstResultOptional();
    }

    //One method for pagination
}
