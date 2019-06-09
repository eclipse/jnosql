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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.jnosql.artemis.PreparedStatement;
import org.jnosql.diana.NonUniqueResultException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class DefaultPreparedStatement implements PreparedStatement {

    private final GremlinExecutor executor;

    private final String gremlin;

    private final Map<String, Object> params = new HashMap<>();

    private final GraphTraversalSource traversalSource;

    DefaultPreparedStatement(GremlinExecutor executor, String gremlin, GraphTraversalSource traversalSource) {
        this.executor = executor;
        this.gremlin = gremlin;
        this.traversalSource = traversalSource;
    }


    @Override
    public PreparedStatement bind(String name, Object value) {
        requireNonNull(name, "name is required");
        requireNonNull(value, "value is required");
        params.put(name, value);
        return this;
    }

    @Override
    public <T> List<T> getResultList() {
        return executor.executeGremlin(traversalSource, gremlin, params);
    }

    @Override
    public <T> Optional<T> getSingleResult() {
        List<T> entities = getResultList();
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.ofNullable(entities.get(0));
        }
        throw new NonUniqueResultException("There is more than one result found in the gremlin query: " + gremlin);
    }
}
