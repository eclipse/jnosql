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
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.MappingException;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
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
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.eclipse.jnosql.mapping.reflection.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.MappingType.ENTITY;

/**
 * Template method to {@link DocumentEntityConverter}
 */
public abstract class AbstractDocumentEntityConverter implements DocumentEntityConverter {

    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    @Override
    public DocumentEntity toDocument(Object entityInstance) {
        requireNonNull(entityInstance, "Object is required");
        EntityMetadata mapping = getEntities().get(entityInstance.getClass());
        DocumentEntity entity = DocumentEntity.of(mapping.getName());
        mapping.getFields().stream()
                .map(f -> to(f, entityInstance))
                .filter(FieldValue::isNotEmpty)
                .map(f -> f.toDocument(this, getConverters()))
                .flatMap(List::stream)
                .forEach(entity::add);

        mapping.getInheritance().ifPresent(i -> entity.add(i.getDiscriminatorColumn(), i.getDiscriminatorValue()));
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
        EntityMetadata mapping = getEntities().get(entityInstance.getClass());
        return convertEntity(entity.getDocuments(), mapping, entityInstance);
    }

    protected <T> T toEntity(Class<T> entityClass, List<Document> documents) {
        EntityMetadata mapping = getEntities().get(entityClass);
        if (mapping.isInheritance()) {
            return inheritanceToEntity(documents, mapping);

        }
        ConstructorMetadata constructor = mapping.getConstructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(documents, mapping, instance);
        } else {
            return convertEntityByConstructor(documents, mapping);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T toEntity(DocumentEntity entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().findByName(entity.getName());
        if (mapping.isInheritance()) {
            return mapInheritanceEntity(entity, mapping.getType());
        }
        T instance = mapping.newInstance();
        return convertEntity(entity.getDocuments(), mapping, instance);
    }

    protected <T> Consumer<String> feedObject(T instance, List<Document> documents, Map<String, FieldMapping> fieldsGroupByName) {
        return k -> {
            Optional<Document> document = documents.stream().filter(c -> c.getName().equals(k)).findFirst();
            FieldMapping field = fieldsGroupByName.get(k);
            FieldConverter fieldConverter = FieldConverter.get(field);
            if (ENTITY.equals(field.getType())) {
                document.ifPresent(d -> fieldConverter.convert(instance,
                        null, d, field, this));
            } else {
                fieldConverter.convert(instance, documents, document.orElse(null), field, this);
            }
        };
    }

    private <T> T convertEntityByConstructor(List<Document> documents, EntityMetadata mapping) {
        ConstructorBuilder builder = new ConstructorBuilder(mapping.getConstructor());
        for (ParameterMetaData parameter : builder.getParameters()) {
            Optional<Document> document = documents.stream()
                    .filter(c -> c.getName().equals(parameter.getName()))
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
                    + entity.getName());
        }
        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::getDiscriminatorColumn)
                .orElseThrow();

        String discriminator = entity.find(column, String.class)
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator column missing" +
                                " on the Document Collection, the document name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata mapping = getEntities().get(inheritance.getEntity());
        T instance = mapping.newInstance();
        return convertEntity(entity.getDocuments(), mapping, instance);
    }

    private <T> T convertEntity(List<Document> documents, EntityMetadata mapping, T instance) {
        final Map<String, FieldMapping> fieldsGroupByName = mapping.getFieldsGroupByName();
        final List<String> names = documents.stream().map(Document::getName).sorted().collect(Collectors.toList());
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            MappingType type = fieldsGroupByName.get(k).getType();
            return EMBEDDED.equals(type) || ENTITY.equals(type);
        };

        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, documents, fieldsGroupByName));

        return instance;
    }

    private <T> T inheritanceToEntity(List<Document> documents, EntityMetadata mapping) {
        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(mapping.getType());

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the document collection "
                    + mapping.getName());
        }

        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::getDiscriminatorColumn)
                .orElseThrow();

        String discriminator = documents.stream()
                .filter(d -> d.getName().equals(column))
                .findFirst()
                .map(d -> d.get(String.class))
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator column missing" +
                                " on the Document Collection, the document name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata inheritanceMetadata = getEntities().get(inheritance.getEntity());
        T instance = inheritanceMetadata.newInstance();
        return convertEntity(documents, inheritanceMetadata, instance);
    }

    private DocumentFieldValue to(FieldMapping field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return DefaultDocumentFieldValue.of(value, field);
    }

}
