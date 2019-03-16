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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.jnosql.artemis.Page;
import org.jnosql.artemis.Pagination;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

public final class GraphPage<T> implements Page<T> {

    private final Pagination pagination;

    private final GraphConverter converter;

    private final GraphTraversal<?, ?> graphTraversal;

    private final List<T> entities;

    GraphPage(Pagination pagination, GraphConverter converter, GraphTraversal<?, ?> graphTraversal) {
        this.pagination = pagination;
        this.converter = converter;
        this.graphTraversal = graphTraversal;
        this.entities = (List<T>) graphTraversal
                .next((int) pagination.getLimit()).stream()
                .map(converter::toVertex)
                .collect(Collectors.toList());
    }


    @Override
    public Pagination getPagination() {
        return pagination.unmodifiable();
    }

    @Override
    public Page<T> next() {
        return new GraphPage<>(pagination, converter, graphTraversal);
    }

    @Override
    public List<T> getContent() {
        return entities;
    }

    @Override
    public <C extends Collection<T>> C getContent(Supplier<C> collectionFactory) {
        requireNonNull(collectionFactory, "collectionFactory is required");
        return entities.stream().collect(toCollection(collectionFactory));
    }

    @Override
    public Stream<T> get() {
        return entities.stream();
    }
}
