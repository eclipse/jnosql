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
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultDynamicReturn<T> implements DynamicReturn {

    private final Class<T> classSource;

    private final Method methodSource;

    private final Supplier<Optional<T>> singleResult;

    private final Supplier<List<T>> list;


    public DefaultDynamicReturn(Class<T> classSource, Method methodSource, Supplier<Optional<T>> singleResult,
                                Supplier<List<T>> list) {
        this.classSource = classSource;
        this.methodSource = methodSource;
        this.singleResult = singleResult;
        this.list = list;
    }

    @Override
    public Class<T> typeClass() {
        return classSource;
    }

    @Override
    public Method getMethod() {
        return methodSource;
    }

    @Override
    public Optional<T> singleResult() {
        return singleResult.get();
    }

    @Override
    public List<T> list() {
        return list.get();
    }

    public static <T> Function<Supplier<List<T>>, Optional<T>> toSingleResult(final Method method) {
        return l -> {
            List<T> entities = l.get();
            if (entities.isEmpty()) {
                return Optional.empty();
            }
            if (entities.size() == 1) {
                return Optional.ofNullable(entities.get(0));
            }
            throw new NonUniqueResultException("No unique result to the method: " + method);
        };
    }

}
