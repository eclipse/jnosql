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
package org.eclipse.jnosql.artemis.column.query;


import jakarta.nosql.Params;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Sort;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnDeleteQueryParams;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParams;
import jakarta.nosql.column.DeleteQueryConverter;
import jakarta.nosql.column.SelectQueryConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.artemis.reflection.ClassMapping;
import jakarta.nosql.query.DeleteQuery;
import jakarta.nosql.query.SelectQuery;
import org.eclipse.jnosql.artemis.repository.DynamicReturn;
import org.eclipse.jnosql.artemis.util.ParamsBinder;
import org.eclipse.jnosql.diana.query.method.DeleteMethodProvider;
import org.eclipse.jnosql.diana.query.method.SelectMethodProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BaseColumnRepository<T> {

    protected abstract Converters getConverters();

    protected abstract ClassMapping getClassMapping();

    protected abstract ColumnTemplate getTemplate();

    private ColumnObserverParser parser;

    private ParamsBinder paramsBinder;

    private static final SelectQueryConverter SELECT_CONVERTER = ServiceLoaderProvider.get(SelectQueryConverter.class);

    private static final DeleteQueryConverter DELETE_CONVERTER = ServiceLoaderProvider.get(DeleteQueryConverter.class);

    protected ColumnQuery getQuery(Method method, Object[] args) {
        SelectMethodProvider selectMethodFactory = SelectMethodProvider.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, getClassMapping().getName());
        ColumnQueryParams queryParams = SELECT_CONVERTER.apply(selectQuery, getParser());
        ColumnQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return getQuerySorts(args, query);
    }

    protected ColumnDeleteQuery getDeleteQuery(Method method, Object[] args) {
        DeleteMethodProvider deleteMethodFactory = DeleteMethodProvider.get();
        DeleteQuery deleteQuery = deleteMethodFactory.apply(method, getClassMapping().getName());
        ColumnDeleteQueryParams queryParams = DELETE_CONVERTER.apply(deleteQuery, getParser());
        ColumnDeleteQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return query;
    }

    protected ColumnQuery getQuerySorts(Object[] args, ColumnQuery query) {
        List<Sort> sorts = DynamicReturn.findSorts(args);
        if (!sorts.isEmpty()) {
            List<Sort> newOrders = new ArrayList<>();
            newOrders.addAll(query.getSorts());
            newOrders.addAll(sorts);
            return new ArtemisColumnQuery(newOrders, query.getLimit(), query.getSkip(),
                    query.getCondition().orElse(null), query.getColumnFamily());
        }
        return query;
    }


    protected ColumnObserverParser getParser() {
        if (parser == null) {
            this.parser = new RepositoryColumnObserverParser(getClassMapping());
        }
        return parser;
    }

    protected ParamsBinder getParamsBinder() {
        if (Objects.isNull(paramsBinder)) {
            this.paramsBinder = new ParamsBinder(getClassMapping(), getConverters());
        }
        return paramsBinder;
    }

    protected Object executeQuery(Method method, Object[] args, Class<?> typeClass, ColumnQuery query) {
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(() -> getTemplate().select(query))
                .withSingleResult(() -> getTemplate().singleResult(query))
                .withPagination(DynamicReturn.findPagination(args))
                .withStreamPagination(streamPagination(query))
                .withSingleResultPagination(getSingleResult(query))
                .withPage(getPage(query))
                .build();
        return dynamicReturn.execute();
    }

    protected Function<Pagination, Page<T>> getPage(ColumnQuery query) {
        return p -> getTemplate().select(ColumnQueryPagination.of(query, p));
    }

    protected Function<Pagination, Optional<T>> getSingleResult(ColumnQuery query) {
        return p -> {
            ColumnQuery queryPagination = ColumnQueryPagination.of(query, p);
            return getTemplate().singleResult(queryPagination);
        };
    }

    protected Function<Pagination, Stream<T>> streamPagination(ColumnQuery query) {
        return p -> {
            ColumnQuery queryPagination = ColumnQueryPagination.of(query, p);
            return getTemplate().select(queryPagination);
        };
    }
}
