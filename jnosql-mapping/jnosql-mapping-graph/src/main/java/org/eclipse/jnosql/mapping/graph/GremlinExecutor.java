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

import org.apache.tinkerpop.gremlin.jsr223.GremlinLangScriptEngine;
import org.apache.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.graph.CommunicationEntityConverter;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class GremlinExecutor {
    private final EntityConverter converter;

    private static final GremlinScriptEngine ENGINE = new GremlinLangScriptEngine();

    GremlinExecutor(EntityConverter converter) {
        this.converter = converter;
    }

    <T> Stream<T> executeGremlin(GraphTraversalSource traversalSource, String gremlin) {
        return executeGremlin(traversalSource, gremlin, Collections.emptyMap());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    <T> Stream<T> executeGremlin(GraphTraversalSource traversalSource, String gremlin, Map<String, Object> params) {
        try {
            Bindings bindings = ENGINE.createBindings();
            bindings.put("g", traversalSource);


            String query = GremlinParamParser.INSTANCE.apply(gremlin, params);
            Object eval = ENGINE.eval(query, bindings);
            if (eval instanceof GraphTraversal graphTraversal) {
                return convertToStream(graphTraversal.toStream());
            }
            if (eval instanceof Iterable iterable) {
                return convertToStream(StreamSupport.stream(iterable.spliterator(), false));
            }
            if (eval instanceof Stream stream) {
                return convertToStream(stream);
            }
            return Stream.of((T) eval);
        } catch (ScriptException e) {
            throw new GremlinQueryException("There is an error when executed the gremlin query: " + gremlin, e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<T> convertToStream(Stream<?> stream) {
        return stream.map(this::getElement).map(e -> (T) e);
    }

    private Object getElement(Object entity) {
        if (entity instanceof Vertex vertex) {
            return converter.toEntity(CommunicationEntityConverter.INSTANCE.apply(vertex));
        }

        if (entity instanceof Edge edge) {
            return EdgeEntity.of(converter, edge);
        }
        return entity;
    }
}
