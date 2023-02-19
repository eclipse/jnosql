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
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
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
     * Delete query method returning either no result (void) or the delete count. It starts with "deleteBy" keyword
     */
    DELETE_BY("deleteBy"),
    /**
     * Method that has the "FindAll" keyword
     */
    FIND_ALL("findAll"),
    /**
     * Count projection returning a numeric result. It starts with "countBy" keyword
     */
    COUNT_BY("countBy"),
    /**
     * Exists projection, returning typically a boolean result. It starts with "existsBy" keyword
     */
    EXISTS_BY("existsBy"),
    UNKNOWN(""),
    /**
     * Methods from {@link Object}
     */
    OBJECT_METHOD(""),
    /**
     * Method that has {@link Query} annotation
     */
    QUERY("");

    private static final Predicate<Class<?>> IS_REPOSITORY_METHOD = Predicate.<Class<?>>isEqual(CrudRepository.class)
            .or(Predicate.<Class<?>>isEqual(PageableRepository.class));

    private static final Set<RepositoryType> KEY_WORLD_METHODS = EnumSet.of(FIND_BY, DELETE_BY, COUNT_BY, EXISTS_BY);
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
            return QUERY;
        }

        String methodName = method.getName();
        if (FIND_ALL.keyword.equals(methodName)) {
            return FIND_ALL;
        }
        return KEY_WORLD_METHODS.stream()
                .filter(k -> methodName.startsWith(k.keyword))
                .findFirst().orElse(UNKNOWN);
    }
}
