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

import org.eclipse.jnosql.mapping.AttributeConverter;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.FieldMetadata;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.eclipse.jnosql.mapping.metadata.MappingType.EMBEDDED;

final class FieldGraph {

    private final Object value;

    private final FieldMetadata field;

    private FieldGraph(Object value, FieldMetadata field) {
        this.value = value;
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public FieldMetadata getField() {
        return field;
    }

    public boolean isNotEmpty() {
        return value != null;
    }

    public boolean isId() {
        return field.isId();
    }

    public boolean isNotId() {
        return !isId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldGraph)) {
            return false;
        }
        FieldGraph that = (FieldGraph) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, field);
    }

    @Override
    public String toString() {
        return  "FieldGraph{" + "value=" + value +
                ", field=" + field +
                '}';
    }

    public static FieldGraph of(Object value, FieldMetadata field) {
        return new FieldGraph(value, field);
    }

    public <X, Y> List<Property<?>> toElements(GraphConverter converter, Converters converters) {
        if (EMBEDDED.equals(field.mappingType())) {
            return converter.getProperties(value);
        }

        Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = field.converter();
        if (optionalConverter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters.get(optionalConverter.get());
            return singletonList(DefaultProperty.of(field.name(), attributeConverter.convertToDatabaseColumn((X) value)));
        }
        return singletonList(DefaultProperty.of(field.name(), value));
    }

    public <X, Y> Property toElement(Converters converters) {
        Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = field.converter();
        if (optionalConverter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters.get(optionalConverter.get());
            return DefaultProperty.of(field.name(), attributeConverter.convertToDatabaseColumn((X) value));
        }
        return DefaultProperty.of(field.name(), value);
    }

}
