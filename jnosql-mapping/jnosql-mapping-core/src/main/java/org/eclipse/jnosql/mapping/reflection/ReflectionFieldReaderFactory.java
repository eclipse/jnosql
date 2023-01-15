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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.Field;
import java.util.Objects;

@ApplicationScoped
class ReflectionFieldReaderFactory implements FieldReaderFactory {

    private Reflections reflections;

    @Inject
    ReflectionFieldReaderFactory(Reflections reflections) {
        this.reflections = reflections;
    }

    ReflectionFieldReaderFactory() {
    }

    @Override
    public FieldReader apply(Field field) {
        Objects.requireNonNull(field, "field is required");
        return bean -> reflections.getValue(bean, field);
    }
}
