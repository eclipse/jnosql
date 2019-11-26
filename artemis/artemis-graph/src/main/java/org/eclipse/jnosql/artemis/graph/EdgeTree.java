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

import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A wrapper of {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree} to manipulate {@link EdgeEntity} instances.
 */
public interface EdgeTree {

    /**
     * A wrapper {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getLeafObjects}
     *
     * @return the leaf {@link Stream} of this Tree
     */
    Stream<EdgeEntity> getLeaf();

    /**
     * A wrapper {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#keySet()}
     *
     * @return the leaf {@link Stream} of this Tree
     */
    Stream<EdgeEntity> getRoots();

    /**
     * An {@link Entry} where the key is the ID and the value is Entity
     *
     * @param <K> the key type
     * @return the {@link Stream} of {@link Entry} of the root of the tree
     */
    <K> Stream<Entry<K, EdgeEntity>> getRootsIds();

    /**
     * Returns tree from the root id
     *
     * @param id  the id
     * @param <T> the id type
     * @return the entity if it is a leaf it will return {@link Optional#isPresent()}
     */
    <T> Optional<EdgeTree> getTreeFromRoot(T id);

    /**
     * Returns a {@link Stream} of {@link EdgeTree} it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getLeafTrees}
     *
     * @return a stream of {@link EdgeTree}
     */
    Stream<EdgeTree> getLeafTrees();

    /**
     * Returns a {@link Stream} of {@link EdgeTree} it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getTreesAtDepth(int)}
     *
     * @param depth the depth
     * @return a {@link Stream} of {@link EdgeTree}
     */
    Stream<EdgeTree> getTreesAtDepth(int depth);

    /**
     * Returns a {@link Stream} of {@link EdgeTree} it is a wrapper of
     * {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#getObjectsAtDepth(int)}
     *
     * @param depth the depth
     * @return a {@link Stream} of entities
     */
    Stream<EdgeEntity> getLeafsAtDepth(int depth);

    /**
     * It is a wrapper of {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree#isLeaf()}
     *
     * @return true if is leaf or not
     */
    boolean isLeaf();
}
