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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static jakarta.nosql.mapping.IdNotFoundException.KEY_NOT_FOUND_EXCEPTION_SUPPLIER;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

abstract class AbstractGraphRepository<T, K> implements Repository<T, K> {

    protected abstract GraphTemplate getTemplate();

    protected abstract EntityMetadata getEntityMetadata();


    @Override
    public <S extends T> S save(S entity) {
        Objects.requireNonNull(entity, "Entity is required");
        Object id = getIdField().read(entity);
        if (nonNull(id) && existsById((K) id)) {
            return getTemplate().update(entity);
        } else {
            return getTemplate().insert(entity);
        }
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::save).collect(toList());
    }

    @Override
    public void deleteById(K id) {
        requireNonNull(id, "is is required");
        getTemplate().delete(id);
    }

    @Override
    public void deleteById(Iterable<K> ids) {
        requireNonNull(ids, "ids is required");
        ids.forEach(this::deleteById);
    }

    @Override
    public Optional<T> findById(K id) {
        requireNonNull(id, "id is required");
        return getTemplate().find(id);
    }

    @Override
    public Iterable<T> findById(Iterable<K> ids) {
        requireNonNull(ids, "ids is required");
        return (Iterable) stream(ids.spliterator(), false)
                .flatMap(optionalToStream()).collect(toList());
    }

    @Override
    public boolean existsById(K id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        return getTemplate().count(getEntityMetadata().getName());
    }

    private FieldMapping getIdField() {
        return getEntityMetadata().getId().orElseThrow(KEY_NOT_FOUND_EXCEPTION_SUPPLIER);
    }

    private Function optionalToStream() {
        return id -> {
            Optional entity = this.findById((K) id);
            return entity.isPresent() ? Stream.of(entity.get()) : Stream.empty();
        };
    }
}
