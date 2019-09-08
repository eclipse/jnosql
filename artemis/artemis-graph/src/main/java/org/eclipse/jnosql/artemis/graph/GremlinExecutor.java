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
package org.eclipse.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class GremlinExecutor {


    private final GraphConverter converter;

    private static final ScriptEngine ENGINE = new GremlinGroovyScriptEngine();

    GremlinExecutor(GraphConverter converter) {
        this.converter = converter;
    }

    <T> Stream<T> executeGremlin(GraphTraversalSource traversalSource, String gremlin) {
        return executeGremlin(traversalSource, gremlin, Collections.emptyMap());
    }

    <T> Stream<T> executeGremlin(GraphTraversalSource traversalSource, String gremlin, Map<String, Object> params) {
        try {
            Bindings bindings = ENGINE.createBindings();
            bindings.put("g", traversalSource);
            params.forEach(bindings::put);

            Object eval = ENGINE.eval(gremlin, bindings);
            if (eval instanceof GraphTraversal) {
                return convertToStream(((GraphTraversal) eval).toStream());
            }
            if (eval instanceof Iterable) {
                return convertToStream(StreamSupport.stream(((Iterable) eval).spliterator(), false));
            }
            return Stream.of((T) eval);
        } catch (ScriptException e) {
            throw new GremlinQueryException("There is an error when executed the gremlin query: " + gremlin, e);
        }
    }

    private <T> Stream<T> convertToStream(Stream<?> stream) {
        return stream.map(this::getElement).map(e -> (T) e);
    }

    private Object getElement(Object entity) {
        if (entity instanceof Vertex) {
            return converter.toEntity((Vertex) entity);
        }

        if (entity instanceof Edge) {
            return converter.toEdgeEntity((Edge) entity);
        }
        return entity;
    }


}
