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
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Sort;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatement;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParams;
import jakarta.nosql.column.SelectQueryConverter;
import jakarta.nosql.query.SelectQuery;
import jakarta.nosql.query.SelectQuery.SelectQueryProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * The default implementation of {@link SelectQueryConverter}
 */
public final class SelectQueryParser implements SelectQueryConverter {

    private final SelectQueryProvider selectQueryProvider;
    private final CacheQuery<ColumnQuery> cache;

    public SelectQueryParser() {
        this.selectQueryProvider = ServiceLoaderProvider.get(SelectQueryProvider.class);
        this.cache = new CacheQuery<>(this::getColumnQuery);
    }

    Stream<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        ColumnQuery columnQuery = cache.get(query, observer);
        return manager.select(columnQuery);
    }


    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        Params params = Params.newParams();

        SelectQuery selectQuery = selectQueryProvider.apply(query);

        ColumnQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return DefaultColumnPreparedStatement.select(columnQuery, params, query, manager);
    }


    @Override
    public ColumnQueryParams apply(SelectQuery selectQuery, ColumnObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");

        Params params = Params.newParams();
        ColumnQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return new DefaultColumnQueryParams(columnQuery, params);
    }


    private ColumnQuery getColumnQuery(String query, ColumnObserverParser observer) {

        SelectQuery selectQuery = selectQueryProvider.apply(query);
        String columnFamily = observer.fireEntity(selectQuery.getEntity());
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> columns = selectQuery.getFields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(s -> toSort(s, observer, columnFamily))
                .collect(toList());
        ColumnCondition condition = null;
        Params params = Params.newParams();
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

    private Sort toSort(Sort sort, ColumnObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.getName()), sort.getType());
    }


}