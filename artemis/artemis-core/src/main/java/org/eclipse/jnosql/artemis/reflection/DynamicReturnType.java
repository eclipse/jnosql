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
package org.eclipse.jnosql.artemis.reflection;

import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Page;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

/**
 * An enumeration of the type supported from DynamicType.
 */
enum DynamicReturnType {

    INSTANCE(null) {
        @Override
        public boolean isCompatible(Class<?> entityClass, Class<?> returnType) {
            return entityClass.equals(returnType);
        }
    }, OPTIONAL(Optional.class),
    LIST(List.class),
    ITERABLE(Iterable.class),
    COLLECTION(Collection.class),
    SET(Set.class),
    QUEUE(Queue.class),
    DEQUE(Deque.class),
    NAVIGABLE_SET(NavigableSet.class) {
        @Override
        public void validate(Class<?> typeClass) {
            checkImplementsComparable(typeClass);
        }
    },
    SORTED_SET(SortedSet.class) {
        @Override
        public void validate(Class<?> typeClass) {
            checkImplementsComparable(typeClass);
        }
    },
    STREAM(Stream.class),
    PAGE(Page.class),
    DEFAULT(Void.class);

    private final Class<?> typeClass;

    DynamicReturnType(Class<?> clazz) {
        this.typeClass = clazz;
    }

    public boolean isCompatible(Class<?> entityClass, Class<?> returnType) {
        return typeClass.equals(returnType);
    }

    public void validate(Class<?> typeClass) {

    }

    static DynamicReturnType of(Class<?> typeClass, Class<?> returnType) {
        DynamicReturnType type = Stream.of(DynamicReturnType.values())
                .filter(d -> d.isCompatible(typeClass, returnType))
                .findFirst()
                .orElse(DEFAULT);
        type.validate(typeClass);
        return type;
    }


    void checkImplementsComparable(Class<?> typeClass) {
        if (!Comparable.class.isAssignableFrom(typeClass)) {
            throw new DynamicQueryException(String.format("To use either NavigableSet or SortedSet the entity %s must implement Comparable.", typeClass));
        }
    }

}
