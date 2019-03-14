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
package org.jnosql.artemis.reflection;

import org.jnosql.artemis.DynamicQueryException;

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

    INSTANCE, OPTIONAL, LIST, ITERABLE, COLLECTION, SET, QUEUE, DEQUE, NAVIGABLE_SET, SORTED_SET, STREAM, DEFAULT;


    static DynamicReturnType of(Class<?> typeClass, Class<?> returnType) {
        if (typeClass.equals(returnType)) {
            return INSTANCE;
        } else if (Optional.class.equals(returnType)) {
            return OPTIONAL;
        } else if (List.class.equals(returnType)
                || Iterable.class.equals(returnType)
                || Collection.class.equals(returnType)) {
            return LIST;
        } else if (Set.class.equals(returnType)) {
            return SET;
        } else if (Queue.class.equals(returnType)) {
            return DEQUE;
        } else if (Stream.class.equals(returnType)) {
            return STREAM;
        } else if (Deque.class.equals(returnType)) {
            return DEQUE;
        } else if (NavigableSet.class.equals(returnType) || SortedSet.class.equals(returnType)) {
            checkImplementsComparable(typeClass);
            return NAVIGABLE_SET;
        }
        return DEFAULT;
    }

    private static void checkImplementsComparable(Class<?> typeClass) {
        if (!Comparable.class.isAssignableFrom(typeClass)) {
            throw new DynamicQueryException(String.format("To use either NavigableSet or SortedSet the entity %s must implement Comparable.", typeClass));
        }
    }

}
