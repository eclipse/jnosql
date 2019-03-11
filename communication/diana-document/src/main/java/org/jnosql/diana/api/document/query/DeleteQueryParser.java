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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.diana.api.document.DocumentPreparedStatementAsync;
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.DeleteQuerySupplier;
import org.jnosql.diana.api.QueryException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

final class DeleteQueryParser implements DeleteQueryConverter {

    private final DeleteQuerySupplier selectQuerySupplier;
    private final CacheQuery<DocumentDeleteQuery> cache;

    DeleteQueryParser() {
        this.selectQuerySupplier = DeleteQuerySupplier.getSupplier();
        cache = new CacheQuery<>(this::getQuery);
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        DocumentDeleteQuery documentQuery = cache.get(query, observer);
        collectionManager.delete(documentQuery);
        return Collections.emptyList();
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager,
                    Consumer<List<DocumentEntity>> callBack, DocumentObserverParser observer) {

        DocumentDeleteQuery documentQuery = cache.get(query, observer);
        collectionManager.delete(documentQuery, v -> callBack.accept(Collections.emptyList()));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager,
                                      DocumentObserverParser observer) {
        DocumentParams params = new DocumentParams();
        DocumentDeleteQuery documentQuery = getQuery(query, params, observer);
        return DefaultDocumentPreparedStatement.delete(documentQuery, params, query, collectionManager);
    }


    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager,
                                                DocumentObserverParser observer) {
        DocumentParams params = new DocumentParams();
        DocumentDeleteQuery documentQuery = getQuery(query, params, observer);
        return DefaultDocumentPreparedStatementAsync.delete(documentQuery, params, query, collectionManager);

    }

    @Override
    public DocumentDeleteQueryParams apply(DeleteQuery deleteQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(deleteQuery, "deleteQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        DocumentParams params = new DocumentParams();
        DocumentDeleteQuery query = getQuery(params, observer, deleteQuery);
        return new DefaultDocumentDeleteQueryParams(query, params);
    }

    private DocumentDeleteQuery getQuery(String query, DocumentParams params, DocumentObserverParser observer) {
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        return getQuery(params, observer, deleteQuery);
    }

    private DocumentDeleteQuery getQuery(DocumentParams params, DocumentObserverParser observer,
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
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        String collection = observer.fireEntity(deleteQuery.getEntity());
        List<String> documents = deleteQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        DocumentCondition condition = null;
        DocumentParams params = new DocumentParams();

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }


}
