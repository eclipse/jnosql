/*
 *
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
 *
 */

package org.jnosql.diana;


/**
 * This interface represents the value that will be storage in the database.
 */
public interface Value {

    /**
     * Returns the real value without conversion.
     *
     * @return the instance inside {@link Value}
     */
    Object get();

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param clazz the new class
     * @param <T>   the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(Class<T> clazz);

    /**
     * Converts {@link Value#get()} to specified class
     *
     * @param typeSupplier the type supplier
     * @param <T>          the new instance type
     * @return a new instance converted to informed class
     * @throws NullPointerException          when the class is null
     * @throws UnsupportedOperationException when the type is unsupported
     * @see ValueReader
     */
    <T> T get(TypeSupplier<T> typeSupplier);

    /**
     * A wrapper of {@link Class#isInstance(Object)} to check the value instance within the {@link Value}
     *
     * @param typeClass the type
     * @return {@link Class#isInstance(Object)}
     * @throws NullPointerException when typeClass is null
     */
    boolean isInstanceOf(Class<?> typeClass);


    /**
     * Creates a new {@link Value} instance
     *
     * @param value - the information to {@link Value}
     * @return a {@link Value} instance within a value informed
     * @throws NullPointerException when the parameter is null
     */
    static Value of(Object value) {
        return DefaultValue.of(value);
    }

}
