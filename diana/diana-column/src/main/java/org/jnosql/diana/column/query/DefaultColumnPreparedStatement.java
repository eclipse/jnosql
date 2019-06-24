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
package org.jnosql.diana.column.query;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnPreparedStatement;
import jakarta.nosql.column.ColumnQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

final class DefaultColumnPreparedStatement implements ColumnPreparedStatement {

    private final ColumnEntity entity;

    private final ColumnQuery columnQuery;

    private final ColumnDeleteQuery columnDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final ColumnFamilyManager manager;

    private DefaultColumnPreparedStatement(ColumnEntity entity,
                                           ColumnQuery columnQuery,
                                           ColumnDeleteQuery columnDeleteQuery,
                                           PreparedStatementType type,
                                           Params params,
                                           String query,
                                           List<String> paramsLeft,
                                           Duration duration,
                                           ColumnFamilyManager manager) {
        this.entity = entity;
        this.columnQuery = columnQuery;
        this.columnDeleteQuery = columnDeleteQuery;
        this.type = type;
        this.params = params;
        this.query = query;
        this.paramsLeft = paramsLeft;
        this.manager = manager;
        this.duration = duration;
    }

    @Override
    public ColumnPreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    @Override
    public List<ColumnEntity> getResultList() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT:
                return manager.select(columnQuery);
            case DELETE:
                manager.delete(columnDeleteQuery);
                return emptyList();
            case UPDATE:
                return singletonList(manager.update(entity));
            case INSERT:
                if (Objects.isNull(duration)) {
                    return singletonList(manager.insert(entity));
                } else {
                    return singletonList(manager.insert(entity, duration));
                }
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);

        }
    }

    @Override
    public Optional<ColumnEntity> getSingleResult() {
        List<ColumnEntity> entities = getResultList();
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

    enum PreparedStatementType {
        SELECT, DELETE, UPDATE, INSERT
    }


    @Override
    public String toString() {
        return query;
    }

    static ColumnPreparedStatement select(
            ColumnQuery columnQuery,
            Params params,
            String query,
            ColumnFamilyManager manager) {
        return new DefaultColumnPreparedStatement(null, columnQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatement delete(ColumnDeleteQuery columnDeleteQuery,
                                          Params params,
                                          String query,
                                          ColumnFamilyManager manager) {

        return new DefaultColumnPreparedStatement(null, null,
                columnDeleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatement insert(ColumnEntity entity,
                                          Params params,
                                          String query,
                                          Duration duration,
                                          ColumnFamilyManager manager) {
        return new DefaultColumnPreparedStatement(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static ColumnPreparedStatement update(ColumnEntity entity,
                                          Params params,
                                          String query,
                                          ColumnFamilyManager manager) {
        return new DefaultColumnPreparedStatement(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}
