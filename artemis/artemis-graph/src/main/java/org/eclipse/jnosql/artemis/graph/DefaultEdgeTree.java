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

import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.Edge;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The default implementation of {@link EdgeTree}
 */
final class DefaultEdgeTree implements EdgeTree {

    private final GraphConverter converter;

    private final Tree<Edge> tree;

    DefaultEdgeTree(GraphConverter converter, Tree<Edge> tree) {
        this.converter = converter;
        this.tree = tree;
    }

    @Override
    public Stream<EdgeEntity> getLeaf() {
        return tree.getLeafObjects()
                .stream()
                .map(converter::toEdgeEntity);
    }

    @Override
    public Stream<EdgeEntity> getRoots() {
        return tree.keySet()
                .stream()
                .map(converter::toEdgeEntity);
    }

    @Override
    public <K> Stream<Map.Entry<K, EdgeEntity>> getRootsIds() {
        return tree.keySet().stream()
                .map(v -> TreeEntry.of(v, converter));
    }

    @Override
    public <T> Optional<EdgeTree> getTreeFromRoot(T id) {
        Objects.requireNonNull(id, "id is required");
        return tree.keySet().stream()
                .filter(v -> id.equals(v.id()))
                .findFirst()
                .map(v -> tree.get(v))
                .map(t -> new DefaultEdgeTree(converter, t));
    }

    @Override
    public Stream<EdgeTree> getLeafTrees() {
        return tree.getLeafTrees()
                .stream()
                .map(t -> new DefaultEdgeTree(converter, t));
    }

    @Override
    public Stream<EdgeTree> getTreesAtDepth(int depth) {
        return tree.getTreesAtDepth(depth)
                .stream()
                .map(t -> new DefaultEdgeTree(converter, t));
    }

    @Override
    public Stream<EdgeEntity> getLeafsAtDepth(int depth) {
        return tree.getObjectsAtDepth(depth)
                .stream()
                .map(converter::toEdgeEntity);
    }

    @Override
    public boolean isLeaf() {
        return tree.isLeaf();
    }
}
