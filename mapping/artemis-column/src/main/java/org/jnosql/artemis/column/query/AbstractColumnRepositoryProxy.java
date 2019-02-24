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
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.DynamicQueryMethodReturn;
import org.jnosql.artemis.reflection.DynamicReturn;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;

/**
 * Template method to {@link Repository} proxy on column
 *
 * @param <T>  the entity type
 * @param <ID> the ID entity
 */
public abstract class AbstractColumnRepositoryProxy<T, ID> extends  BaseColumnRepository implements InvocationHandler {

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
                DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                        .withClassSource(typeClass)
                        .withMethodSource(method).withList(() -> getTemplate().select(query))
                        .withSingleResult(() -> getTemplate().singleResult(query)).build();

                return dynamicReturn.execute();
            case FIND_ALL:

                ColumnQuery queryFindAll = select().from(getClassMapping().getName()).build();
                DynamicReturn<?> dynamicReturnFindAll = DynamicReturn.builder()
                        .withClassSource(typeClass)
                        .withMethodSource(method).withList(() -> getTemplate().select(queryFindAll))
                        .withSingleResult(() -> getTemplate().singleResult(queryFindAll)).build();
                return dynamicReturnFindAll.execute();

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