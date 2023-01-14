/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.document.query;


import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.mapping.query.RepositoryType;
import org.eclipse.jnosql.mapping.repository.DynamicQueryMethodReturn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.eclipse.jnosql.communication.document.DocumentQuery.select;


/**
 * The template method to {@link jakarta.nosql.mapping.Repository} to Document
 *
 * @param <T> the class type
 */
public abstract class AbstractDocumentRepositoryProxy<T> extends BaseDocumentRepository implements InvocationHandler {

    protected abstract Repository getRepository();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getEntityMetadata().getType();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                DocumentQuery query = getQuery(method, args);
                return executeQuery(method, args, typeClass, query);
            case FIND_ALL:
                DocumentQuery queryFindAll = select().from(getEntityMetadata().getName()).build();
                return executeQuery(method, args, typeClass, getQuerySorts(args, queryFindAll));
            case DELETE_BY:
                DocumentDeleteQuery documentDeleteQuery = getDeleteQuery(method, args);
                getTemplate().delete(documentDeleteQuery);
                return null;
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
