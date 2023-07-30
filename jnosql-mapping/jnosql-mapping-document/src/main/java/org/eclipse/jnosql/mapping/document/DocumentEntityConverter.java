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

import jakarta.data.exceptions.MappingException;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.ConstructorBuilder;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldValue;
import org.eclipse.jnosql.mapping.reflection.InheritanceMetadata;
import org.eclipse.jnosql.mapping.reflection.MappingType;
import org.eclipse.jnosql.mapping.reflection.ParameterMetaData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.MappingType.ENTITY;

/**
 * This interface represents the converter between an entity and the {@link DocumentEntity}
 */
public abstract class DocumentEntityConverter {

    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    /**
     * Converts the instance entity to {@link DocumentEntity}
     *
     * @param entity the instance
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when entity is null
     */
    public DocumentEntity toDocument(Object entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().get(entity.getClass());
        DocumentEntity communication = DocumentEntity.of(mapping.name());
        mapping.fields().stream()
                .map(f -> to(f, entity))
                .filter(FieldValue::isNotEmpty)
                .map(f -> f.toDocument(this, getConverters()))
                .flatMap(List::stream)
                .forEach(communication::add);

        mapping.inheritance().ifPresent(i -> communication.add(i.discriminatorColumn(), i.discriminatorValue()));
        return communication;

    }

    /**
     * Converts a {@link DocumentEntity} to entity
     *
     * @param type   the entity class
     * @param entity the {@link DocumentEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(Class<T> type, DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");
        return toEntity(type, entity.documents());

    }

    /**
     * Converts a {@link DocumentEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type   the entity class
     * @param entity the {@link DocumentEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(T type, DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");

        if (type.getClass().isRecord()) {
            return (T) toEntity(type.getClass(), entity.documents());
        }
        EntityMetadata mapping = getEntities().get(type.getClass());
        return convertEntity(entity.documents(), mapping, type);
    }

    /**
     * Similar to {@link DocumentEntityConverter#toEntity(Class, DocumentEntity)}, but
     * search the instance type from {@link DocumentEntity#name()}
     *
     * @param entity the {@link DocumentEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link DocumentEntity}
     * @throws NullPointerException when entity is null
     */
    public <T> T toEntity(DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().findByName(entity.name());
        if (mapping.isInheritance()) {
            return mapInheritanceEntity(entity, mapping.type());
        }
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(entity.documents(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.documents(), mapping);
        }
    }

    protected <T> T toEntity(Class<T> type, List<Document> documents) {
        EntityMetadata mapping = getEntities().get(type);
        if (mapping.isInheritance()) {
            return inheritanceToEntity(documents, mapping);

        }
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(documents, mapping, instance);
        } else {
            return convertEntityByConstructor(documents, mapping);
        }
    }


    protected <T> Consumer<String> feedObject(T entity, List<Document> documents, Map<String, FieldMetadata> fieldsGroupByName) {
        return k -> {
            Optional<Document> document = documents.stream().filter(c -> c.name().equals(k)).findFirst();
            FieldMetadata field = fieldsGroupByName.get(k);
            FieldConverter fieldConverter = FieldConverter.get(field);
            if (ENTITY.equals(field.mappingType())) {
                document.ifPresent(d -> fieldConverter.convert(entity,
                        null, d, field, this));
            } else {
                fieldConverter.convert(entity, documents, document.orElse(null), field, this);
            }
        };
    }

    private <T> T convertEntityByConstructor(List<Document> documents, EntityMetadata mapping) {
        ConstructorBuilder builder = ConstructorBuilder.of(mapping.constructor());
        for (ParameterMetaData parameter : builder.getParameters()) {
            Optional<Document> document = documents.stream()
                    .filter(c -> c.name().equals(parameter.name()))
                    .findFirst();
            document.ifPresentOrElse(c -> {
                ParameterConverter converter = ParameterConverter.of(parameter);
                converter.convert(this, c, parameter, builder);
            }, builder::addEmptyParameter);
        }
        return builder.build();
    }

    private <T> T mapInheritanceEntity(DocumentEntity entity, Class<?> type) {
        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(type);

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the document collection "
                    + entity.name());
        }
        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::discriminatorColumn)
                .orElseThrow();

        String discriminator = entity.find(column, String.class)
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator column missing" +
                                " on the Document Collection, the document name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata mapping = getEntities().get(inheritance.entity());
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(entity.documents(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.documents(), mapping);
        }
    }

    private <T> T convertEntity(List<Document> documents, EntityMetadata mapping, T instance) {
        final Map<String, FieldMetadata> fieldsGroupByName = mapping.fieldsGroupByName();
        final List<String> names = documents.stream().map(Document::name).sorted().toList();
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            MappingType type = fieldsGroupByName.get(k).mappingType();
            return EMBEDDED.equals(type) || ENTITY.equals(type);
        };

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, documents, fieldsGroupByName));

        return instance;
    }

    private <T> T inheritanceToEntity(List<Document> documents, EntityMetadata mapping) {
        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(mapping.type());

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the document collection "
                    + mapping.name());
        }

        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::discriminatorColumn)
                .orElseThrow();

        String discriminator = documents.stream()
                .filter(d -> d.name().equals(column))
                .findFirst()
                .map(d -> d.get(String.class))
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator column missing" +
                                " on the Document Collection, the document name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata inheritanceMetadata = getEntities().get(inheritance.entity());
        T instance = inheritanceMetadata.newInstance();
        return convertEntity(documents, inheritanceMetadata, instance);
    }

    private DocumentFieldValue to(FieldMetadata field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return DefaultDocumentFieldValue.of(value, field);
    }

}
