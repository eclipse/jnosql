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
package org.eclipse.jnosql.mapping.query;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.PageableRepository;
import jakarta.data.repository.Query;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * It defines the operation that might be from the Method
 */
public enum RepositoryType {

    /**
     * Methods from either {@link CrudRepository} or {@link  PageableRepository}
     */
    DEFAULT(""),
    /**
     * General query method returning the repository type.It starts with "findBy" key word
     */
    FIND_BY("findBy"),
    /**
     * Delete query method returning either no result (void) or the delete count. It starts with "deleteBy" key word
     */
    DELETE_BY("deleteBy"),
    UNKNOWN(""),
    /**
     * Methods from {@link Object}
     */
    OBJECT_METHOD(""),
    /**
     * Method that has {@link Query} annotation
     */
    JNOSQL_QUERY(""),
    /**
     * Method that has the "FindAll" keyword
     */
    FIND_ALL("findAll");

    private static final Predicate<Class<?>> IS_REPOSITORY_METHOD =  Predicate.<Class<?>>isEqual(CrudRepository.class)
            .or(Predicate.<Class<?>>isEqual(PageableRepository.class));
    private final String keyword;

    RepositoryType(String keyword) {
        this.keyword = keyword;
    }


    /**
     * Returns an operation type from the {@link Method}
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
        } else if (methodName.startsWith(FIND_BY.keyword)) {
            return FIND_BY;
        } else if (methodName.startsWith(DELETE_BY.keyword)) {
            return DELETE_BY;
        }
        return UNKNOWN;
    }
}
