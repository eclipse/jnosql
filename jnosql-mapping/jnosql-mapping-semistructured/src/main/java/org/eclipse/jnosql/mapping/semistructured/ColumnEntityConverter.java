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

import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.core.Converters;
import jakarta.data.exceptions.MappingException;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.InheritanceMetadata;
import org.eclipse.jnosql.mapping.metadata.MappingType;
import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.eclipse.jnosql.mapping.metadata.MappingType.EMBEDDED;
import static org.eclipse.jnosql.mapping.metadata.MappingType.ENTITY;


/**
 * This interface represents the converter between an entity and the {@link CommunicationEntity}
 */
public abstract class ColumnEntityConverter {

    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    /**
     * Converts the instance entity to {@link CommunicationEntity}
     *
     * @param entity the instance
     * @return a {@link CommunicationEntity} instance
     * @throws NullPointerException when entity is null
     */
    public CommunicationEntity toColumn(Object entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().get(entity.getClass());
        CommunicationEntity communication = CommunicationEntity.of(mapping.name());
        mapping.fields().stream()
                .map(f -> to(f, entity))
                .map(f -> f.toElements(this, getConverters()))
                .flatMap(List::stream)
                .forEach(communication::add);

        mapping.inheritance().ifPresent(i -> communication.add(i.discriminatorColumn(),
                i.discriminatorValue()));
        return communication;
    }

    /**
     * Converts a {@link CommunicationEntity} to entity
     *
     * @param type   the entity class
     * @param entity the {@link CommunicationEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link CommunicationEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(Class<T> type, CommunicationEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");
        return toEntity(type, entity.elements());
    }

    /**
     * Converts a {@link CommunicationEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type   the instance
     * @param entity the {@link CommunicationEntity} to be converted
     * @param <T>    the entity type
     * @return the same instance with values set from {@link CommunicationEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(T type, CommunicationEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");

        if (type.getClass().isRecord()) {
            return (T) toEntity(type.getClass(), entity.elements());
        }
        EntityMetadata mapping = getEntities().get(type.getClass());
        return convertEntity(entity.elements(), mapping, type);
    }

    /**
     * Similar to {@link ColumnEntityConverter#toEntity(Class, CommunicationEntity)}, but
     * search the instance type from {@link CommunicationEntity#name()}
     *
     * @param entity the {@link CommunicationEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link CommunicationEntity}
     * @throws NullPointerException when entity is null
     */
    public <T> T toEntity(CommunicationEntity entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().findByName(entity.name());
        if (mapping.isInheritance()) {
            return mapInheritanceEntity(entity, mapping.type());
        }
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(entity.elements(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.elements(), mapping);
        }
    }

    protected ColumnFieldValue to(FieldMetadata field, Object entity) {
        Object value = field.read(entity);
        return DefaultColumnFieldValue.of(value, field);
    }

    protected <T> Consumer<String> feedObject(T entity, List<Element> elements, Map<String, FieldMetadata> fieldsGroupByName) {
        return (String k) -> {
            Optional<Element> element = elements.stream().filter(c -> c.name().equals(k)).findFirst();
            FieldMetadata field = fieldsGroupByName.get(k);
            FieldConverter fieldConverter = FieldConverter.get(field);
            if (ENTITY.equals(field.mappingType())) {
                element.ifPresent(c -> fieldConverter.convert(entity, c, field, this));
            } else {
                fieldConverter.convert(entity, elements, element.orElse(null), field, this);
            }
        };
    }


    protected <T> T toEntity(Class<T> type, List<Element> elements) {
        EntityMetadata mapping = getEntities().get(type);
        if (mapping.isInheritance()) {
            return inheritanceToEntity(elements, mapping);
        }
        ConstructorMetadata constructor = mapping.constructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(elements, mapping, instance);
        } else {
            return convertEntityByConstructor(elements, mapping);
        }
    }

    private <T> T convertEntityByConstructor(List<Element> elements, EntityMetadata mapping) {
        ConstructorBuilder builder = ConstructorBuilder.of(mapping.constructor());
        for (ParameterMetaData parameter : builder.parameters()) {
            Optional<Element> element = elements.stream()
                    .filter(c -> c.name().equals(parameter.name()))
                    .findFirst();
            element.ifPresentOrElse(c -> {
                ParameterConverter converter = ParameterConverter.of(parameter, getEntities());
                converter.convert(this, c, parameter, builder);
            }, builder::addEmptyParameter);
        }
        return builder.build();
    }

    private <T> T convertEntity(List<Element> elements, EntityMetadata mapping, T instance) {
        final Map<String, FieldMetadata> fieldsGroupByName = mapping.fieldsGroupByName();
        final List<String> names = elements.stream().map(Element::name).sorted().toList();
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            MappingType type = fieldsGroupByName.get(k).mappingType();
            return EMBEDDED.equals(type) || ENTITY.equals(type);
        };
        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, elements, fieldsGroupByName));

        return instance;
    }

    private <T> T mapInheritanceEntity(CommunicationEntity entity, Class<?> type) {
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
            return convertEntity(entity.elements(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.elements(), mapping);
        }
    }

    private <T> T inheritanceToEntity(List<Element> elements, EntityMetadata mapping) {
        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(mapping.type());

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the structure "
                    + mapping.name());
        }

        String column = group.values()
                .stream()
                .findFirst()
                .map(InheritanceMetadata::discriminatorColumn)
                .orElseThrow();

        String discriminator = elements.stream()
                .filter(d -> d.name().equals(column))
                .findFirst()
                .map(d -> d.get(String.class))
                .orElseThrow(
                        () -> new MappingException("To inheritance there is the discriminator element missing" +
                                " at the structure, the field's name: " + column));

        InheritanceMetadata inheritance = Optional.ofNullable(group.get(discriminator))
                .orElseThrow(() -> new MappingException("There is no inheritance map to the discriminator" +
                        " column value " + discriminator));

        EntityMetadata inheritanceMetadata = getEntities().get(inheritance.entity());
        T instance = inheritanceMetadata.newInstance();
        return convertEntity(elements, inheritanceMetadata, instance);
    }
}
