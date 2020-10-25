/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.keyvalue;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.keyvalue.KeyValueEntityConverter;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.mapping.keyvalue.KeyValueWorkflow;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * This class provides a skeletal implementation of the {@link KeyValueTemplate} interface,
 * to minimize the effort required to implement this interface.
 */
public abstract class AbstractKeyValueTemplate implements KeyValueTemplate {

    protected abstract KeyValueEntityConverter getConverter();

    protected abstract BucketManager getManager();


    protected abstract KeyValueWorkflow getFlow();

    @Override
    public <T> T put(T entity) {
        requireNonNull(entity, "entity is required");

        UnaryOperator<KeyValueEntity> putAction = k -> {
            getManager().put(k);
            return k;

        };
        return getFlow().flow(entity, putAction);
    }

    @Override
    public <T> T put(T entity, Duration ttl) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl class is required");

        UnaryOperator<KeyValueEntity> putAction = k -> {
            getManager().put(k, ttl);
            return k;

        };
        return getFlow().flow(entity, putAction);
    }

    @Override
    public <K, T> Optional<T> get(K key, Class<T> entityClass) {
        requireNonNull(key, "key is required");
        requireNonNull(entityClass, "entity class is required");

        Optional<Value> value = getManager().get(key);
        return value.map(v -> getConverter().toEntity(entityClass, KeyValueEntity.of(key, v)))
                .filter(Objects::nonNull);
    }

    @Override
    public <K, T> Iterable<T> get(Iterable<K> keys, Class<T> entityClass) {
        requireNonNull(keys, "keys is required");
        requireNonNull(entityClass, "entity class is required");
        return StreamSupport.stream(keys.spliterator(), false)
                .map(k -> getManager().get(k)
                        .map(v -> KeyValueEntity.of(k, v)))
                .filter(Optional::isPresent)
                .map(e -> getConverter().toEntity(entityClass, e.get()))
                .collect(Collectors.toList());
    }

    @Override
    public <K> void delete(K key) {
        requireNonNull(key, "key is required");
        getManager().delete(key);
    }

    @Override
    public <K> void delete(Iterable<K> keys) {
        requireNonNull(keys, "keys is required");
        getManager().delete(keys);
    }

    @Override
    public <T> Stream<T> query(String query, Class<T> entityClass) {
        requireNonNull(query, "query is required");
        requireNonNull(entityClass, "entityClass is required");
        Stream<Value> values = getManager().query(query);
            return values.map(v -> v.get(entityClass));
    }

    @Override
    public <T> Optional<T> getSingleResult(String query, Class<T> entityClass) {
        requireNonNull(query, "query is required");
        requireNonNull(entityClass, "entityClass is required");

        Stream<T> entities = query(query, entityClass);
        final Iterator<T> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("No Unique result found to the query: " + query);
    }

    @Override
    public void query(String query) {
        requireNonNull(query, "query is required");
        getManager().query(query);
    }

    @Override
    public <T> PreparedStatement prepare(String query, Class<T> entityClass) {
        requireNonNull(query, "query is required");
        return new KeyValuePreparedStatement(getManager().prepare(query), entityClass);
    }

}
