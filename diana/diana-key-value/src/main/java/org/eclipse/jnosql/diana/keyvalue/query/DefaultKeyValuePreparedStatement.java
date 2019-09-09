/*
 *
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
 *
 */
package org.eclipse.jnosql.diana.keyvalue.query;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.keyvalue.KeyValuePreparedStatement;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class DefaultKeyValuePreparedStatement implements KeyValuePreparedStatement {


    private final List<Value> keys;

    private final PreparedStatementType type;

    private final BucketManager manager;

    private final List<String> paramsLeft;

    private final Params params;

    private final Duration ttl;

    private final String query;
    private final Value key;
    private final Value value;

    DefaultKeyValuePreparedStatement(Value key, Value value, List<Value> keys,
                                     PreparedStatementType type,
                                     BucketManager manager,
                                     Params params,
                                     Duration ttl, String query) {
        this.key = key;
        this.value = value;
        this.keys = keys;
        this.type = type;
        this.manager = manager;
        this.params = params;
        this.paramsLeft = params.getParametersNames();
        this.ttl = ttl;
        this.query = query;
    }

    @Override
    public KeyValuePreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    @Override
    public Stream<Value> getResult() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: "
                    + paramsLeft);
        }
        switch (type) {
            case GET:
                return keys.stream().map(Value::get)
                        .map(manager::get)
                        .filter(Optional::isPresent)
                        .map(Optional::get);
            case DEL:
                manager.remove(keys.stream().map(Value::get).collect(Collectors.toList()));
                return Stream.empty();
            case PUT:
                KeyValueEntity entity = KeyValueEntity.of(key.get(), value.get());
                if (Objects.isNull(ttl)) {
                    manager.put(entity);
                } else {
                    manager.put(entity, ttl);
                }
                return Stream.empty();
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);
        }
    }

    @Override
    public Optional<Value> getSingleResult() {
        Stream<Value> entities = getResult();
        final Iterator<Value> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final Value next = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(next);
        }

        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

    enum PreparedStatementType {
        GET, PUT, DEL
    }

    static KeyValuePreparedStatement get(List<Value> keys,
                                         BucketManager manager,
                                         Params params, String query) {
        return new DefaultKeyValuePreparedStatement(null, null, keys, PreparedStatementType.GET,
                manager, params, null, query);
    }

    static KeyValuePreparedStatement put(Value key,
                                         Value value,
                                         BucketManager manager,
                                         Params params,
                                         Duration ttl, String query) {
        return new DefaultKeyValuePreparedStatement(key, value, null, PreparedStatementType.PUT,
                manager, params, ttl, query);
    }

    static KeyValuePreparedStatement del(List<Value> keys,
                                         BucketManager manager,
                                         Params params, String query) {
        return new DefaultKeyValuePreparedStatement(null, null, keys, PreparedStatementType.DEL,
                manager, params, null, query);
    }


}
