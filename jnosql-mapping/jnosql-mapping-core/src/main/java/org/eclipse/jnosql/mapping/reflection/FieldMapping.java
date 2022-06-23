/*
 *  Copyright (c) 2020 Otávio Santana and others
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
import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.mapping.AttributeConverter;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * This class represents the information from {@link Field}.
 * The strategy is do cache in all fields in a class to either read and writer faster from Field
 */
public interface FieldMapping {

    /**
     * Return the type of the field
     *
     * @return the {@link FieldType}
     */
    FieldType getType();

    /**
     * The {@link Field}
     *
     * @return the field
     */
    Field getNativeField();

    /**
     * Reads the field using {@link FieldReader}
     *
     * @param bean the bean
     * @return the property value
     * @throws NullPointerException when bean is null
     */
    Object read(Object bean);

    /**
     * Writes the field using {@link java.io.FileWriter}
     *
     * @param bean  the bean
     * @param value the value to write
     * @throws NullPointerException when there is null parameter
     */
    void write(Object bean, Object value);

    /**
     * Returns the name of the field that can be either the field name
     * or {@link Column#value()}
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the Java Fields name.
     * {@link Field#getName()}
     *
     * @return The Java Field name {@link Field#getName()}
     */
    String getFieldName();


    /**
     * Returns the object from the field type
     *
     * @param value the value {@link Value}
     * @return the instance from the field type
     */
    Object getValue(Value value);

    /**
     * Returns true is the field is annotated with {@link Id}
     *
     * @return true is annotated with {@link Id}
     */
    boolean isId();

    /**
     * Returns the converter class
     * @param <X> the type of the entity attribute
     * @param <Y> the type of the database column
     * @param <T> the Converter
     * @return the converter if present
     */
    <X, Y, T extends AttributeConverter<X, Y>> Optional<Class<? extends AttributeConverter<X, Y>>> getConverter();


}
