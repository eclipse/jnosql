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
package org.jnosql.artemis.key.query;

import org.jnosql.artemis.DynamicQueryException;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.key.KeyValueTemplate;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.DynamicQueryMethodReturn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractKeyValueRepositoryProxy<T> implements InvocationHandler {


    protected abstract Repository getRepository();

    protected abstract KeyValueTemplate getTemplate();

    protected abstract Class<T> getEntityClass();


    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);
        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case JNOSQL_QUERY:
                Class<?> typeClass = getEntityClass();
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q, typeClass))
                        .withQueryConverter(q -> getTemplate().query(q, typeClass)).build();
                return methodReturn.execute();
            default:
                throw new DynamicQueryException("Key Value repository does not support query method");
        }
    }
}
