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
package org.eclipse.jnosql.artemis.column;

import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.PreparedStatementAsync;
import jakarta.nosql.mapping.column.ColumnEntityConverter;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class ColumnPreparedStatementAsync implements PreparedStatementAsync {

    private final jakarta.nosql.column.ColumnPreparedStatementAsync preparedStatementAsync;
    private final ColumnEntityConverter converter;

    ColumnPreparedStatementAsync(jakarta.nosql.column.ColumnPreparedStatementAsync preparedStatementAsync,
                                 ColumnEntityConverter converter) {
        this.preparedStatementAsync = preparedStatementAsync;
        this.converter = converter;
    }

    @Override
    public PreparedStatementAsync bind(String name, Object value) {
        preparedStatementAsync.bind(name, value);
        return this;
    }

    @Override
    public <T> void getResult(Consumer<Stream<T>> callback) {
        requireNonNull(callback, "callback is required");
        Consumer<Stream<ColumnEntity>> mapper = columnEntities -> callback.accept(columnEntities.map(c -> (T) converter.toEntity(c)));
        preparedStatementAsync.getResult(mapper);
    }

    @Override
    public <T> void getSingleResult(Consumer<Optional<T>> callback) {
        requireNonNull(callback, "callback is required");

        Consumer<Optional<ColumnEntity>> mapper = columnEntity -> callback.accept(columnEntity.map(converter::toEntity));
        preparedStatementAsync.getSingleResult(mapper);
    }
}
