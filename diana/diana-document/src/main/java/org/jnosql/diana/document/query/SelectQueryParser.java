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
import jakarta.nosql.Sort;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatement;
import jakarta.nosql.document.DocumentPreparedStatementAsync;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.document.DocumentQueryParams;
import jakarta.nosql.document.SelectQueryConverter;
import jakarta.nosql.query.SelectQuery;
import jakarta.nosql.query.SelectQuery.SelectQueryProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

final class SelectQueryParser implements SelectQueryConverter {

    private final SelectQueryProvider selectQueryProvider;
    private final CacheQuery<DocumentQuery> cache;

    SelectQueryParser() {
        this.selectQueryProvider = ServiceLoaderProvider.get(SelectQueryProvider.class);
        this.cache = new CacheQuery<>(this::getDocumentQuery);
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        DocumentQuery documentQuery = cache.get(query, observer);
        return collectionManager.select(documentQuery);
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager, Consumer<List<DocumentEntity>> callBack,
                    DocumentObserverParser observer) {

        DocumentQuery documentQuery = cache.get(query, observer);
        collectionManager.select(documentQuery, callBack);
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        Params params = Params.newParams();

        SelectQuery selectQuery = selectQueryProvider.apply(query);

        DocumentQuery documentQuery = getDocumentQuery(params, selectQuery, observer);
        return DefaultDocumentPreparedStatement.select(documentQuery, params, query, collectionManager);
    }

    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager,
                                                DocumentObserverParser observer) {
        Params params = Params.newParams();

        SelectQuery selectQuery = selectQueryProvider.apply(query);

        DocumentQuery documentQuery = getDocumentQuery(params, selectQuery, observer);
        return DefaultDocumentPreparedStatementAsync.select(documentQuery, params, query, collectionManager);
    }

    @Override
    public DocumentQueryParams apply(SelectQuery selectQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        Params params = Params.newParams();
        DocumentQuery columnQuery = getDocumentQuery(params, selectQuery, observer);
        return new DefaultDocumentQueryParams(columnQuery, params);
    }

    private DocumentQuery getDocumentQuery(String query, DocumentObserverParser observer) {

        SelectQuery selectQuery = selectQueryProvider.apply(query);
        String collection = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = selectQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, collection))
                .collect(toList());
        DocumentCondition condition = null;
        Params params = Params.newParams();
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentQuery(limit, skip, collection, documents, sorts, condition);
    }

    private DocumentQuery getDocumentQuery(Params params, SelectQuery selectQuery, DocumentObserverParser observer) {

        String collection = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = selectQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());

        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, collection)).collect(toList());
        DocumentCondition condition = null;
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        return new DefaultDocumentQuery(limit, skip, collection, documents, sorts, condition);
    }

    private Sort toSort(Sort sort, DocumentObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.getName()), sort.getType());
    }


}
