/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Represents a precompiled query statement.
 */
public final class CommunicationPreparedStatement {

    private final CommunicationEntity entity;

    private final SelectQuery selectQuery;

    private final DeleteQuery deleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final DatabaseManager manager;

    private CommunicationPreparedStatement(CommunicationEntity entity,
                                           SelectQuery selectQuery,
                                           DeleteQuery deleteQuery,
                                           PreparedStatementType type,
                                           Params params,
                                           String query,
                                           List<String> paramsLeft,
                                           Duration duration,
                                           DatabaseManager manager) {
        this.entity = entity;
        this.selectQuery = selectQuery;
        this.deleteQuery = deleteQuery;
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
     * @throws NullPointerException when either name or value is null
     */
    public CommunicationPreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    /**
     * Returns the result as a stream of entities.
     *
     * @return the stream of entities
     * @throws QueryException if there are parameters left to bind
     */
    public Stream<CommunicationEntity> result() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT -> {
                return manager.select(selectQuery);
            }
            case DELETE -> {
                manager.delete(deleteQuery);
                return Stream.empty();
            }
            case UPDATE -> {
                return Stream.of(manager.update(entity));
            }
            case INSERT -> {
                if (Objects.isNull(duration)) {
                    return Stream.of(manager.insert(entity));
                } else {
                    return Stream.of(manager.insert(entity, duration));
                }
            }
            default -> throw new UnsupportedOperationException("there is not support to operation type: " + type);
        }
    }

    /**
     * Returns the single result as an optional entity.
     *
     * @return the optional entity
     * @throws NonUniqueResultException if the result contains more than one entity
     */
    public Optional<CommunicationEntity> singleResult() {
        Stream<CommunicationEntity> entities = result();
        final Iterator<CommunicationEntity> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final CommunicationEntity next = iterator.next();
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

    static CommunicationPreparedStatement select(
            SelectQuery selectQuery,
            Params params,
            String query,
            DatabaseManager manager) {
        return new CommunicationPreparedStatement(null, selectQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static CommunicationPreparedStatement delete(DeleteQuery deleteQuery,
                                                 Params params,
                                                 String query,
                                                 DatabaseManager manager) {

        return new CommunicationPreparedStatement(null, null,
                deleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static CommunicationPreparedStatement insert(CommunicationEntity entity,
                                                 Params params,
                                                 String query,
                                                 Duration duration,
                                                 DatabaseManager manager) {
        return new CommunicationPreparedStatement(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static CommunicationPreparedStatement update(CommunicationEntity entity,
                                                 Params params,
                                                 String query,
                                                 DatabaseManager manager) {
        return new CommunicationPreparedStatement(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}
