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

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.DeleteQueryConverter;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentDeleteQueryParams;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatement;
import jakarta.nosql.document.DocumentPreparedStatementAsync;
import jakarta.nosql.query.DeleteQuery;
import jakarta.nosql.query.DeleteQuery.DeleteQueryProvider;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default implementation of {@link DeleteQueryConverter}
 */
public final class DeleteQueryParser implements DeleteQueryConverter {

    private final DeleteQueryProvider deleteQueryProvider;
    private final CacheQuery<DocumentDeleteQuery> cache;

    public DeleteQueryParser() {
        this.deleteQueryProvider = ServiceLoaderProvider.get(DeleteQueryProvider.class);
        cache = new CacheQuery<>(this::getQuery);
    }

    Stream<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        DocumentDeleteQuery documentQuery = cache.get(query, observer);
        collectionManager.delete(documentQuery);
        return Stream.empty();
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager,
                    Consumer<Stream<DocumentEntity>> callBack, DocumentObserverParser observer) {

        DocumentDeleteQuery documentQuery = cache.get(query, observer);
        collectionManager.delete(documentQuery, v -> callBack.accept(Stream.empty()));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager,
                                      DocumentObserverParser observer) {
        Params params = Params.newParams();
        DocumentDeleteQuery documentQuery = getQuery(query, params, observer);
        return DefaultDocumentPreparedStatement.delete(documentQuery, params, query, collectionManager);
    }


    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager,
                                                DocumentObserverParser observer) {
        Params params = Params.newParams();
        DocumentDeleteQuery documentQuery = getQuery(query, params, observer);
        return DefaultDocumentPreparedStatementAsync.delete(documentQuery, params, query, collectionManager);

    }

    @Override
    public DocumentDeleteQueryParams apply(DeleteQuery deleteQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(deleteQuery, "deleteQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        Params params = Params.newParams();
        DocumentDeleteQuery query = getQuery(params, observer, deleteQuery);
        return new DefaultDocumentDeleteQueryParams(query, params);
    }

    private DocumentDeleteQuery getQuery(String query, Params params, DocumentObserverParser observer) {
        DeleteQuery deleteQuery = deleteQueryProvider.apply(query);

        return getQuery(params, observer, deleteQuery);
    }

    private DocumentDeleteQuery getQuery(Params params, DocumentObserverParser observer,
                                         DeleteQuery deleteQuery) {
        String collection = observer.fireEntity(deleteQuery.getEntity());
        List<String> documents = deleteQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        DocumentCondition condition = null;

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }

    private DocumentDeleteQuery getQuery(String query, DocumentObserverParser observer) {
        DeleteQuery deleteQuery = deleteQueryProvider.apply(query);

        String collection = observer.fireEntity(deleteQuery.getEntity());
        List<String> documents = deleteQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        DocumentCondition condition = null;
        Params params = Params.newParams();

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }


}
