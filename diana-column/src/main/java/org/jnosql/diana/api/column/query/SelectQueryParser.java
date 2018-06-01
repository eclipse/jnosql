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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.diana.api.column.ColumnPreparedStatement;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.diana.api.column.ColumnQuery;
import org.jnosql.query.QueryException;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;

final class SelectQueryParser {

    private final SelectQuerySupplier selectQuerySupplier;
    private final CacheQuery<ColumnQuery> cache;

    SelectQueryParser() {
        this.selectQuerySupplier = SelectQuerySupplier.getSupplier();
        this.cache = new CacheQuery<>(this::getColumnQuery);
    }

    List<ColumnEntity> query(String query, ColumnFamilyManager collectionManager, ColumnObserverParser observer) {

        ColumnQuery ColumnQuery = cache.get(query, observer);
        return collectionManager.select(ColumnQuery);
    }

    void queryAsync(String query, ColumnFamilyManagerAsync collectionManager, Consumer<List<ColumnEntity>> callBack,
                    ColumnObserverParser observer) {

        ColumnQuery ColumnQuery = cache.get(query, observer);
        collectionManager.select(ColumnQuery, callBack);
    }

    ColumnPreparedStatement prepare(String query, ColumnFamilyManager collectionManager, ColumnObserverParser observer) {

        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        ColumnQuery ColumnQuery = getColumnQuery(params, selectQuery, observer);
        return DefaultDocumentPreparedStatement.select(ColumnQuery, params, query, collectionManager);
    }

    ColumnPreparedStatementAsync prepareAsync(String query, ColumnFamilyManagerAsync collectionManager,
                                              ColumnObserverParser observer) {
        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        ColumnQuery ColumnQuery = getColumnQuery(params, selectQuery, observer);
        return DefaultColumnPreparedStatementAsync.select(ColumnQuery, params, query, collectionManager);
    }

    private ColumnQuery getColumnQuery(String query, ColumnObserverParser observer) {

        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        String collection = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = selectQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, collection))
                .collect(toList());
        ColumnCondition condition = null;
        Params params = new Params();
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultColumnQuery(limit, skip, collection, documents, sorts, condition);
    }

    private ColumnQuery getColumnQuery(Params params, SelectQuery selectQuery, ColumnObserverParser observer) {

        String collection = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = selectQuery.getFields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());

        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, collection)).collect(toList());
        ColumnCondition condition = null;
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        return new DefaultColumnQuery(limit, skip, collection, documents, sorts, condition);
    }

    private Sort toSort(org.jnosql.query.Sort sort, ColumnObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.getName()),
                sort.getType().equals(org.jnosql.query.Sort.SortType.ASC) ? ASC : DESC);
    }


}