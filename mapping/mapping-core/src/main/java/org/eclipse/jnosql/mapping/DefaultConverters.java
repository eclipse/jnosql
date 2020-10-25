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
package org.eclipse.jnosql.mapping;


import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.InstanceProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.Objects;

/**
 * The Default implementation to {@link Converters}
 */
@ApplicationScoped
class DefaultConverters implements Converters {

    @Inject
    private BeanManager beanManager;

    @Inject
    private InstanceProducer instanceProducer;

    @Override
    public <X, Y> AttributeConverter<X, Y> get(Class<? extends AttributeConverter<X, Y>> converterClass) {
        Objects.requireNonNull(converterClass, "The converterClass is required");
        return getInstance(converterClass);
    }

    private <T> T getInstance(Class<T> clazz) {
        Iterator<Bean<?>> iterator = beanManager.getBeans(clazz).iterator();
        if (iterator.hasNext()) {
            Bean<T> bean = (Bean<T>) iterator.next();
            CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
            return (T) beanManager.getReference(bean, clazz, ctx);
        } else {
            return instanceProducer.create(clazz);
        }

    }

    @Override
    public String toString() {
        return "DefaultConverters{" +
                "beanManager=" + beanManager +
                '}';
    }
}
