/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.column;


import org.eclipse.jnosql.communication.NonUniqueResultException;
import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * An object that represents a precompiled Query statement.
 */
public final class ColumnPreparedStatement {

    private final ColumnEntity entity;

    private final ColumnQuery columnQuery;

    private final ColumnDeleteQuery columnDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final ColumnManager manager;

    private ColumnPreparedStatement(ColumnEntity entity,
                                    ColumnQuery columnQuery,
                                    ColumnDeleteQuery columnDeleteQuery,
                                    PreparedStatementType type,
                                    Params params,
                                    String query,
                                    List<String> paramsLeft,
                                    Duration duration,
                                    ColumnManager manager) {
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

    /**
     * Binds an argument to a positional parameter.
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @return the same query instance
     * @throws NullPointerException     when there is null parameter
     */
    public ColumnPreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    /**
     * Returns the result as a single element otherwise it will return an {@link Optional#empty()}
     *
     * @return the single result
     */
    public Stream<ColumnEntity> result() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT:
                return manager.select(columnQuery);
            case DELETE:
                manager.delete(columnDeleteQuery);
                return Stream.empty();
            case UPDATE:
                return Stream.of(manager.update(entity));
            case INSERT:
                if (Objects.isNull(duration)) {
                    return Stream.of(manager.insert(entity));
                } else {
                    return Stream.of(manager.insert(entity, duration));
                }
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);

        }
    }

    public Optional<ColumnEntity> singleResult() {
        Stream<ColumnEntity> entities = result();
        final Iterator<ColumnEntity> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final ColumnEntity next = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(next);
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
            ColumnManager manager) {
        return new ColumnPreparedStatement(null, columnQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatement delete(ColumnDeleteQuery columnDeleteQuery,
                                          Params params,
                                          String query,
                                          ColumnManager manager) {

        return new ColumnPreparedStatement(null, null,
                columnDeleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static ColumnPreparedStatement insert(ColumnEntity entity,
                                          Params params,
                                          String query,
                                          Duration duration,
                                          ColumnManager manager) {
        return new ColumnPreparedStatement(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static ColumnPreparedStatement update(ColumnEntity entity,
                                          Params params,
                                          String query,
                                          ColumnManager manager) {
        return new ColumnPreparedStatement(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}
