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
package org.eclipse.jnosql.artemis.graph;

import jakarta.nosql.NonUniqueResultException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The default implementation of {@link TreeTraversal}
 */
final class DefaultTreeTraversal implements TreeTraversal {

    private final Supplier<GraphTraversal<?, ?>> supplier;
    private final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Tree>> flow;
    private final GraphConverter converter;

    DefaultTreeTraversal(Supplier<GraphTraversal<?, ?>> supplier,
                         Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Tree>> flow,
                         GraphConverter converter) {
        this.supplier = supplier;
        this.flow = flow;
        this.converter = converter;
    }


    @Override
    public Stream<EntityTree> getResult() {
        return flow.apply(supplier.get()).toStream()
                .map(this::toEntityTree);
    }

    @Override
    public Optional<EntityTree> getSingleResult() {
        final Stream<EntityTree> stream = getResult();
        final Iterator<EntityTree> iterator = stream.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final EntityTree entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("The Tree traversal query returns more than one result");
    }

    @Override
    public Optional<EntityTree> next() {
        Optional<Tree> tree = flow.apply(supplier.get()).tryNext();
        return tree.map(this::toEntityTree);
    }

    @Override
    public Stream<EntityTree> next(int limit) {
        return flow.apply(supplier.get())
                .next(limit).stream()
                .map(this::toEntityTree);
    }

    private EntityTree toEntityTree(Tree<?> vertexTree) {
        return new DefaultEntityTree(converter, (Tree<Vertex>) vertexTree);
    }

}
