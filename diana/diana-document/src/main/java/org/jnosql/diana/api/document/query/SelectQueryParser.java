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

import org.jnosql.diana.api.Params;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.diana.api.document.DocumentPreparedStatementAsync;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.QueryException;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;

final class SelectQueryParser  implements SelectQueryConverter{

    private final SelectQuerySupplier selectQuerySupplier;
    private final CacheQuery<DocumentQuery> cache;

    SelectQueryParser() {
        this.selectQuerySupplier = SelectQuerySupplier.getSupplier();
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

        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        DocumentQuery documentQuery = getDocumentQuery(params, selectQuery, observer);
        return DefaultDocumentPreparedStatement.select(documentQuery, params, query, collectionManager);
    }

    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager,
                                                DocumentObserverParser observer) {
        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        DocumentQuery documentQuery = getDocumentQuery(params, selectQuery, observer);
        return DefaultDocumentPreparedStatementAsync.select(documentQuery, params, query, collectionManager);
    }

    @Override
    public DocumentQueryParams apply(SelectQuery selectQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        Params params = new Params();
        DocumentQuery columnQuery = getDocumentQuery(params, selectQuery, observer);
        return new DefaultDocumentQueryParams(columnQuery, params);
    }

    private DocumentQuery getDocumentQuery(String query, DocumentObserverParser observer) {

        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        String collection = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = selectQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, collection))
                .collect(toList());
        DocumentCondition condition = null;
        Params params = new Params();
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

    private Sort toSort(org.jnosql.query.Sort sort, DocumentObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.getName()),
                sort.getType().equals(org.jnosql.query.Sort.SortType.ASC) ? ASC : DESC);
    }



}
