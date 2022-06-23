/*
 *  Copyright (c) 2017 Otávio Santana and others
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

import org.apache.tinkerpop.gremlin.structure.Graph;

/**
 * The producer of {@link GraphTemplate}
 */
public interface GraphTemplateProducer {

    /**
     * creates a {@link GraphTemplate}
     *
     * @param <T>   the GraphTemplate instance
     * @param graph the graph
     * @return a new instance
     * @throws NullPointerException when collectionManager is null
     */
    <T extends GraphTemplate> T get(Graph graph);


    /**
     * creates a {@link GraphTemplate}
     *
     * @param <T>      the GraphTemplate instance
     * @param supplier the supplier
     * @return a new instance
     * @throws NullPointerException when supplier is null
     */
    <T extends GraphTemplate> T get(GraphTraversalSourceSupplier supplier);
}
