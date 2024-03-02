/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import org.eclipse.jnosql.communication.Entry;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;


/**
 * Represents a single column in a {@link CommunicationEntity}, consisting of a name and its corresponding value.
 */
public interface Element extends Entry {

    /**
     * Retrieves the value of this element as the specified type.
     *
     * @param type the class object representing the type to which the value should be converted
     * @param <T>  the type of the returned value
     * @return the value of this element as the specified type
     * @throws NullPointerException          if the specified type is {@code null}
     * @throws UnsupportedOperationException if the value cannot be converted to the specified type
     */
    <T> T get(Class<T> type) ;

    /**
     * Retrieves the value of this element as the specified type using a {@link TypeSupplier}.
     *
     * @param supplier the type supplier providing the target type to which the value should be converted
     * @param <T>      the type of the returned value
     * @return the value of this element as the specified type
     * @throws NullPointerException          if the specified type supplier is {@code null}
     * @throws UnsupportedOperationException if the value cannot be converted to the specified type
     */
    <T> T get(TypeSupplier<T> supplier);

    /**
     * Retrieves the value of this element as an {@code Object}.
     *
     * @return the value of this element
     */
    Object get();


    /**
     * Creates a new element with the specified name and value.
     *
     * @param name  the name of the element
     * @param value the value of the element
     * @param <V>   the type of the value
     * @return a new element instance
     * @throws NullPointerException if the specified name is {@code null}
     * @see Elements
     */
    static <V> Element of(String name, V value) {
        Objects.requireNonNull(name, "name is required");
        return new DefaultElement(name, getValue(value));
    }

    private static Value getValue(Object value) {
        if (value instanceof Value) {
            return (Value) value;
        } else {
            return Value.of(value);
        }
    }
}
