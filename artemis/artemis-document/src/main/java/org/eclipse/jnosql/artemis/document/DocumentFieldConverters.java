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

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.mapping.AttributeConverter;
import org.eclipse.jnosql.artemis.reflection.FieldMapping;
import org.eclipse.jnosql.artemis.reflection.GenericFieldMapping;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.eclipse.jnosql.artemis.reflection.FieldType.COLLECTION;
import static org.eclipse.jnosql.artemis.reflection.FieldType.EMBEDDED;
import static org.eclipse.jnosql.artemis.reflection.FieldType.EMBEDDED_ENTITY;

class DocumentFieldConverters {

    static class DocumentFieldConverterFactory {

        private final EmbeddedFieldConverter embeddedFieldConverter = new EmbeddedFieldConverter();
        private final DefaultConverter defaultConverter = new DefaultConverter();
        private final CollectionEmbeddableConverter embeddableConverter = new CollectionEmbeddableConverter();
        private final SubEntityConverter subEntityConverter = new SubEntityConverter();

        DocumentFieldConverter get(FieldMapping field) {
            if (EMBEDDED.equals(field.getType())) {
                return embeddedFieldConverter;
            } else if (EMBEDDED_ENTITY.equals(field.getType())) {
                return subEntityConverter;
            } else if (isCollectionEmbeddable(field)) {
                return embeddableConverter;
            } else {
                return defaultConverter;
            }
        }

        private boolean isCollectionEmbeddable(FieldMapping field) {
            return COLLECTION.equals(field.getType()) && ((GenericFieldMapping) field).isEmbeddable();
        }
    }

    private static class SubEntityConverter implements DocumentFieldConverter {

        @Override
        public <X, Y, T> void convert(T instance, List<Document> documents, Optional<Document> document,
                                FieldMapping field, AbstractDocumentEntityConverter converter) {

            if (document.isPresent()) {
                Document sudDocument = document.get();
                Object value = sudDocument.get();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    List<Document> embeddedDocument = new ArrayList<>();

                    for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                        embeddedDocument.add(Document.of(entry.getKey().toString(), entry.getValue()));
                    }
                    field.write(instance, converter.toEntity(field.getNativeField().getType(), embeddedDocument));

                } else {
                    field.write(instance, converter.toEntity(field.getNativeField().getType(),
                            sudDocument.get(new TypeReference<List<Document>>() {
                            })));
                }

            } else {
                field.write(instance, converter.toEntity(field.getNativeField().getType(), documents));
            }
        }
    }

    private static class EmbeddedFieldConverter implements DocumentFieldConverter {


        @Override
        public <X, Y, T> void convert(T instance, List<Document> documents, Optional<Document> document,
                                FieldMapping field, AbstractDocumentEntityConverter converter) {

            Field nativeField = field.getNativeField();
            Object subEntity = converter.toEntity(nativeField.getType(), documents);
            field.write(instance, subEntity);

        }
    }

    private static class DefaultConverter implements DocumentFieldConverter {

        @Override
        public <X, Y, T> void convert(T instance, List<Document> documents, Optional<Document> document,
                                FieldMapping field, AbstractDocumentEntityConverter converter) {
            Value value = document.get().getValue();

            Optional<Class<? extends AttributeConverter<X, Y>>> optionalConverter = field.getConverter();
            if (optionalConverter.isPresent()) {
                AttributeConverter<X, Y> attributeConverter = converter.getConverters().get(optionalConverter.get());
                Object attributeConverted = attributeConverter.convertToEntityAttribute((Y) value.get());
                field.write(instance, field.getValue(Value.of(attributeConverted)));
            } else {
                field.write(instance, field.getValue(value));
            }
        }
    }

    private static class CollectionEmbeddableConverter implements DocumentFieldConverter {

        @Override
        public <X, Y, T> void convert(T instance, List<Document> documents, Optional<Document> document,
                                FieldMapping field, AbstractDocumentEntityConverter converter) {
            document.ifPresent(convertDocument(instance, field, converter));
        }

        private <T> Consumer<Document> convertDocument(T instance, FieldMapping field, AbstractDocumentEntityConverter converter) {
            return document -> {
                GenericFieldMapping genericField = (GenericFieldMapping) field;
                Collection collection = genericField.getCollectionInstance();
                List<List<Document>> embeddable = (List<List<Document>>) document.get();
                for (List<Document> documentList : embeddable) {
                    Object element = converter.toEntity(genericField.getElementType(), documentList);
                    collection.add(element);
                }
                field.write(instance, collection);
            };
        }
    }


}
