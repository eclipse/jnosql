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
package org.jnosql.artemis.key;

import org.jnosql.artemis.IdNotFoundException;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.artemis.reflection.FieldMapping;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.KeyValueEntity;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Template method to {@link KeyValueEntityConverter}
 */
public abstract class AbstractKeyValueEntityConverter implements KeyValueEntityConverter {

    protected abstract ClassMappings getClassMappings();


    @Override
    public KeyValueEntity<?> toKeyValue(Object entityInstance) {
        requireNonNull(entityInstance, "Object is required");
        Class<?> clazz = entityInstance.getClass();
        ClassMapping mapping = getClassMappings().get(clazz);

        FieldMapping key = getId(clazz, mapping);

        Object value = key.read(entityInstance);
        requireNonNull(value, String.format("The key field %s is required", key.getName()));

        return KeyValueEntity.of(value, entityInstance);
    }

    @Override
    public <T> T toEntity(Class<T> entityClass, KeyValueEntity<?> entity) {

        Value value = entity.getValue();
        T t = value.get(entityClass);
        if (Objects.isNull(t)) {
            return null;
        }
        FieldMapping key = getId(entityClass, getClassMappings().get(entityClass));

        Object keyValue = key.read(t);
        if (Objects.isNull(keyValue) || !keyValue.equals(entity.getKey())) {
            key.write(t, entity.getKey());
        }
        return t;
    }

    @Override
    public <T> T toEntity(Class<T> entityClass, Value value) {
        T t = value.get(entityClass);
        if (Objects.isNull(t)) {
            return null;
        }
        return t;
    }

    private FieldMapping getId(Class<?> clazz, ClassMapping mapping) {
        List<FieldMapping> fields = mapping.getFields();
        return fields.stream().filter(FieldMapping::isId)
                .findFirst().orElseThrow(() -> IdNotFoundException.newInstance(clazz));
    }
}
