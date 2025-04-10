package com.romanskochko.taxi.core.service;

import com.romanskochko.taxi.core.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseService<Entity, Id> {
    private final String entityName;

    public BaseService() {
        this.entityName = entityClass().toGenericString().toLowerCase();
    }

    @Transactional(readOnly = true)
    public List<Entity> findAll() {
        return getRepository().findAll();
    }

    @Transactional(readOnly = true)
    public Entity findById(Id id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional
    public void delete(Id id) {
        getRepository().deleteById(id);
    }

    protected Entity findByIdOrThrowNotFound(Id id) {
        return getRepository()
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName));
    }

    protected abstract BaseRepository<Entity, Id> getRepository();

    protected abstract Class<Entity> entityClass();
}
