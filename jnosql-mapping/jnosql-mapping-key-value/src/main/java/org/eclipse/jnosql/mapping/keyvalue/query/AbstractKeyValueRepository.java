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


import jakarta.data.repository.Page;
import jakarta.data.repository.Pageable;
import jakarta.data.repository.PageableRepository;
import jakarta.nosql.keyvalue.KeyValueTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The template method to key-value repository
 */
public abstract class AbstractKeyValueRepository<T> implements PageableRepository {


    private final Class<T> type;


    protected abstract KeyValueTemplate getTemplate();

    public AbstractKeyValueRepository(Class<T> type) {
        this.type = type;
    }


    @Override
    public Object save(Object entity) {
        return getTemplate().put(entity);
    }

    @Override
    public Iterable saveAll(Iterable entities) {
        return getTemplate().put(entities);
    }

    @Override
    public void deleteById(Object key) {
        getTemplate().delete(key);
    }

    @Override
    public void deleteAllById(Iterable ids) {
        getTemplate().delete(ids);
    }

    @Override
    public Optional findById(Object key) {
        return getTemplate().get(key, type);
    }

    @Override
    public Stream findAllById(Iterable keys) {
        return StreamSupport.stream(getTemplate().get(keys, type).spliterator(), false);
    }

    @Override
    public boolean existsById(Object key) {
        return getTemplate().get(key, type).isPresent();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public Page findAll(Pageable pageable) {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public Stream findAll() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("The key-value type does not support count method");
    }

    @Override
    public void delete(Object entity) {
        Objects.requireNonNull(entity, "entity is required");
        getTemplate().delete(entity);
    }

    @Override
    public void deleteAll(Iterable entities) {
        Objects.requireNonNull(entities, "entities is required");
       getTemplate().delete(entities);
    }
}
