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
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.keyvalue.KeyValueEntityConverter;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Template method to {@link KeyValueEntityConverter}
 */
public abstract class AbstractKeyValueEntityConverter implements KeyValueEntityConverter {

    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    @Override
    public KeyValueEntity toKeyValue(Object entity) {
        requireNonNull(entity, "Object is required");
        Class<?> clazz = entity.getClass();

        FieldMapping key = getId(clazz);
        Object value = key.read(entity);

        requireNonNull(value, String.format("The key field %s is required", key.getName()));
        return KeyValueEntity.of(getKey(value, clazz, false), entity);
    }

    @Override
    public <T> T toEntity(Class<T> entityClass, KeyValueEntity entity) {

        T bean = entity.getValue(entityClass);
        if (Objects.isNull(bean)) {
            return null;
        }

        Object key = getKey(entity.getKey(), entityClass, true);
        FieldMapping id = getId(entityClass);
        id.write(bean, key);
        return bean;
    }

    private <T> Object getKey(Object key, Class<T> entityClass, boolean toEntity) {
        FieldMapping id = getId(entityClass);
        if (id.getConverter().isPresent()) {
            AttributeConverter attributeConverter = getConverters().get(id.getConverter().get());
            if(toEntity) {
                return attributeConverter.convertToEntityAttribute(key);
            } else {
                return attributeConverter.convertToDatabaseColumn(key);
            }
        } else {
            return Value.of(key).get(id.getNativeField().getType());
        }
    }

    private FieldMapping getId(Class<?> clazz) {
        ClassMapping mapping = getClassMappings().get(clazz);
        return mapping.getId().orElseThrow(() -> IdNotFoundException.newInstance(clazz));
    }
}
