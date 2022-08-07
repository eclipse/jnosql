/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.column.reactive.query;

import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.mapping.column.query.BaseColumnRepository;
import org.eclipse.jnosql.mapping.query.RepositoryType;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepositoryType;
import org.eclipse.jnosql.mapping.repository.DynamicQueryMethodReturn;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;

import static jakarta.nosql.column.ColumnQuery.select;

public abstract class AbstractReactiveColumnRepositoryProxy<T> extends BaseColumnRepository<T> implements InvocationHandler {


    protected abstract ReactiveRepository<?, ?> getRepository();

    protected abstract ColumnTemplate getTemplate();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = ReactiveRepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getType();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                ColumnQuery query = getQuery(method, args);
                return executeQuery(method, args, typeClass, query);
            case FIND_ALL:
                ColumnQuery queryFindAll = select().from(getClassMapping().getName()).build();
                return executeQuery(method, args, typeClass, getQuerySorts(args, queryFindAll));
            case DELETE_BY:
                ColumnDeleteQuery columnDeleteQuery = getDeleteQuery(method, args);
                Iterable<Void> iterable = () -> {
                    getTemplate().delete(columnDeleteQuery);
                    return Collections.emptyIterator();
                };
                return ReactiveStreams.fromIterable(iterable).buildRs();
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
}

