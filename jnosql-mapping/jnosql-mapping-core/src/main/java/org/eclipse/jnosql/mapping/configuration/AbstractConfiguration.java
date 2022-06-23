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
package org.eclipse.jnosql.mapping.configuration;

import org.eclipse.jnosql.mapping.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A template method of Converter where it checks if the BeanManager is up to return the real value; otherwise, it will return a Mock instance.
 * There are several reasons to return the Mock instead of the real value when the CDI container isn't up:
 * To enable a Mock instance to validation in Eclipse MicroProfile Configurations.
 * To avoid waste of computer power to create an instance only for validation.
 *
 * @param <T> the converter type
 */
public abstract class AbstractConfiguration<T> implements Converter<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractConfiguration.class.getName());

    protected abstract T success(String value);

    private final Class<T> beanType;

    {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.beanType = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public T convert(String value) {
        try {
            BeanManagers.getBeanManager();
            BeanManagers.getInstance(Config.class);
        } catch (Exception ex) {
            LOGGER.log(Level.FINEST, "CDI container is not up, using a dump instance", ex);
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
            throw new UnsupportedOperationException("The Eclipse MicroProfile Config does not start the " +
                    "CDI container to the " + Converter.class.getName() + " implementations, " +
                    "check it with your provider");
        }
    }
}
