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


import org.jnosql.artemis.DynamicQueryException;
import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;

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
                return getJnosqlQuery(method, args);
            default:
                return Void.class;
        }
    }

    private Object getJnosqlQuery(Method method, Object[] args) {
        String value = method.getAnnotation(Query.class).value();
        Map<String, Object> params = getParams(method, args);
        Consumer<List<T>> consumer = getConsumer(args);
        if (params.isEmpty()) {
            getTemplate().query(value, consumer);
        } else {
            PreparedStatementAsync prepare = getTemplate().prepare(value);
            params.forEach(prepare::bind);
            prepare.getResultList(consumer);
        }

        return Void.class;
    }

    private Consumer<List<T>> getConsumer(Object[] args) {
        Consumer<List<T>> consumer;
        Object callBack = getCallback(args);
        if (callBack instanceof Consumer) {
            consumer = Consumer.class.cast(callBack);
        } else {
            consumer = l -> {
            };
        }
        return consumer;
    }


    private Object executeDelete(Object arg, ColumnDeleteQuery deleteQuery) {
        if (Consumer.class.isInstance(arg)) {
            getTemplate().delete(deleteQuery, Consumer.class.cast(arg));
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
        if (Consumer.class.isInstance(arg)) {
            getTemplate().select(query, Consumer.class.cast(arg));
            return null;
        }

        throw new DynamicQueryException("On select async method you must put a java.util.function.Consumer" +
                " as end parameter as callback");
    }

}
