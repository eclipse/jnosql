/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.configuration;

import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public abstract class AbstractConfiguration<T> implements Converter<T> {

    protected abstract T success(String value);

    private Class<T> beanType;

    {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.beanType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public T convert(String value) {
        try {
            BeanManagers.getBeanManager();
        } catch (Exception ex) {
            return getMock(beanType);
        }
        return success(value);
    }

    private <T> T getMock(Class<T> bean) {
        return (T) Proxy.newProxyInstance(
                AbstractConfiguration.class.getClassLoader(),
                new Class[]{bean}, new MockInvocationHandler());
    }

    private static class MockInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            throw new UnsupportedOperationException("This mock is for validation proposal.");
        }
    }
}
