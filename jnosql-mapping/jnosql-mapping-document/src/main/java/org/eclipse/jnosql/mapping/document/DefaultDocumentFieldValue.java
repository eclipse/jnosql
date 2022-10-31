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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.document.Document;
import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.reflection.MappingType;
import org.eclipse.jnosql.mapping.reflection.FieldValue;
import org.eclipse.jnosql.mapping.reflection.DefaultFieldValue;
import org.eclipse.jnosql.mapping.reflection.GenericFieldMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.eclipse.jnosql.mapping.reflection.MappingType.COLLECTION;
import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.MappingType.ENTITY;
import static java.util.Collections.singletonList;

final class DefaultDocumentFieldValue implements DocumentFieldValue {

    private final FieldValue fieldValue;

    private DefaultDocumentFieldValue(FieldValue fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public Object getValue() {
        return fieldValue.getValue();
    }

    @Override
    public FieldMapping getField() {
        return fieldValue.getField();
    }


    @Override
    public <X, Y> List<Document> toDocument(DocumentEntityConverter converter, Converters converters) {
        if (EMBEDDED.equals(getType())) {
            return converter.toDocument(getValue()).getDocuments();
        }  else if (ENTITY.equals(getType())) {
            return singletonList(Document.of(getName(), converter.toDocument(getValue()).getDocuments()));
        } else if (isEmbeddableCollection()) {
            return singletonList(Document.of(getName(), getDocuments(converter)));
        }
        Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = getField().getConverter();
        if (optionalConverter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters.get(optionalConverter.get());
            return singletonList(Document.of(getName(), attributeConverter.convertToDatabaseColumn((X) getValue())));
        }
        return singletonList(Document.of(getName(), getValue()));
    }

    private List<List<Document>> getDocuments(DocumentEntityConverter converter) {
        List<List<Document>> documents = new ArrayList<>();
        for (Object element : (Iterable) getValue()) {
            documents.add(converter.toDocument(element).getDocuments());
        }
        return documents;
    }

    private boolean isEmbeddableCollection() {
        return COLLECTION.equals(getType()) && isEmbeddableElement();
    }

    @Override
    public boolean isNotEmpty() {
        return fieldValue.isNotEmpty();
    }

    private MappingType getType() {
        return getField().getType();
    }

    private boolean isEmbeddableElement() {
        return ((GenericFieldMapping) getField()).isEmbeddable();
    }

    private String getName() {
        return getField().getName();
    }

    static DocumentFieldValue of(Object value, FieldMapping field) {
        return new DefaultDocumentFieldValue(new DefaultFieldValue(value, field));
    }


    @Override
    public String toString() {
        return  "DocumentFieldValue{" + "fieldValue=" + fieldValue +
                '}';
    }
}
