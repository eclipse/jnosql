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
package org.eclipse.jnosql.mapping.graph;

import jakarta.data.exceptions.NonUniqueResultException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;

/**
 * The default implementation of {@link ValueMapTraversal}
 */
class DefaultValueMapTraversal implements ValueMapTraversal {

    private final Supplier<GraphTraversal<?, ?>> supplier;
    private final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Map<Object, Object>>> flow;

    DefaultValueMapTraversal(Supplier<GraphTraversal<?, ?>> supplier, Function<GraphTraversal<?, ?>,
            GraphTraversal<Vertex, Map<Object, Object>>> flow) {
        this.supplier = supplier;
        this.flow = flow;
    }


    @Override
    public Stream<Map<String, Object>> stream() {
        return flow.apply(supplier.get()).toList().stream().map(toMap());
    }


    @Override
    public Stream<Map<String, Object>> next(int limit) {
        return flow.apply(supplier.get()).next(limit).stream().map(toMap());
    }

    @Override
    public Map<String, Object> next() {
        return flow.apply(supplier.get()).tryNext().map(toMap()).orElse(emptyMap());
    }

    @Override
    public Optional<Map<String, Object>> singleResult() {
        List<Map<String, Object>> result = resultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() == 1) {
            return Optional.of(result.get(0));
        }
        throw new NonUniqueResultException("The Edge traversal query returns more than one result");
    }

    @Override
    public List<Map<String, Object>> resultList() {
        return stream().collect(toList());
    }

    @Override
    public long count() {
        return flow.apply(supplier.get()).count().tryNext().orElse(0L);
    }

    private Function<Map<?, ?>, Map<String, Object>> toMap() {
        return map -> map
                .entrySet()
                .stream()
                .collect(Collectors.toMap(k -> k.getKey().toString(), Map.Entry::getValue));
    }
}
