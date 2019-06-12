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
package org.jnosql.artemis.document;

import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.jnosql.artemis.reflection.FieldTypeUtil;
import org.jnosql.artemis.reflection.FieldValue;
import org.jnosql.artemis.reflection.GenericFieldMapping;
import org.jnosql.diana.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.jnosql.artemis.reflection.FieldTypeUtil.COLLECTION;
import static org.jnosql.artemis.reflection.FieldTypeUtil.EMBEDDED;
import static org.jnosql.artemis.reflection.FieldTypeUtil.SUBENTITY;

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
        }  else if (SUBENTITY.equals(getType())) {
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

    private FieldTypeUtil getType() {
        return getField().getType();
    }

    private boolean isEmbeddableElement() {
        return ((GenericFieldMapping) getField()).isEmbeddable();
    }

    private String getName() {
        return getField().getName();
    }

    static DocumentFieldValue of(Object value, FieldMapping field) {
        return new DefaultDocumentFieldValue(FieldValue.of(value, field));
    }


    @Override
    public String toString() {
        return  "DocumentFieldValue{" + "fieldValue=" + fieldValue +
                '}';
    }
}
