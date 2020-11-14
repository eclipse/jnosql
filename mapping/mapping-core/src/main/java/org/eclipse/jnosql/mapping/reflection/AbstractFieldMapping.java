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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.Value;
import jakarta.nosql.mapping.AttributeConverter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class to all {@link FieldMapping}
 *
 * @see FieldMapping
 */
abstract class AbstractFieldMapping implements FieldMapping {

    protected final FieldType type;

    protected final Field field;

    protected final String name;

    protected final String fieldName;

    protected final Class<? extends AttributeConverter<?, ?>> converter;

    protected final FieldReader reader;

    protected final FieldWriter writer;

    AbstractFieldMapping(FieldType type, Field field, String name,
                         Class<? extends AttributeConverter<?, ?>> converter, FieldReader reader, FieldWriter writer) {
        this.type = type;
        this.field = field;
        this.name = name;
        this.fieldName = field.getName();
        this.converter = converter;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public FieldType getType() {
        return type;
    }

    @Override
    public Field getNativeField() {
        return field;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object read(Object bean) {
        Objects.requireNonNull(bean, "bean is required");
        return this.reader.read(bean);
    }

    @Override
    public void write(Object bean, Object value) {
        Objects.requireNonNull(bean, "bean is required");
        this.writer.write(bean, value);
    }

    @Override
    public <X, Y, T extends AttributeConverter<X, Y>> Optional<Class<? extends AttributeConverter<X, Y>>> getConverter() {
        return Optional.ofNullable((Class<? extends AttributeConverter<X, Y>>) converter);
    }

    @Override
    public String toString() {
        return "AbstractFieldMapping{" +
                "type=" + type +
                ", field=" + field +
                ", name='" + name + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", converter=" + converter +
                ", reader=" + reader +
                ", writer=" + writer +
                '}';
    }

    public Object getValue(Value value) {
        return value.get(field.getType());
    }
}
