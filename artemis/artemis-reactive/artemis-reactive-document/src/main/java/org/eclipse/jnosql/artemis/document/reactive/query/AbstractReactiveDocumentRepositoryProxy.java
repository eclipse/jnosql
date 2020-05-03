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
package org.eclipse.jnosql.artemis.document.reactive.query;

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.artemis.document.query.BaseDocumentRepository;
import org.eclipse.jnosql.artemis.query.RepositoryType;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepositoryType;
import org.eclipse.jnosql.artemis.repository.DynamicQueryMethodReturn;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;

import static jakarta.nosql.document.DocumentQuery.select;

public abstract class AbstractReactiveDocumentRepositoryProxy<T> extends BaseDocumentRepository implements InvocationHandler {

    protected abstract ReactiveRepository getRepository();

    protected abstract DocumentTemplate getTemplate();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = ReactiveRepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getClassInstance();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                DocumentQuery query = getQuery(method, args);
                return executeQuery(method, args, typeClass, query);
            case FIND_ALL:
                DocumentQuery queryFindAll = select().from(getClassMapping().getName()).build();
                return executeQuery(method, args, typeClass, getQuerySorts(args, queryFindAll));
            case DELETE_BY:
                DocumentDeleteQuery documentDeleteQuery = getDeleteQuery(method, args);
                Iterable<Void> iterable = () -> {
                    getTemplate().delete(documentDeleteQuery);
                    return Collections.<Void>emptyList().iterator();
                };
                return ReactiveStreams.fromIterable(iterable);
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
