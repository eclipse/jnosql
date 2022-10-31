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

import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A wrapper of {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree} to manipulate entities classes.
 */
public interface EntityTree {

    /**
     * A wrapper {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getLeafObjects}
     *
     * @param <T> the entity type
     * @return the leaf {@link Stream} of this Tree
     */
    <T> Stream<T> getLeaf();

    /**
     * A wrapper {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#keySet()}
     *
     * @param <T> the entity type
     * @return the leaf {@link Stream} of this Tree
     */
    <T> Stream<T> getRoots();

    /**
     * An {@link Entry} where the key is the ID and the value is Entity
     *
     * @param <K> the key type
     * @param <V> the entity type
     * @return the {@link Stream} of {@link Entry} of the root of the tree
     */
    <K, V> Stream<Entry<K, V>> getRootsIds();

    /**
     * Returns tree from the root id
     *
     * @param id  the id
     * @param <T> the id type
     * @return the entity if it is a leaf it will return {@link Optional#isPresent()}
     */
    <T> Optional<EntityTree> getTreeFromRoot(T id);

    /**
     * Returns a {@link Stream} of  it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getLeafTrees}
     *
     * @return a stream of
     */
    Stream<EntityTree> getLeafTrees();

    /**
     * Returns a {@link Stream} of  it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getTreesAtDepth(int)}
     *
     * @param depth the depth
     * @return a {@link Stream} of
     */
    Stream<EntityTree> getTreesAtDepth(int depth);

    /**
     * Returns a {@link Stream} of  it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getObjectsAtDepth(int)}
     *
     * @param depth the depth
     * @param <T>   the entity type
     * @return a {@link Stream} of entities
     */
    <T> Stream<T> getLeafsAtDepth(int depth);

    /**
     * It is a wrapper of {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#isLeaf()}
     *
     * @return true if is leaf or not
     */
    boolean isLeaf();
}
