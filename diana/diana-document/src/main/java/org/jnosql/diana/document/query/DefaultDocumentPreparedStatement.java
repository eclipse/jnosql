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

import org.jnosql.diana.NonUniqueResultException;
import org.jnosql.diana.Params;
import org.jnosql.diana.QueryException;
import org.jnosql.diana.document.DocumentCollectionManager;
import org.jnosql.diana.document.DocumentDeleteQuery;
import org.jnosql.diana.document.DocumentEntity;
import org.jnosql.diana.document.DocumentPreparedStatement;
import org.jnosql.diana.document.DocumentQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

final class DefaultDocumentPreparedStatement implements DocumentPreparedStatement {

    private final DocumentEntity entity;

    private final DocumentQuery documentQuery;

    private final DocumentDeleteQuery documentDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private final Duration duration;

    private final DocumentCollectionManager manager;

    private DefaultDocumentPreparedStatement(DocumentEntity entity,
                                             DocumentQuery documentQuery,
                                             DocumentDeleteQuery documentDeleteQuery,
                                             PreparedStatementType type,
                                             Params params,
                                             String query,
                                             List<String> paramsLeft,
                                             Duration duration,
                                             DocumentCollectionManager manager) {
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
    public DocumentPreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        params.bind(name, value);
        return this;
    }

    @Override
    public List<DocumentEntity> getResultList() {
        if (!paramsLeft.isEmpty()) {
            throw new QueryException("Check all the parameters before execute the query, params left: " + paramsLeft);
        }
        switch (type) {
            case SELECT:
                return manager.select(documentQuery);
            case DELETE:
                manager.delete(documentDeleteQuery);
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
    public Optional<DocumentEntity> getSingleResult() {
        List<DocumentEntity> entities = getResultList();
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

    static DocumentPreparedStatement select(
            DocumentQuery documentQuery,
            Params params,
            String query,
            DocumentCollectionManager manager) {
        return new DefaultDocumentPreparedStatement(null, documentQuery,
                null, PreparedStatementType.SELECT, params, query,
                params.getParametersNames(), null, manager);

    }

    static DocumentPreparedStatement delete(DocumentDeleteQuery documentDeleteQuery,
                                            Params params,
                                            String query,
                                            DocumentCollectionManager manager) {

        return new DefaultDocumentPreparedStatement(null, null,
                documentDeleteQuery, PreparedStatementType.DELETE, params, query,
                params.getParametersNames(), null, manager);

    }

    static DocumentPreparedStatement insert(DocumentEntity entity,
                                            Params params,
                                            String query,
                                            Duration duration,
                                            DocumentCollectionManager manager) {
        return new DefaultDocumentPreparedStatement(entity, null,
                null, PreparedStatementType.INSERT, params, query,
                params.getParametersNames(), duration, manager);

    }

    static DocumentPreparedStatement update(DocumentEntity entity,
                                            Params params,
                                            String query,
                                            DocumentCollectionManager manager) {
        return new DefaultDocumentPreparedStatement(entity, null,
                null, PreparedStatementType.UPDATE, params, query,
                params.getParametersNames(), null, manager);

    }
}
