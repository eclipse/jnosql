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
import org.jnosql.artemis.Page;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;


/**
 * The default implementation to {@link DynamicExecutorQueryConverter}.
 * This implementation won't work with pagination
 */
final class DefaultDynamicExecutorQueryConverter implements DynamicExecutorQueryConverter {

    @Override
    public <T> T toInstance(DynamicReturn<T> dynamic) {
        Optional<T> optional = dynamic.singleResult();
        return optional.orElse(null);
    }

    @Override
    public <T> Optional<T> toOptional(DynamicReturn<T> dynamic) {
        return dynamic.singleResult();
    }

    @Override
    public <T> List<T> toList(DynamicReturn<T> dynamic) {
        return dynamic.list();
    }

    @Override
    public <T> Set<T> toSet(DynamicReturn<T> dynamic) {
        return new HashSet<>(dynamic.list());
    }

    @Override
    public <T> Queue<T> toLinkedList(DynamicReturn<T> dynamic) {
        return new LinkedList<>(dynamic.list());
    }

    @Override
    public <T> Stream<T> toStream(DynamicReturn<T> dynamic) {
        return dynamic.list().stream();
    }

    @Override
    public <T> TreeSet<T> toTreeSet(DynamicReturn<T> dynamic) {
        return new TreeSet<>(dynamic.list());
    }

    @Override
    public <T> Object toDefault(DynamicReturn<T> dynamic) {
        return dynamic.list();
    }

    @Override
    public <T> Page<T> toPage(DynamicReturn<?> dynamic) {
        throw new DynamicQueryException("There is not support to normal query");
    }
}
