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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
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
        return dynamic.result().collect(Collectors.toList());
    }

    @Override
    public <T> Set<T> toSet(DynamicReturn<T> dynamic) {
        return dynamic.result().collect(Collectors.toSet());
    }

    @Override
    public <T> LinkedList<T> toLinkedList(DynamicReturn<T> dynamic) {
        return dynamic.result().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public <T> Stream<T> toStream(DynamicReturn<T> dynamic) {
        return dynamic.result();
    }

    @Override
    public <T> TreeSet<T> toTreeSet(DynamicReturn<T> dynamic) {
        return dynamic.result().collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public <T> Object toDefault(DynamicReturn<T> dynamic) {
        return dynamic.result();
    }

    @Override
    public <T> Page<T> toPage(DynamicReturn<T> dynamic) {
        throw new DynamicQueryException("There is not pagination at the method: " + dynamic.getMethod());
    }
}
