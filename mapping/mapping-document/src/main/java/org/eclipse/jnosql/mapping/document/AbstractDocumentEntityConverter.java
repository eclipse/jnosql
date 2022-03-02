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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.Documents;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.document.DocumentFieldConverters.DocumentFieldConverterFactory;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.reflection.FieldType;
import org.eclipse.jnosql.mapping.reflection.FieldValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.eclipse.jnosql.mapping.reflection.FieldType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.FieldType.SUB_ENTITY;

/**
 * Template method to {@link DocumentEntityConverter}
 */
public abstract class AbstractDocumentEntityConverter implements DocumentEntityConverter {

    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    private final DocumentFieldConverterFactory converterFactory = new DocumentFieldConverterFactory();


    @Override
    public DocumentEntity toDocument(Object entityInstance) {
        requireNonNull(entityInstance, "Object is required");
        ClassMapping mapping = getClassMappings().get(entityInstance.getClass());
        DocumentEntity entity = DocumentEntity.of(mapping.getName());
        mapping.getFields().stream()
                .map(f -> to(f, entityInstance))
                .filter(FieldValue::isNotEmpty)
                .map(f -> f.toDocument(this, getConverters()))
                .flatMap(List::stream)
                .forEach(entity::add);
        return entity;

    }

    @Override
    public <T> T toEntity(Class<T> entityClass, DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(entityClass, "entityClass is required");
        return toEntity(entityClass, entity.getDocuments());

    }

    @Override
    public <T> T toEntity(T entityInstance, DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(entityInstance, "entityInstance is required");
        ClassMapping mapping = getClassMappings().get(entityInstance.getClass());
        return convertEntity(entity.getDocuments(), mapping, entityInstance);
    }

    protected <T> T toEntity(Class<T> entityClass, List<Document> documents) {
        ClassMapping mapping = getClassMappings().get(entityClass);
        T instance = mapping.newInstance();
        return convertEntity(documents, mapping, instance);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T toEntity(DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        ClassMapping mapping = getClassMappings().findByName(entity.getName());
        T instance = mapping.newInstance();
        return convertEntity(entity.getDocuments(), mapping, instance);
    }

    private <T> T convertEntity(List<Document> documents, ClassMapping mapping, T instance) {
        final Map<String, FieldMapping> fieldsGroupByName = mapping.getFieldsGroupByName();
        final List<String> names = documents.stream().map(Document::getName).sorted().collect(Collectors.toList());
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            FieldType type = fieldsGroupByName.get(k).getType();
            return EMBEDDED.equals(type) || SUB_ENTITY.equals(type);
        };

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, documents, fieldsGroupByName));

        return instance;
    }

    protected <T> Consumer<String> feedObject(T instance, List<Document> documents, Map<String, FieldMapping> fieldsGroupByName) {
        return k -> {
            Optional<Document> document = documents.stream().filter(c -> c.getName().equals(k)).findFirst();
            FieldMapping field = fieldsGroupByName.get(k);
            DocumentFieldConverter fieldConverter = converterFactory.get(field);
            if (SUB_ENTITY.equals(field.getType())) {
                feedSubEntity(instance, document, field, fieldConverter);
            } else {
                fieldConverter.convert(instance, documents, document.orElse(null), field, this);
            }
        };
    }

    private <T> void feedSubEntity(T instance, Optional<Document> document, FieldMapping field, DocumentFieldConverter fieldConverter) {
        if (document.isPresent()) {
            Object value = document.get().get();
            List<Document> documents;
            if (value instanceof Map) {
                documents = Documents.of((Map<String, ?>) value);
            } else {
                documents = (List<Document>) value;
            }
            fieldConverter.convert(instance, null, document.orElse(null), field, this);
        } else {
            return;
        }
    }

    private DocumentFieldValue to(FieldMapping field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return DefaultDocumentFieldValue.of(value, field);
    }


}
