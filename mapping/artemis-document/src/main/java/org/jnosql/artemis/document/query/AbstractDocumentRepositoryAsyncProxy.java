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
package org.jnosql.artemis.document.query;


import org.jnosql.artemis.DynamicQueryException;
import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The template method to {@link org.jnosql.artemis.RepositoryAsync} to Document
 *
 * @param <T> the class type
 */
public abstract class AbstractDocumentRepositoryAsyncProxy<T> extends BaseDocumentRepository implements InvocationHandler {


    protected abstract RepositoryAsync getRepository();

    protected abstract DocumentTemplateAsync getTemplate();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {


        RepositoryType type = RepositoryType.of(method);

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                DocumentQuery query = getQuery(method, args);
                return executeQuery(getCallBack(args), query);
            case DELETE_BY:
                DocumentDeleteQuery deleteQuery = getDeleteQuery(method, args);
                return executeDelete(args, deleteQuery);
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case JNOSQL_QUERY:
                return getJnosqlQuery(method, args);
            default:
                return Void.class;
        }

    }

    private Object executeDelete(Object[] args, DocumentDeleteQuery query1) {
        Object callBack = getCallBack(args);
        if (Consumer.class.isInstance(callBack)) {
            getTemplate().delete(query1, Consumer.class.cast(callBack));
        } else {
            getTemplate().delete(query1);
        }
        return Void.class;
    }

    private Object getCallBack(Object[] args) {
        return args[args.length - 1];
    }

    private Object executeQuery(Object arg, DocumentQuery query) {
        if (Consumer.class.isInstance(arg)) {
            getTemplate().select(query, Consumer.class.cast(arg));
        } else {
            throw new DynamicQueryException("On select async method you must put a java.util.function.Consumer" +
                    " as end parameter as callback");
        }
        return Void.class;
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

    private Object getCallback(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        return args[args.length - 1];
    }

}
