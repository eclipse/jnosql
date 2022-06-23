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
package org.eclipse.jnosql.mapping.query;

import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * It defines the operation that might be from the Method
 */
public enum RepositoryType {

    DEFAULT, FIND_BY, DELETE_BY, UNKNOWN, OBJECT_METHOD, JNOSQL_QUERY, FIND_ALL;

    private static final Predicate<Class<?>> IS_REPOSITORY_METHOD =  Predicate.<Class<?>>isEqual(Repository.class);


    /**
     * Returns a operation type from the {@link Method}
     *
     * @param method the method
     * @return a repository type
     */
    public static RepositoryType of(Method method) {
        Objects.requireNonNull(method, "method is required");
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return OBJECT_METHOD;
        }
        if (IS_REPOSITORY_METHOD.test(declaringClass)) {
            return DEFAULT;
        }
        if (Objects.nonNull(method.getAnnotation(Query.class))) {
            return JNOSQL_QUERY;
        }

        String methodName = method.getName();
        if ("findAll".equals(methodName)) {
            return FIND_ALL;
        } else if (methodName.startsWith("findBy")) {
            return FIND_BY;
        } else if (methodName.startsWith("deleteBy")) {
            return DELETE_BY;
        }
        return UNKNOWN;
    }
}
