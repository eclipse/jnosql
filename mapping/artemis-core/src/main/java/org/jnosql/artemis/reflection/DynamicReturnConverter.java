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

import org.jnosql.diana.api.NonUniqueResultException;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public enum DynamicReturnConverter {

    INSTANCE;

    public Object returnObject(ColumnQuery query, ColumnTemplate template, Class typeClass, Method method) {
        Class<?> returnType = method.getReturnType();

        if (typeClass.equals(returnType)) {
            Optional<Object> optional = template.singleResult(query);
            return optional.orElse(null);

        } else if (Optional.class.equals(returnType)) {
            return template.singleResult(query);
        } else if (List.class.equals(returnType)
                || Iterable.class.equals(returnType)
                || Collection.class.equals(returnType)) {
            return template.select(query);
        } else if (Set.class.equals(returnType)) {
            return new HashSet<>(template.select(query));
        } else if (Queue.class.equals(returnType)) {
            return new PriorityQueue<>(template.select(query));
        } else if (Stream.class.equals(returnType)) {
            return template.select(query).stream();
        }

        return template.select(query);
    }

    <T> Object returnObject(List<T> entities, Class<?> typeClass, Method method) {
        Class<?> returnType = method.getReturnType();

        if (typeClass.equals(returnType)) {
            return getObject(entities, method);

        } else if (Optional.class.equals(returnType)) {
            return Optional.ofNullable(getObject(entities, method));
        } else if (List.class.equals(returnType)
                || Iterable.class.equals(returnType)
                || Collection.class.equals(returnType)) {
            return entities;
        } else if (Set.class.equals(returnType)) {
            return new HashSet<>(entities);
        } else if (Queue.class.equals(returnType)) {
            return new PriorityQueue<>(entities);
        } else if (Stream.class.equals(returnType)) {
            return entities.stream();
        }

        return entities;
    }

    private <T> Object getObject(List<T> entities, Method method) {
        if (entities.isEmpty()) {
            return null;
        }
        if (entities.size() == 1) {
            return entities.get(0);
        }
        throw new NonUniqueResultException("No unique result to the method: " + method);
    }

}
