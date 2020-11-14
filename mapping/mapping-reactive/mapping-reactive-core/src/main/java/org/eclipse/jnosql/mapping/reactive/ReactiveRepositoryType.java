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
package org.eclipse.jnosql.mapping.reactive;

import org.eclipse.jnosql.mapping.query.RepositoryType;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * It is a wrapper of {@link RepositoryType}
 */
public final class ReactiveRepositoryType {

    private static final Predicate<Class<?>> IS_REPOSITORY_METHOD = Predicate.<Class<?>>isEqual(ReactiveRepository.class);

    private ReactiveRepositoryType() {
    }

    /**
     * Returns a operation type from the {@link Method}
     *
     * @param method the method
     * @return a repository type
     */
    public static RepositoryType of(Method method) {
        Objects.requireNonNull(method, "method is required");
        Class<?> declaringClass = method.getDeclaringClass();
        if (IS_REPOSITORY_METHOD.test(declaringClass)) {
            return RepositoryType.DEFAULT;
        }
        final RepositoryType type = RepositoryType.of(method);
        if (RepositoryType.DEFAULT.equals(type)) {
            return RepositoryType.UNKNOWN;
        }
        return type;
    }
}
