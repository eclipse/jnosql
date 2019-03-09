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
import org.jnosql.artemis.PreparedStatement;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The converter within the return method at Repository class.
 */
enum DynamicReturnConverter {

    INSTANCE;


    /**
     * Converts the entity from the Method return type.
     *
     * @param dynamic the information about the method and return source
     * @return the conversion result
     * @throws NullPointerException when the dynamic is null
     */
    public Object convert(DynamicReturn dynamic) {

        Method method = dynamic.getMethod();
        Class<?> typeClass = dynamic.typeClass();
        Class<?> returnType = method.getReturnType();

        if (typeClass.equals(returnType)) {
            Optional<Object> optional = dynamic.singleResult();
            return optional.orElse(null);

        } else if (Optional.class.equals(returnType)) {
            return dynamic.singleResult();
        } else if (List.class.equals(returnType)
                || Iterable.class.equals(returnType)
                || Collection.class.equals(returnType)) {
            return dynamic.list();
        } else if (Set.class.equals(returnType)) {
            return new HashSet<>(dynamic.list());
        } else if (Queue.class.equals(returnType)) {
            return new PriorityQueue<>(dynamic.list());
        } else if (Stream.class.equals(returnType)) {
            return dynamic.list().stream();
        } else if (Deque.class.equals(returnType)) {
            return new ArrayDeque<>(dynamic.list());
        } else if (NavigableSet.class.equals(returnType) || SortedSet.class.equals(returnType)) {
            checkImplementsComparable(typeClass);
            return new TreeSet<>(dynamic.list());
        }

        return dynamic.list();
    }

    private void checkImplementsComparable(Class<?> typeClass) {
        if (!Comparable.class.isAssignableFrom(typeClass)) {
            throw new DynamicQueryException(String.format("To use either NavigableSet or SortedSet the entity %s must implement Comparable.", typeClass));
        }
    }

    /**
     * Reads and execute JNoSQL query from the Method that has the {@link org.jnosql.artemis.Query} annotation
     *
     * @return the result from the query annotation
     */
    public Object convert(DynamicQueryMethodReturn dynamicQueryMethod) {
        Method method = dynamicQueryMethod.getMethod();
        Object[] args = dynamicQueryMethod.getArgs();
        Function<String, List<?>> queryConverter = dynamicQueryMethod.getQueryConverter();
        Function<String, PreparedStatement> prepareConverter = dynamicQueryMethod.getPrepareConverter();
        Class<?> typeClass = dynamicQueryMethod.getTypeClass();

        String value = RepositoryReflectionUtils.INSTANCE.getQuery(method);


        Map<String, Object> params = RepositoryReflectionUtils.INSTANCE.getParams(method, args);
        List<?> entities;
        if (params.isEmpty()) {
            entities = queryConverter.apply(value);
        } else {
            PreparedStatement prepare = prepareConverter.apply(value);
            params.forEach(prepare::bind);
            entities = prepare.getResultList();
        }

        Supplier<List<?>> listSupplier = () -> entities;

        Supplier<Optional<?>> singleSupplier = DynamicReturn.toSingleResult(method).apply(listSupplier);

        DynamicReturn dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withList(listSupplier)
                .withSingleResult(singleSupplier)
                .build();

        return convert(dynamicReturn);
    }
}
