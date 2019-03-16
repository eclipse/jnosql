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
package org.jnosql.artemis.column.query;


import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Page;
import org.jnosql.artemis.Pagination;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.column.ColumnQueryPagination;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.DynamicQueryMethodReturn;
import org.jnosql.artemis.reflection.DynamicReturn;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.jnosql.artemis.reflection.DynamicReturn.IS_PAGINATION;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;

/**
 * Template method to {@link Repository} proxy on column
 *
 * @param <T>  the entity type
 * @param <K> the K entity
 */
public abstract class AbstractColumnRepositoryProxy<T, K> extends  BaseColumnRepository implements InvocationHandler {

    protected abstract Repository getRepository();

    protected abstract ColumnTemplate getTemplate();

    protected abstract Converters getConverters();


    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {
        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getClassInstance();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                ColumnQuery query = getQuery(method, args);
                return executeQuery(method, args, typeClass, query);
            case FIND_ALL:
                ColumnQuery queryFindAll = select().from(getClassMapping().getName()).build();
                return executeQuery(method, args, typeClass, queryFindAll);
            case DELETE_BY:
                ColumnDeleteQuery deleteQuery = getDeleteQuery(method, args);
                getTemplate().delete(deleteQuery);
                return Void.class;
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case JNOSQL_QUERY:
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q))
                        .withQueryConverter(q -> getTemplate().query(q)).build();
                return methodReturn.execute();
            default:
                return Void.class;

        }
    }

    private Object executeQuery(Method method, Object[] args, Class<?> typeClass, ColumnQuery query) {
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withList(() -> getTemplate().select(query))
                .withSingleResult(() -> getTemplate().singleResult(query))
                .withPagination(DynamicReturn.findPagination(args))
                .withListPagination(listPagination(query))
                .withSingleResultPagination(getSingleResult(query))
                .withPage(getPage(query))
                .build();
        return dynamicReturn.execute();
    }

    private Function<Pagination, Page<T>> getPage(ColumnQuery query) {
        return p -> getTemplate().select(ColumnQueryPagination.of(query, p));
    }

    private Function<Pagination, Optional<T>> getSingleResult(ColumnQuery query) {
        return p -> {
            ColumnQuery queryPagination = ColumnQueryPagination.of(query, p);
            return getTemplate().singleResult(queryPagination);
        };
    }

    private Function<Pagination, List<T>> listPagination(ColumnQuery query) {
        return p -> {
            ColumnQuery queryPagination = ColumnQueryPagination.of(query, p);
            return getTemplate().select(queryPagination);
        };
    }

}