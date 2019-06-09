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
package org.jnosql.diana.column.query;

import org.jnosql.diana.Params;
import org.jnosql.diana.QueryException;
import org.jnosql.diana.Sort;
import org.jnosql.diana.column.ColumnCondition;
import org.jnosql.diana.column.ColumnEntity;
import org.jnosql.diana.column.ColumnFamilyManager;
import org.jnosql.diana.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.column.ColumnObserverParser;
import org.jnosql.diana.column.ColumnPreparedStatement;
import org.jnosql.diana.column.ColumnPreparedStatementAsync;
import org.jnosql.diana.column.ColumnQuery;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

final class SelectQueryParser implements SelectQueryConverter {

    private final SelectQuerySupplier selectQuerySupplier;
    private final CacheQuery<ColumnQuery> cache;

    SelectQueryParser() {
        this.selectQuerySupplier = SelectQuerySupplier.getSupplier();
        this.cache = new CacheQuery<>(this::getColumnQuery);
    }

    List<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        ColumnQuery columnQuery = cache.get(query, observer);
        return manager.select(columnQuery);
    }

    void queryAsync(String query, ColumnFamilyManagerAsync manager, Consumer<List<ColumnEntity>> callBack,
                    ColumnObserverParser observer) {

        ColumnQuery columnQuery = cache.get(query, observer);
        manager.select(columnQuery, callBack);
    }

    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        ColumnQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return DefaultColumnPreparedStatement.select(columnQuery, params, query, manager);
    }


    @Override
    public ColumnQueryParams apply(SelectQuery selectQuery, ColumnObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");

        Params params = new Params();
        ColumnQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return new DefaultColumnQueryParams(columnQuery, params);
    }

    ColumnPreparedStatementAsync prepareAsync(String query, ColumnFamilyManagerAsync manager,
                                              ColumnObserverParser observer) {
        Params params = new Params();

        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        ColumnQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return DefaultColumnPreparedStatementAsync.select(columnQuery, params, query, manager);
    }

    private ColumnQuery getColumnQuery(String query, ColumnObserverParser observer) {

        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        String columnFamily = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> columns = selectQuery.getFields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, columnFamily))
                .collect(toList());
        ColumnCondition condition = null;
        Params params = new Params();
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultColumnQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    private ColumnQuery getColumnQuery(Params params, SelectQuery selectQuery, ColumnObserverParser observer) {

        String columnFamily = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> columns = selectQuery.getFields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());

        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, columnFamily)).collect(toList());
        ColumnCondition condition = null;
        if (selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        return new DefaultColumnQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    private Sort toSort(org.jnosql.query.Sort sort, ColumnObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.getName()),
                sort.getType().equals(org.jnosql.query.Sort.SortType.ASC) ? Sort.SortType.ASC : Sort.SortType.DESC);
    }


}