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
package org.jnosql.artemis.document;


import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.diana.document.DocumentEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class DocumentPreparedStatementAsync implements PreparedStatementAsync {

    private final org.jnosql.diana.document.DocumentPreparedStatementAsync preparedStatementAsync;
    private final DocumentEntityConverter converter;

    DocumentPreparedStatementAsync(org.jnosql.diana.document.DocumentPreparedStatementAsync preparedStatementAsync,
                                   DocumentEntityConverter converter) {
        this.preparedStatementAsync = preparedStatementAsync;
        this.converter = converter;
    }

    @Override
    public PreparedStatementAsync bind(String name, Object value) {
        preparedStatementAsync.bind(name, value);
        return this;
    }

    @Override
    public <T> void getResultList(Consumer<List<T>> callback) {
        requireNonNull(callback, "callback is required");

        Consumer<List<DocumentEntity>> mapper = columnEntities -> callback
                .accept(columnEntities.stream()
                        .map(c -> (T) converter.toEntity(c))
                .collect(toList()));
        preparedStatementAsync.getResultList(mapper);
    }

    @Override
    public <T> void getSingleResult(Consumer<Optional<T>> callback) {
        requireNonNull(callback, "callback is required");

        Consumer<Optional<DocumentEntity>> mapper = columnEntity -> callback.accept(columnEntity.map(converter::toEntity));
        preparedStatementAsync.getSingleResult(mapper);
    }
}