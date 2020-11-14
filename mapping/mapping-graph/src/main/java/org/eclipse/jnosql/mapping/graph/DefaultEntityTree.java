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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The default implementation of {@link EntityTree}
 */
final class DefaultEntityTree implements EntityTree {

    private final GraphConverter converter;

    private final Tree<Vertex> tree;

    DefaultEntityTree(GraphConverter converter, Tree<Vertex> tree) {
        this.converter = converter;
        this.tree = tree;
    }

    @Override
    public <T> Stream<T> getLeaf() {
        return tree.getLeafObjects()
                .stream()
                .map(converter::toEntity);
    }

    @Override
    public <T> Stream<T> getRoots() {
        return tree.keySet()
                .stream()
                .map(converter::toEntity);
    }

    @Override
    public <K, V> Stream<Entry<K, V>> getRootsIds() {
        return tree.keySet().stream()
                .map(v -> TreeEntry.of(v, converter));
    }

    @Override
    public <T> Optional<EntityTree> getTreeFromRoot(T id) {
        Objects.requireNonNull(id, "id is required");
        return tree.keySet().stream()
                .filter(v -> id.equals(v.id()))
                .findFirst()
                .map(tree::get)
                .map(t -> new DefaultEntityTree(converter, t));
    }

    @Override
    public Stream<EntityTree> getLeafTrees() {
        return tree.getLeafTrees()
                .stream()
                .map(t -> new DefaultEntityTree(converter, t));
    }

    @Override
    public Stream<EntityTree> getTreesAtDepth(int depth) {
        return tree.getTreesAtDepth(depth)
                .stream()
                .map(t -> new DefaultEntityTree(converter, t));
    }

    @Override
    public <T> Stream<T> getLeafsAtDepth(int depth) {
        return tree.getObjectsAtDepth(depth)
                .stream()
                .map(converter::toEntity);
    }

    @Override
    public boolean isLeaf() {
        return tree.isLeaf();
    }
}
