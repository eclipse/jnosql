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
package org.eclipse.jnosql.mapping.semistructured;

import org.eclipse.jnosql.communication.semistructured.Element;
import jakarta.nosql.AttributeConverter;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.MappingType;
import org.eclipse.jnosql.mapping.metadata.FieldValue;
import org.eclipse.jnosql.mapping.metadata.DefaultFieldValue;
import org.eclipse.jnosql.mapping.metadata.GenericFieldMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.eclipse.jnosql.mapping.metadata.MappingType.COLLECTION;

import static org.eclipse.jnosql.mapping.metadata.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.metadata.MappingType.EMBEDDED_GROUP;
import static org.eclipse.jnosql.mapping.metadata.MappingType.ENTITY;
import static java.util.Collections.singletonList;

final class DefaultAttributeFieldValue implements AttributeFieldValue {

    private final FieldValue fieldValue;

    private DefaultAttributeFieldValue(FieldValue fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public Object value() {
        return fieldValue.value();
    }

    @Override
    public FieldMetadata field() {
        return fieldValue.field();
    }

    @Override
    public boolean isNotEmpty() {
        return fieldValue.isNotEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X, Y> List<Element> toElements(EntityConverter converter, Converters converters) {
        if (value() == null) {
            return singletonList(Element.of(getName(), null));
        } else if (EMBEDDED.equals(getType())) {
            return converter.toCommunication(value()).elements();
        } else if (ENTITY.equals(getType())|| EMBEDDED_GROUP.equals(getType())) {
            return singletonList(Element.of(getName(), converter.toCommunication(value()).elements()));
        } else if (isEmbeddableCollection()) {
            return singletonList(Element.of(getName(), getColumns(converter)));
        }
        Optional<Class<AttributeConverter<Object, Object>>> optionalConverter = field().converter();
        if (optionalConverter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters.get(field());
            return singletonList(Element.of(getName(), attributeConverter.convertToDatabaseColumn((X) value())));
        }
        return singletonList(Element.of(getName(), value()));
    }

    private List<List<Element>> getColumns(EntityConverter converter) {
        List<List<Element>> columns = new ArrayList<>();
        for (Object element : (Iterable<?>) value()) {
            columns.add(converter.toCommunication(element).elements());
        }
        return columns;
    }

    private boolean isEmbeddableCollection() {
        return COLLECTION.equals(getType()) && isEmbeddableElement();
    }

    private MappingType getType() {
        return field().mappingType();
    }

    private String getName() {
        return field().name();
    }

    private boolean isEmbeddableElement() {
        return ((GenericFieldMetadata) field()).isEmbeddable();
    }

    @Override
    public String toString() {
        return "ColumnFieldValue{" + "fieldValue=" + fieldValue +
                '}';
    }

    static AttributeFieldValue of(Object value, FieldMetadata field) {
        return new DefaultAttributeFieldValue(new DefaultFieldValue(value, field));
    }
}
