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

import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.FieldMetadata;
import org.eclipse.jnosql.mapping.reflection.MappingType;
import org.eclipse.jnosql.mapping.reflection.FieldValue;
import org.eclipse.jnosql.mapping.reflection.DefaultFieldValue;
import org.eclipse.jnosql.mapping.reflection.GenericFieldMetadata;

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
    public Object value() {
        return fieldValue.value();
    }

    @Override
    public FieldMetadata field() {
        return fieldValue.field();
    }


    @Override
    public <X, Y> List<Document> toDocument(DocumentEntityConverter converter, Converters converters) {
        if (EMBEDDED.equals(getType())) {
            return converter.toDocument(value()).documents();
        }  else if (ENTITY.equals(getType())) {
            return singletonList(Document.of(getName(), converter.toDocument(value()).documents()));
        } else if (isEmbeddableCollection()) {
            return singletonList(Document.of(getName(), getDocuments(converter)));
        }
        Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = field().converter();
        if (optionalConverter.isPresent()) {
            AttributeConverter<X, Y> attributeConverter = converters.get(optionalConverter.get());
            return singletonList(Document.of(getName(), attributeConverter.convertToDatabaseColumn((X) value())));
        }
        return singletonList(Document.of(getName(), value()));
    }

    private List<List<Document>> getDocuments(DocumentEntityConverter converter) {
        List<List<Document>> documents = new ArrayList<>();
        for (Object element : (Iterable) value()) {
            documents.add(converter.toDocument(element).documents());
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
        return field().mappingType();
    }

    private boolean isEmbeddableElement() {
        return ((GenericFieldMetadata) field()).isEmbeddable();
    }

    private String getName() {
        return field().name();
    }

    static DocumentFieldValue of(Object value, FieldMetadata field) {
        return new DefaultDocumentFieldValue(new DefaultFieldValue(value, field));
    }


    @Override
    public String toString() {
        return  "DocumentFieldValue{" + "fieldValue=" + fieldValue +
                '}';
    }
}
