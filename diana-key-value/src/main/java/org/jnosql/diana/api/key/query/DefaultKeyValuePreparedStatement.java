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
package org.jnosql.diana.api.key.query;

import org.jnosql.diana.api.NonUniqueResultException;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValueEntity;
import org.jnosql.diana.api.key.KeyValuePreparedStatement;
import org.jnosql.query.QueryException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

final class DefaultKeyValuePreparedStatement implements KeyValuePreparedStatement {


    private final KeyValueEntity entity;

    private final List<Value> keys;

    private final PreparedStatementType type;

    private final BucketManager manager;

    private final List<String> paramsLeft;

    private final Params params;

    private final Duration ttl;

    private final String query;

    DefaultKeyValuePreparedStatement(KeyValueEntity entity, List<Value> keys,
                                     PreparedStatementType type,
                                     BucketManager manager,
                                     Params params,
                                     Duration ttl, String query) {
        this.entity = entity;
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
    public List<Value> getResultList() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case GET:
                Iterable<Value> values = manager.get(keys.stream().map(Value::get).collect(Collectors.toList()));
                List<Value> target = new ArrayList<>();
                values.forEach(target::add);
                return target;
            case DEL:
                manager.del(keys.stream().map(Value::get).collect(Collectors.toList()));
                return Collections.emptyList();
            case PUT:
                if (Objects.isNull(ttl)) {
                    manager.put(entity);
                } else {
                    manager.put(entity, ttl);
                }
                return Collections.emptyList();
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);
        }
    }

    @Override
    public Optional<Value> getSingleResult() {
        List<Value> entities = getResultList();
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

    enum PreparedStatementType {
        GET, PUT, DEL;
    }

    static KeyValuePreparedStatement get(List<Value> keys,
                                         BucketManager manager,
                                         Params params, String query) {
        return new DefaultKeyValuePreparedStatement(null, keys, PreparedStatementType.GET,
                manager, params, null, query);
    }

    static KeyValuePreparedStatement put(KeyValueEntity entity,
                                         BucketManager manager,
                                         Params params,
                                         Duration ttl, String query) {
        return new DefaultKeyValuePreparedStatement(entity, null, PreparedStatementType.PUT,
                manager, params, ttl, query);
    }

    static KeyValuePreparedStatement del(List<Value> keys,
                                         BucketManager manager,
                                         Params params, String query) {
        return new DefaultKeyValuePreparedStatement(null, keys, PreparedStatementType.DEL,
                manager, params, null, query);
    }


}
