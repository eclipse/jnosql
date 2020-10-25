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


import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.artemis.query.RepositoryType;
import org.eclipse.jnosql.artemis.repository.DynamicQueryMethodReturn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Template method to {@link Repository} proxy on column
 *
 * @param <T>  the entity type
 * @param <K> the K entity
 */
public abstract class AbstractColumnRepositoryProxy<T, K> extends  BaseColumnRepository implements InvocationHandler {

    protected abstract Repository getRepository();

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
                ColumnQuery queryFindAll = ColumnQuery.select().from(getClassMapping().getName()).build();
                return executeQuery(method, args, typeClass, getQuerySorts(args, queryFindAll));
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


}