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

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Traversal to manipulate {@link org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree}
 */
public interface TreeTraversal {

    /**
     * Get all the result in the traversal as Stream
     *
     * @return the entity result as {@link Stream}
     */
    Stream<EntityTree> getResult();

    /**
     * Concludes the traversal that returns a single result
     *
     * @return the entity result otherwise {@link Optional#empty()}
     * @throws NonUniqueResultException when there is more than one result
     */
    Optional<EntityTree> getSingleResult();

    /**
     * Returns the next elements in the traversal.
     * If the traversal is empty, then an {@link Optional#empty()} is returned.
     *
     * @return the entity result otherwise {@link Optional#empty()}
     */
    Optional<EntityTree> next();

    /**
     * Get the next n-number of results from the traversal.
     *
     * @param limit the limit to result
     * @return the entity result as {@link Stream}
     */
    Stream<EntityTree> next(int limit);
}
