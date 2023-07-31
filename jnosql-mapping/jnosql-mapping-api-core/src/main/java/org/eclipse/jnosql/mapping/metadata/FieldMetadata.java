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
package org.eclipse.jnosql.mapping.metadata;


import jakarta.nosql.Column;
import jakarta.nosql.Id;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.AttributeConverter;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * This class represents the information from {@link java.lang.reflect.Field}.
 * The strategy is to cache in all fields in a class to either read and write faster from Field
 */
public interface FieldMetadata {

    /**
     * Return the type of the field
     *
     * @return the {@link MappingType}
     */
    MappingType mappingType();

    /**
     * Returns a {@code Class} object that identifies the
     * declared type for the field represented by this
     * {@code  java.lang.reflect.Field} object.
     *
     * @return a {@code Class} object identifying the declared
     * type of the field represented by this object
     */
    Class<?> type();

    /**
     * Reads and returns the field information through the bean.
     *
     * @param bean the bean
     * @return the property value
     * @throws NullPointerException when bean is null
     */
    Object read(Object bean);

    /**
     * Writes the field information through the bean.
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
    String name();

    /**
     * Returns the Java Fields name.
     * {@link java.lang.reflect.Field#getName()}
     *
     * @return The Java Field name {@link java.lang.reflect.Field#getName()}
     */
    String fieldName();


    /**
     * Returns the object from the field type
     *
     * @param value the value {@link Value}
     * @return the instance from the field type
     */
    Object value(Value value);

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
    <X, Y, T extends AttributeConverter<X, Y>> Optional<Class<? extends AttributeConverter<X, Y>>> converter();


    /**
     * Retrieves the value from the default method (usually named "value") of the provided annotation type.
     *
     * <p>This method allows you to obtain the value from the default method, typically named "value," in the
     * specified annotation type. If the field in question has this annotation or if the method with the name
     * "value" exists in the annotation, this method will return an {@link java.util.Optional} containing the
     * value. Otherwise, it will return an empty {@link java.util.Optional}.</p>
     *
     * @param type the annotation
     * @return an {@link java.util.Optional} containing the value from the default method if present,
     *         otherwise an empty {@link Optional#empty()}
     * @param <T> the annotation type
     * @see java.util.Optional
     */
    <T extends Annotation> Optional<String> value(Class<T> type);
}
