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


import java.util.Objects;
import java.util.function.Function;

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
     * @return the instance inside {@link Value}
     */
    Object get();

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param type the class type
     * @param <T>  the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(Class<T> type);

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param supplier the type supplier
     * @param <T>      the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(TypeSupplier<T> supplier);

    /**
     * A wrapper of {@link Class#isInstance(Object)} to check the value instance within the {@link Value}
     *
     * @param type the type
     * @return {@link Class#isInstance(Object)}
     * @throws NullPointerException when type is null
     */
    boolean isInstanceOf(Class<?> type);


    /**
     * Creates a new {@link Value} instance
     *
     * @param value - the information to {@link Value}
     * @return a {@link Value} instance within a value informed
     * @throws NullPointerException when the parameter is null
     */
    static Value of(Object value) {
        Objects.requireNonNull(value, "value is required");
        return new DefaultValue(value);
    }
}
