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
package org.eclipse.jnosql.mapping.column;

import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnEntity;
import org.eclipse.jnosql.mapping.Converters;
import jakarta.data.exceptions.MappingException;
import org.eclipse.jnosql.mapping.reflection.ConstructorBuilder;
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
 * This interface represents the converter between an entity and the {@link ColumnEntity}
 */
public abstract class ColumnEntityConverter {


    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    /**
     * Converts the instance entity to {@link ColumnEntity}
     *
     * @param entity the instance
     * @return a {@link ColumnEntity} instance
     * @throws NullPointerException when entity is null
     */
    public ColumnEntity toColumn(Object entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().get(entity.getClass());
        ColumnEntity communication = ColumnEntity.of(mapping.getName());
        mapping.getFields().stream()
                .map(f -> to(f, entity))
                .filter(FieldValue::isNotEmpty)
                .map(f -> f.toColumn(this, getConverters()))
                .flatMap(List::stream)
                .forEach(communication::add);

        mapping.getInheritance().ifPresent(i -> communication.add(i.getDiscriminatorColumn(),
                i.getDiscriminatorValue()));
        return communication;
    }

    /**
     * Converts a {@link ColumnEntity} to entity
     *
     * @param type the entity class
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(Class<T> type, ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");
        return toEntity(type, entity.columns());
    }

    /**
     * Converts a {@link ColumnEntity} to entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type the instance
     * @param entity      the {@link ColumnEntity} to be converted
     * @param <T>         the entity type
     * @return the same instance with values set from {@link ColumnEntity}
     * @throws NullPointerException when either type or entity are null
     */
    public <T> T toEntity(T type, ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(type, "type is required");
        EntityMetadata mapping = getEntities().get(type.getClass());
        return convertEntity(entity.columns(), mapping, type);
    }

    /**
     * Similar to {@link ColumnEntityConverter#toEntity(Class, ColumnEntity)}, but
     * search the instance type from {@link ColumnEntity#name()} }
     *
     * @param entity the {@link ColumnEntity} to be converted
     * @param <T>    the entity type
     * @return the instance from {@link ColumnEntity}
     * @throws NullPointerException when entity is null
     */
    public <T> T toEntity(ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        EntityMetadata mapping = getEntities().findByName(entity.name());
        if (mapping.isInheritance()) {
            return mapInheritanceEntity(entity, mapping.getType());
        }
        ConstructorMetadata constructor = mapping.getConstructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(entity.columns(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.columns(), mapping);
        }
    }

    protected ColumnFieldValue to(FieldMapping field, Object entity) {
        Object value = field.read(entity);
        return DefaultColumnFieldValue.of(value, field);
    }

    protected <T> Consumer<String> feedObject(T entity, List<Column> columns, Map<String, FieldMapping> fieldsGroupByName) {
        return (String k) -> {
            Optional<Column> column = columns.stream().filter(c -> c.name().equals(k)).findFirst();
            FieldMapping field = fieldsGroupByName.get(k);
            FieldConverter fieldConverter = FieldConverter.get(field);
            if (ENTITY.equals(field.getType())) {
                column.ifPresent(c -> fieldConverter.convert(entity, c, field, this));
            } else {
                fieldConverter.convert(entity, columns, column.orElse(null), field, this);
            }
        };
    }


    protected <T> T toEntity(Class<T> type, List<Column> columns) {
        EntityMetadata mapping = getEntities().get(type);
        if (mapping.isInheritance()) {
            return inheritanceToEntity(columns, mapping);
        }
        ConstructorMetadata constructor = mapping.getConstructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(columns, mapping, instance);
        } else {
            return convertEntityByConstructor(columns, mapping);
        }
    }

    private <T> T convertEntityByConstructor(List<Column> columns, EntityMetadata mapping) {
        ConstructorBuilder builder = ConstructorBuilder.of(mapping.getConstructor());
        for (ParameterMetaData parameter : builder.getParameters()) {
            Optional<Column> column = columns.stream()
                    .filter(c -> c.name().equals(parameter.getName()))
                    .findFirst();
            column.ifPresentOrElse(c -> {
                ParameterConverter converter = ParameterConverter.of(parameter);
                converter.convert(this, c, parameter, builder);
            }, builder::addEmptyParameter);
        }
        return builder.build();
    }

    private <T> T convertEntity(List<Column> columns, EntityMetadata mapping, T instance) {
        final Map<String, FieldMapping> fieldsGroupByName = mapping.getFieldsGroupByName();
        final List<String> names = columns.stream().map(Column::name).sorted().collect(Collectors.toList());
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            MappingType type = fieldsGroupByName.get(k).getType();
            return EMBEDDED.equals(type) || ENTITY.equals(type);
        };
        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, columns, fieldsGroupByName));

        return instance;
    }

    private <T> T mapInheritanceEntity(ColumnEntity entity, Class<?> type) {
        Map<String, InheritanceMetadata> group = getEntities()
                .findByParentGroupByDiscriminatorValue(type);

        if (group.isEmpty()) {
            throw new MappingException("There is no discriminator inheritance to the document collection "
                    + entity.name());
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
        ConstructorMetadata constructor = mapping.getConstructor();
        if (constructor.isDefault()) {
            T instance = mapping.newInstance();
            return convertEntity(entity.columns(), mapping, instance);
        } else {
            return convertEntityByConstructor(entity.columns(), mapping);
        }
    }

    private <T> T inheritanceToEntity(List<Column> columns, EntityMetadata mapping) {
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

        String discriminator = columns.stream()
                .filter(d -> d.name().equals(column))
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
        return convertEntity(columns, inheritanceMetadata, instance);
    }
}
