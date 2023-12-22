/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.keyvalue.query;


import jakarta.data.page.Page;
import jakarta.data.page.Pageable;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.PageableRepository;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;
import static org.eclipse.jnosql.mapping.IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;

/**
 * The template method to key-value repository
 */
public abstract class AbstractKeyValueRepository<T, K> implements PageableRepository<T, K>, CrudRepository<T, K> {


    private final Class<T> type;


    protected abstract KeyValueTemplate getTemplate();

    protected abstract EntityMetadata getEntityMetadata();

    public AbstractKeyValueRepository(Class<T> type) {
        this.type = type;
    }


    @Override
    public <S extends T> S save(S entity) {
        return getTemplate().put(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return getTemplate().put(entities);
    }

    @Override
    public void deleteById(K key) {
        getTemplate().delete(key);
    }

    @Override
    public void deleteByIdIn(Iterable<K> ids) {
        getTemplate().delete(ids);
    }

    @Override
    public Optional<T> findById(K id) {
        return getTemplate().get(id, type);
    }

    @Override
    public Stream<T> findByIdIn(Iterable<K> ids) {
        return StreamSupport.stream(getTemplate().get(ids, type).spliterator(), false);
    }

    @Override
    public boolean existsById(K id) {
        return getTemplate().get(id, type).isPresent();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public Stream<T> findAll() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public void delete(Object entity) {
        Objects.requireNonNull(entity, "entity is required");
        FieldMetadata id = getEntityMetadata().id().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
        Object value = id.read(entity);
        getTemplate().delete(value);
    }

    @Override
    public void deleteAll(Iterable entities) {
        Objects.requireNonNull(entities, "entities is required");
        EntityMetadata metadata = getEntityMetadata();
        FieldMetadata id = metadata.id().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
        List<Object> ids = stream(entities.spliterator(), false).map(id::read).toList();
        getTemplate().delete(ids);
    }

    @Override
    public <S extends T> S insert(S entity) {
        Objects.requireNonNull(entity, "entity is required");
        return getTemplate().insert(entity);
    }

    @Override
    public <S extends T> Iterable<S> insertAll(Iterable<S> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return getTemplate().insert(entities);
    }

    @Override
    public boolean update(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        return getTemplate().update(entity) != null;
    }

    @Override
    public int updateAll(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        getTemplate().update(entities);
        return (int) StreamSupport.stream(entities.spliterator(), false).count();
    }
}
