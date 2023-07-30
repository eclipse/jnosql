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
package org.eclipse.jnosql.mapping.reflection;

import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.MappingType;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class to all {@link FieldMetadata}
 *
 * @see FieldMetadata
 */
abstract class AbstractFieldMetadata implements FieldMetadata {

    protected final MappingType mappingType;

    protected final Field field;

    protected final String name;

    protected final String fieldName;

    protected final Class<? extends AttributeConverter<?, ?>> converter;

    protected final FieldReader reader;

    protected final FieldWriter writer;

    protected final Class<?> type;

    AbstractFieldMetadata(MappingType mappingType, Field field, String name,
                          Class<? extends AttributeConverter<?, ?>> converter, FieldReader reader, FieldWriter writer) {
        this.mappingType = mappingType;
        this.field = field;
        this.name = name;
        this.fieldName = field.getName();
        this.converter = converter;
        this.reader = reader;
        this.writer = writer;
        this.type = field.getType();
    }

    @Override
    public MappingType mappingType() {
        return mappingType;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String fieldName() {
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
    public Class<?> type() {
        return this.type;
    }

    @Override
    public <X, Y, T extends AttributeConverter<X, Y>> Optional<Class<? extends AttributeConverter<X, Y>>> converter() {
        return Optional.ofNullable((Class<? extends AttributeConverter<X, Y>>) converter);
    }

    @Override
    public String toString() {
        return "AbstractFieldMetadata{" +
                "mappingType=" + mappingType +
                ", field=" + field +
                ", name='" + name + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", converter=" + converter +
                ", reader=" + reader +
                ", writer=" + writer +
                ", type=" + type +
                '}';
    }

    public Object value(Value value) {
        return value.get(field.getType());
    }
}
