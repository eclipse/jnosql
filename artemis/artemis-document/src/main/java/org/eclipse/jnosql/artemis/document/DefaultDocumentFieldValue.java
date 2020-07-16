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
package org.eclipse.jnosql.artemis.document;

import jakarta.nosql.document.Document;
import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.artemis.reflection.FieldMapping;
import org.eclipse.jnosql.artemis.reflection.FieldType;
import org.eclipse.jnosql.artemis.reflection.FieldValue;
import org.eclipse.jnosql.artemis.reflection.DefaultFieldValue;
import org.eclipse.jnosql.artemis.reflection.GenericFieldMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.eclipse.jnosql.artemis.reflection.FieldType.COLLECTION;
import static org.eclipse.jnosql.artemis.reflection.FieldType.EMBEDDED;
import static org.eclipse.jnosql.artemis.reflection.FieldType.EMBEDDED_ENTITY;
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


    public List<Document> toDocument(DocumentEntityConverter converter, Converters converters) {
        if (EMBEDDED.equals(getType())) {
            return converter.toDocument(getValue()).getDocuments();
        }  else if (EMBEDDED_ENTITY.equals(getType())) {
            return singletonList(Document.of(getName(), converter.toDocument(getValue()).getDocuments()));
        } else if (isEmbeddableCollection()) {
            return singletonList(Document.of(getName(), getDocuments(converter)));
        }
        Optional<Class<? extends AttributeConverter>> optionalConverter = getField().getConverter();
        if (optionalConverter.isPresent()) {
            AttributeConverter attributeConverter = converters.get(optionalConverter.get());
            return singletonList(Document.of(getName(), attributeConverter.convertToDatabaseColumn(getValue())));
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

    private FieldType getType() {
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
