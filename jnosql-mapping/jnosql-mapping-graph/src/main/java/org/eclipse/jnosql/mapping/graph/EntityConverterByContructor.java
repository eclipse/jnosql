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

import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.ConstructorBuilder;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.ParameterMetaData;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

final class GraphContructorBuilder<T> implements Supplier<T> {

    private final EntityMetadata mapping;
    private final Vertex vertex;

    private final Converters converters;

    private GraphContructorBuilder(EntityMetadata mapping, Vertex vertex, Converters converters) {
        this.mapping = mapping;
        this.vertex = vertex;
        this.converters = converters;
    }

    @Override
    public T get() {
        ConstructorBuilder builder = ConstructorBuilder.of(mapping.constructor());
        List<Property<?>> properties = vertex.keys().stream()
                .map(k -> DefaultProperty.of(k, vertex.value(k)))
                .collect(toList());
        for (ParameterMetaData parameter : builder.getParameters()) {

            if (parameter.isId()) {
                Object vertexId = vertex.id();
            } else {
                Optional<Property<?>> property = properties.stream()
                        .filter(c -> c.key().equals(parameter.getName()))
                        .findFirst();
                property.ifPresentOrElse(p -> parameter.getConverter().ifPresentOrElse(c -> {
                    Object value = this.converters.get(c).convertToEntityAttribute(p.value());
                    builder.add(value);
                }, () -> {
                    Value value = Value.of(p.value());
                    builder.add(value.get(parameter.getType()));
                }), builder::addEmptyParameter);

            }
        }
        return builder.build();
    }

    static <T> GraphContructorBuilder<T> of(EntityMetadata mapping, Vertex vertex, Converters converters) {
        return new GraphContructorBuilder<>(mapping, vertex, converters);
    }
}