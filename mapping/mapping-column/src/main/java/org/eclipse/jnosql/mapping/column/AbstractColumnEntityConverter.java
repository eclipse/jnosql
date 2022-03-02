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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.Columns;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
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
import org.eclipse.jnosql.mapping.column.ColumnFieldConverters.ColumnFieldConverterFactory;
import static org.eclipse.jnosql.mapping.reflection.FieldType.EMBEDDED;
import static org.eclipse.jnosql.mapping.reflection.FieldType.SUB_ENTITY;
import static java.util.Objects.requireNonNull;



/**
 * Template method to {@link ColumnEntityConverter}
 */
public abstract class AbstractColumnEntityConverter implements ColumnEntityConverter {

    private final ColumnFieldConverterFactory converterFactory = new ColumnFieldConverterFactory();

    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    @Override
    public ColumnEntity toColumn(Object entityInstance) {
        requireNonNull(entityInstance, "Object is required");
        ClassMapping mapping = getClassMappings().get(entityInstance.getClass());
        ColumnEntity entity = ColumnEntity.of(mapping.getName());
        mapping.getFields().stream()
                .map(f -> to(f, entityInstance))
                .filter(FieldValue::isNotEmpty)
                .map(f -> f.toColumn(this, getConverters()))
                .flatMap(List::stream)
                .forEach(entity::add);
        return entity;
    }

    @Override
    public <T> T toEntity(Class<T> entityClass, ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(entityClass, "entityClass is required");
        return toEntity(entityClass, entity.getColumns());
    }

    @Override
    public <T> T toEntity(T entityInstance, ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        requireNonNull(entityInstance, "entityInstance is required");
        ClassMapping mapping = getClassMappings().get(entityInstance.getClass());
        return convertEntity(entity.getColumns(), mapping, entityInstance);
    }

    @Override
    public <T> T toEntity(ColumnEntity entity) {
        requireNonNull(entity, "entity is required");
        ClassMapping mapping = getClassMappings().findByName(entity.getName());
        T instance = mapping.newInstance();
        return convertEntity(entity.getColumns(), mapping, instance);
    }

    protected ColumnFieldValue to(FieldMapping field, Object entityInstance) {
        Object value = field.read(entityInstance);
        return DefaultColumnFieldValue.of(value, field);
    }

    protected <T> Consumer<String> feedObject(T instance, List<Column> columns, Map<String, FieldMapping> fieldsGroupByName) {
        return (String k) -> {
            Optional<Column> column = columns.stream().filter(c -> c.getName().equals(k)).findFirst();
            FieldMapping field = fieldsGroupByName.get(k);
            ColumnFieldConverter fieldConverter = converterFactory.get(field);
            if (SUB_ENTITY.equals(field.getType())) {
                fieldConverter.convert(instance, null, column.orElse(null), field, this);
            } else {
                fieldConverter.convert(instance, columns, column.orElse(null), field, this);
            }
        };
    }


    protected <T> T toEntity(Class<T> entityClass, List<Column> columns) {
        ClassMapping mapping = getClassMappings().get(entityClass);
        T instance = mapping.newInstance();
        return convertEntity(columns, mapping, instance);
    }

    private <T> T convertEntity(List<Column> columns, ClassMapping mapping, T instance) {
        final Map<String, FieldMapping> fieldsGroupByName = mapping.getFieldsGroupByName();
        final List<String> names = columns.stream().map(Column::getName).sorted().collect(Collectors.toList());
        final Predicate<String> existField = k -> Collections.binarySearch(names, k) >= 0;
        final Predicate<String> isElementType = k -> {
            FieldType type = fieldsGroupByName.get(k).getType();
            return EMBEDDED.equals(type) || SUB_ENTITY.equals(type);
        };
        fieldsGroupByName.keySet().stream()
                .filter(existField.or(isElementType))
                .forEach(feedObject(instance, columns, fieldsGroupByName));

        return instance;
    }

}
