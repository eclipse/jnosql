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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.mapping.InstanceProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The default implementation of {@link InstanceProducer}
 */
@ApplicationScoped
class DefaultInstanceProducer implements InstanceProducer {

    @Inject
    private Reflections reflections;

    @Override
    public <T> T create(Class<T> instanceType) {
        Objects.requireNonNull(instanceType, "instanceType is required");
        return reflections.newInstance(instanceType);
    }

}
