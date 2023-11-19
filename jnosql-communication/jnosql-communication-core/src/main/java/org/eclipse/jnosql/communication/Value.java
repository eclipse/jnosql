/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 *
 */
package org.eclipse.jnosql.communication;



/**
 * It represents an information unit that is to/from a database.
 * Where to read it operates the {@link ValueReader} and writes it using {@link ValueWriter}.
 * For both reading and writing, it will load those implementations from SPI.
 *
 * @see ValueReader
 * @see ValueWriter
 */
public interface Value {

    /**
     * Returns the value without conversion.
     *
     * @return the instance inside {@link Value}, or {@code null} if the value is null
     */
    Object get();

    /**
     * Converts {@link Value#get()} to the specified class.
     * When the value is {@code null}, it will return null.
     *
     * @param type the class type
     * @param <T>  the new instance type
     * @return a new instance converted to the informed class, or {@code null} if the value is null
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(Class<T> type);


    /**
     * Converts {@link Value#get()} to the specified class.
     *
     * @param supplier the type supplier
     * @param <T>      the new instance type
     * @return a new instance converted to the informed class, or {@code null} if the value is null
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(TypeSupplier<T> supplier);

    /**
     * A wrapper of {@link Class#isInstance(Object)} to check the value instance within the {@link Value}.
     *
     * @param type the type
     * @return {@link Class#isInstance(Object)}
     * @throws NullPointerException when the type is null
     */
    boolean isInstanceOf(Class<?> type);

    /**
     * Checks whether the current instance represents a null value.
     *
     * @return {@code true} if the value encapsulated by this instance is null,
     *         {@code false} otherwise.
     */
    boolean isNull();


    /**
     * Creates a new {@link Value} instance.
     *
     * @param value - the information to {@link Value}
     * @return a {@link Value} instance within a value informed, or {@link DefaultValue#NULL} if the value is null
     */
    static Value of(Object value) {
        if(value == null) {
            return DefaultValue.NULL;
        }
        return new DefaultValue(value);
    }

    /**
     * Creates and returns a {@link Value} instance representing a null value.
     * This method provides a convenient way to obtain a {@link Value} instance that encapsulates a null value.
     * The returned instance is often used to signify the absence of a meaningful value in scenarios
     * where a valid value is expected but none is available.
     *
     * @return a {@link Value} instance representing a null value, typically {@link DefaultValue#NULL}.
     */
    static Value ofNull() {
        return DefaultValue.NULL;
    }
}
