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
package org.eclipse.jnosql.diana.column.query;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnPreparedStatementAsync;
import jakarta.nosql.column.ColumnQuery;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

final class DefaultColumnPreparedStatementAsync implements ColumnPreparedStatementAsync {

    private final ColumnEntity entity;

    private final ColumnQuery columnQuery;

    private final ColumnDeleteQuery columnDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final ColumnFamilyManagerAsync manager;

    private DefaultColumnPreparedStatementAsync(ColumnEntity entity,
                                                ColumnQuery columnQuery,
                                                ColumnDeleteQuery columnDeleteQuery,
                                                PreparedStatementType type,
                                                Params params,
                                                String query,
                                                List<String> paramsLeft,
                                                Duration duration,
                                                ColumnFamilyManagerAsync manager) {
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
    public ColumnPreparedStatementAsync bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    @Override
    public void getResult(Consumer<Stream<ColumnEntity>> callBack) {
        Objects.requireNonNull(callBack, "callBack is required");

        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT:
                manager.select(columnQuery, callBack);
                return;
            case DELETE:
                manager.delete(columnDeleteQuery, c -> callBack.accept(Stream.empty()));
                return;
            case UPDATE:
                manager.update(entity, c -> callBack.accept(Stream.of(c)));
                return;
            case INSERT:
                if (Objects.isNull(duration)) {
                    manager.insert(entity, c -> callBack.accept(Stream.of(c)));
                } else {
                    manager.insert(entity, duration, c -> callBack.accept(Stream.of(c)));
                }
                return;
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);

        }
    }

    public void getSingleResult(Consumer<Optional<ColumnEntity>> callBack) {
        Objects.requireNonNull(callBack, "callBack is required");

        getResult(entities -> {
            final Iterator<ColumnEntity> iterator = entities.iterator();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.empty());
                return;
            }
            final ColumnEntity next = iterator.next();
            if (!iterator.hasNext()) {
                callBack.accept(Optional.of(next));
                return;
            }
            throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
        });

    }

    enum PreparedStatementType {
        SELECT, DELETE, UPDATE, INSERT
    }


    @Override
    public String toString() {
        return query;
    }

    static ColumnPreparedStatementAsync select(
            ColumnQuery columnQuery,
            Params params,
            String query,
            ColumnFamilyManagerAsync manager) {
        return new DefaultColumnPreparedStatementAsync(null, columnQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatementAsync delete(ColumnDeleteQuery columnDeleteQuery,
                                               Params params,
                                               String query,
                                               ColumnFamilyManagerAsync manager) {

        return new DefaultColumnPreparedStatementAsync(null, null,
                columnDeleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatementAsync insert(ColumnEntity entity,
                                               Params params,
                                               String query,
                                               Duration duration,
                                               ColumnFamilyManagerAsync manager) {
        return new DefaultColumnPreparedStatementAsync(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static ColumnPreparedStatementAsync update(ColumnEntity entity,
                                               Params params,
                                               String query,
                                               ColumnFamilyManagerAsync manager) {
        return new DefaultColumnPreparedStatementAsync(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}