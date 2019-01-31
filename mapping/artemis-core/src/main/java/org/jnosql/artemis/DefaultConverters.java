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
package org.jnosql.artemis;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The Default implementation to {@link Converters}
 */
@ApplicationScoped
class DefaultConverters implements Converters {

    @Inject
    private BeanManager beanManager;

    @Override
    public AttributeConverter get(Class<? extends AttributeConverter> converterClass) {
        Objects.requireNonNull(converterClass, "The converterClass is required");
        return getInstance(converterClass);
    }

    private <T> T getInstance(Class<T> clazz) {
        Bean<T> bean = (Bean<T>) beanManager.getBeans(clazz).iterator().next();
        CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultConverters{");
        sb.append("beanManager=").append(beanManager);
        sb.append('}');
        return sb.toString();
    }
}
