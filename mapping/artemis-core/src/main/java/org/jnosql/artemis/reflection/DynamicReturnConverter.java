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

import org.jnosql.artemis.PreparedStatement;
import org.jnosql.artemis.Query;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The converter within the return method at Repository class.
 */
public enum DynamicReturnConverter {

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
        }

        return dynamic.list();
    }

    public Object getJnosqlQuery(Method method, Object[] args, Class<?> typeClass) {
        String value = method.getAnnotation(Query.class).value();
        ParamsUtils.
        Map<String, Object> params = getParams(method, args);
        List<T> entities;
        if (params.isEmpty()) {
            entities = getTemplate().query(value);
        } else {
            PreparedStatement prepare = getTemplate().prepare(value);
            params.forEach(prepare::bind);
            entities = prepare.getResultList();
        }

        Supplier<List<?>> listSupplier = () -> entities;

        Supplier<Optional<?>> singleSupplier = DefaultDynamicReturn.toSingleResult(method).apply(listSupplier);

        DefaultDynamicReturn dynamicReturn = DefaultDynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withList(listSupplier)
                .withSingleResult(singleSupplier)
                .build();

        return returnConverter.convert(dynamicReturn);
    }
}
