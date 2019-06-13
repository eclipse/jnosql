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


import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.DynamicAsyncQueryMethodReturn;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static jakarta.nosql.column.ColumnQuery.select;


/**
 * Template method to {@link RepositoryAsync} proxy on column
 *
 * @param <T> the type
 */
public abstract class AbstractColumnRepositoryAsyncProxy<T> extends BaseColumnRepository implements InvocationHandler {

    protected abstract RepositoryAsync getRepository();

    protected abstract ColumnTemplateAsync getTemplate();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                ColumnQuery query = getQuery(method, args);
                return executeQuery(getCallback(args), query);
            case FIND_ALL:
                return executeQuery(getCallback(args), select().from(getClassMapping().getName()).build());
            case DELETE_BY:
                ColumnDeleteQuery deleteQuery = getDeleteQuery(method, args);
                return executeDelete(getCallback(args), deleteQuery);
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case JNOSQL_QUERY:

                DynamicAsyncQueryMethodReturn<Object> nativeQuery = DynamicAsyncQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withAsyncConsumer(getTemplate()::query)
                        .withPrepareConverter(q -> getTemplate().prepare(q))
                        .build();
                nativeQuery.execute();
                return Void.class;
            default:
                return Void.class;
        }
    }



    private Object executeDelete(Object arg, ColumnDeleteQuery deleteQuery) {
        if (arg instanceof Consumer) {
            getTemplate().delete(deleteQuery, (Consumer) arg);
            return Void.class;
        }
        getTemplate().delete(deleteQuery);
        return Void.class;
    }

    private Object getCallback(Object[] args) {
        if(args == null || args.length == 0) {
            return null;
        }
        return args[args.length - 1];
    }

    private Object executeQuery(Object arg, ColumnQuery query) {
        if (arg instanceof Consumer) {
            getTemplate().select(query, (Consumer) arg);
            return null;
        }

        throw new DynamicQueryException("On select async method you must put a java.util.function.Consumer" +
                " as end parameter as callback");
    }

}
