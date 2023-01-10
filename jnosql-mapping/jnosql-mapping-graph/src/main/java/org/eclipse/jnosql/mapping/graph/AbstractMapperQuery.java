/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.nosql.mapping.Converters;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.util.ConverterUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.tinkerpop.gremlin.structure.T.id;

class AbstractMapperQuery {

    protected boolean negate;

    protected boolean and;

    protected String name;

    protected transient final EntityMetadata mapping;

    protected transient final Converters converters;

    protected transient final GraphTraversal<Vertex, Vertex> traversal;

    protected transient final GraphConverter converter;

    protected transient GraphTraversal<Object, Object> condition;

    AbstractMapperQuery(EntityMetadata mapping, Converters converters,
                        GraphTraversal<Vertex, Vertex> traversal, GraphConverter converter) {
        this.mapping = mapping;
        this.converters = converters;
        this.traversal = traversal;
        this.converter = converter;
    }

    protected <T> void eqImpl(T value) {
        requireNonNull(value, "value is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.eq(getValue(value))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.eq(getValue(value))));
        }
    }

    protected <T> void gtImpl(T value) {
        requireNonNull(value, "value is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.gt(getValue(value))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.gt(getValue(value))));
        }
    }

    protected <T> void gteImpl(T value) {
        requireNonNull(value, "value is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.gte(getValue(value))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.gte(getValue(value))));
        }
    }

    protected <T> void ltImpl(T value) {
        requireNonNull(value, "value is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.lt(getValue(value))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.lt(getValue(value))));
        }
    }

    protected <T> void lteImpl(T value) {
        requireNonNull(value, "value is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.lte(getValue(value))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.lte(getValue(value))));
        }
    }

    protected <T> void betweenImpl(T valueA, T valueB) {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        if (isKey(name)) {
            appendCondition(__.has(id, P.between(getValue(valueA), getValue(valueB))));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.between(getValue(valueA), getValue(valueB))));
        }
    }

    protected <T> void inImpl(Iterable<T> values) {

        requireNonNull(values, "values is required");
        List<Object> convertedValues = StreamSupport.stream(values.spliterator(), false)
                .map(this::getValue).collect(toList());
        if (isKey(name)) {
            appendCondition(__.has(id, P.within(convertedValues)));
        } else {
            appendCondition(__.has(mapping.getColumnField(name), P.within(convertedValues)));
        }
    }

    protected void likeImpl(String value) {
        throw new UnsupportedOperationException("The Graph database/Apache Tinkerpop does not have support to like operation");
    }

    protected Object getValue(Object value) {
        return ConverterUtil.getValue(value, mapping, name, converters);
    }

    private boolean isKey(String name) {
        Optional<FieldMapping> field = mapping.getFieldMapping(name);
        return field.map(FieldMapping::isId).orElse(false);
    }

    private void appendCondition(GraphTraversal<Object, Object> newCondition) {
        if (nonNull(condition)) {
            if (and) {
                this.condition = __.and(condition, checkNegation(newCondition));
            } else {
                this.condition = __.or(condition, checkNegation(newCondition));
            }
        } else {
            this.condition = checkNegation(newCondition);
        }
        this.negate = false;
        this.name = null;
    }

    private GraphTraversal<Object, Object> checkNegation(GraphTraversal<Object, Object> condition) {
        if (negate) {
            return __.not(condition);
        }
        return condition;
    }
}
