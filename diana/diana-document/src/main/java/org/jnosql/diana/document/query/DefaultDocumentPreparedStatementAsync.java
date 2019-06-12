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
package org.jnosql.diana.document.query;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentPreparedStatementAsync;
import jakarta.nosql.document.DocumentQuery;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final class DefaultDocumentPreparedStatementAsync implements DocumentPreparedStatementAsync {

    private final DocumentEntity entity;

    private final DocumentQuery documentQuery;

    private final DocumentDeleteQuery documentDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final DocumentCollectionManagerAsync manager;

    private DefaultDocumentPreparedStatementAsync(DocumentEntity entity,
                                                  DocumentQuery documentQuery,
                                                  DocumentDeleteQuery documentDeleteQuery,
                                                  PreparedStatementType type,
                                                  Params params,
                                                  String query,
                                                  List<String> paramsLeft,
                                                  Duration duration,
                                                  DocumentCollectionManagerAsync manager) {
        this.entity = entity;
        this.documentQuery = documentQuery;
        this.documentDeleteQuery = documentDeleteQuery;
        this.type = type;
        this.params = params;
        this.query = query;
        this.paramsLeft = paramsLeft;
        this.manager = manager;
        this.duration = duration;
    }

    @Override
    public DocumentPreparedStatementAsync bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    @Override
    public void getResultList(Consumer<List<DocumentEntity>> callBack) {
        Objects.requireNonNull(callBack, "callBack is required");

        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT:
                manager.select(documentQuery, callBack);
                return;
            case DELETE:
                manager.delete(documentDeleteQuery, c -> callBack.accept(Collections.emptyList()));
                return;
            case UPDATE:
                manager.update(entity, c -> callBack.accept(Collections.singletonList(c)));
                return;
            case INSERT:
                if (Objects.isNull(duration)) {
                    manager.insert(entity, c -> callBack.accept(Collections.singletonList(c)));
                } else {
                    manager.insert(entity, duration, c -> callBack.accept(Collections.singletonList(c)));
                }
                return;
            default:
                throw new UnsupportedOperationException("there is not support to operation type: " + type);

        }
    }

    public void getSingleResult(Consumer<Optional<DocumentEntity>> callBack) {
        Objects.requireNonNull(callBack, "callBack is required");

        getResultList(entities -> {
            if (entities.isEmpty()) {
                callBack.accept(Optional.empty());
                return;
            }
            if (entities.size() == 1) {
                callBack.accept(Optional.of(entities.get(0)));
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

    static DefaultDocumentPreparedStatementAsync select(
            DocumentQuery documentQuery,
            Params params,
            String query,
            DocumentCollectionManagerAsync manager) {
        return new DefaultDocumentPreparedStatementAsync(null, documentQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static DefaultDocumentPreparedStatementAsync delete(DocumentDeleteQuery documentDeleteQuery,
                                                        Params params,
                                            String query,
                                            DocumentCollectionManagerAsync manager) {

        return new DefaultDocumentPreparedStatementAsync(null, null,
                documentDeleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static DefaultDocumentPreparedStatementAsync insert(DocumentEntity entity,
                                                        Params params,
                                            String query,
                                            Duration duration,
                                            DocumentCollectionManagerAsync manager) {
        return new DefaultDocumentPreparedStatementAsync(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static DefaultDocumentPreparedStatementAsync update(DocumentEntity entity,
                                                        Params params,
                                            String query,
                                            DocumentCollectionManagerAsync manager) {
        return new DefaultDocumentPreparedStatementAsync(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}
