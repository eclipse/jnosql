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
package org.jnosql.artemis.kv;


import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.mapping.keyvalue.KeyValueEntityConverter;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.mapping.keyvalue.KeyValueWorkflow;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

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
    public <K> void remove(K key) {
        requireNonNull(key, "key is required");
        getManager().remove(key);
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        requireNonNull(keys, "keys is required");
        getManager().remove(keys);
    }

    @Override
    public <T> List<T> query(String query, Class<T> entityClass) {
        requireNonNull(query, "query is required");
        List<Value> values = getManager().query(query);
        if (!values.isEmpty()) {
            requireNonNull(entityClass, "entityClass is required");
            return values.stream().map(v -> v.get(entityClass)).collect(toList());
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Optional<T> getSingleResult(String query, Class<T> entityClass) {
        List<T> result = query(query, entityClass);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() == 1) {
            return Optional.ofNullable(result.get(0));
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
        return new org.jnosql.artemis.kv.KeyValuePreparedStatement(getManager().prepare(query), entityClass);
    }

}
