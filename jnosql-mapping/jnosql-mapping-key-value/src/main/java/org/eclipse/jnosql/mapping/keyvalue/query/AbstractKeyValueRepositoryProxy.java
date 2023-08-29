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
package org.eclipse.jnosql.mapping.keyvalue.query;

import jakarta.data.repository.PageableRepository;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.DynamicQueryException;
import org.eclipse.jnosql.mapping.query.RepositoryType;
import org.eclipse.jnosql.mapping.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.repository.ThrowingSupplier;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractKeyValueRepositoryProxy<T> implements InvocationHandler {


    protected abstract PageableRepository getRepository();

    protected abstract KeyValueTemplate getTemplate();

    protected abstract Class<T> getType();

    protected abstract Class<?> repositoryType();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method, repositoryType());
        switch (type) {
            case DEFAULT -> {
                return unwrapInvocationTargetException(() -> method.invoke(getRepository(), args));
            }
            case OBJECT_METHOD -> {
                return unwrapInvocationTargetException(() -> method.invoke(this, args));
            }
            case DEFAULT_METHOD -> {
                return unwrapInvocationTargetException(() -> InvocationHandler.invokeDefault(instance, method, args));
            }
            case QUERY -> {
                Class<?> typeClass = getType();
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q, typeClass))
                        .withQueryConverter(q -> getTemplate().query(q, typeClass)).build();
                return methodReturn.execute();
            }
            case CUSTOM_REPOSITORY -> {
                Object customRepository = CDI.current().select(method.getDeclaringClass()).get();
                return unwrapInvocationTargetException(() -> method.invoke(customRepository, args));
            }
            default -> throw new DynamicQueryException("Key Value repository does not support query method");
        }
    }

    private Object unwrapInvocationTargetException(ThrowingSupplier<Object> supplier) throws Throwable {
        try {
            return supplier.get();
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
