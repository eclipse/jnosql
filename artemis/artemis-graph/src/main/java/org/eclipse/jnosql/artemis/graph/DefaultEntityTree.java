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
                .map(v -> EntityTreeEntry.of(v, converter));
    }

    @Override
    public <T> Optional<EntityTree> getTreeFromRoot(T id) {
        Objects.requireNonNull(id, "id is required");
        return tree.keySet().stream()
                .filter(v -> id.equals(v.id()))
                .findFirst()
                .map(v -> tree.get(v))
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


    private static class EntityTreeEntry<K, V> implements Entry<K, V> {

        private final K key;

        private final V value;

        private EntityTreeEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException("This entry is read-only");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            EntityTreeEntry<?, ?> that = (EntityTreeEntry<?, ?>) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "EntityTreeEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        static <K, V> EntityTreeEntry<K, V> of(Vertex vertex, GraphConverter converter) {
            K key = (K) vertex.id();
            V value = converter.toEntity(vertex);
            return new EntityTreeEntry<>(key, value);
        }
    }
}
