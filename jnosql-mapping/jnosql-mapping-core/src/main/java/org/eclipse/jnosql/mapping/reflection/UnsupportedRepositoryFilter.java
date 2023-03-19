/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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


import jakarta.nosql.Entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A filter to remove any Repository that Eclipse JNoSQL does not support. It will check the first parameter
 * on the repository, and if the entity has not had an unsupported annotation,
 * it will return false and true to supported Repository.
 */
enum UnsupportedRepositoryFilter implements Predicate<Class<?>> {

    INSTANCE;

    @Override
    public boolean test(Class<?> type) {
        Optional<Class<?>> entity = getEntity(type);
        return entity.map(c -> c.getAnnotation(Entity.class) == null)
                .orElse(true);
    }

    private Optional<Class<?>> getEntity(Class<?> repository) {
        Type[] interfaces = repository.getGenericInterfaces();
        if (interfaces.length == 0) {
            return Optional.empty();
        }
        ParameterizedType param = (ParameterizedType) interfaces[0];
        Type[] arguments = param.getActualTypeArguments();
        if (arguments.length == 0) {
            return Optional.empty();
        }
        Type argument = arguments[0];
        if (argument instanceof Class<?> entity) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
