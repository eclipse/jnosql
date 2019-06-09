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

package org.jnosql.diana.document;


import jakarta.nosql.TypeSupplier;
import org.jnosql.diana.Value;

/**
 * A Document Collection Entity unit, it is a tuple (pair) that consists of a key-value pair,
 * where the key is mapped to a value.
 */
public interface Document  {

    /**
     * Creates a Document instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a Document instance
     * @see Documents
     */
    static Document of(String name, Value value) {
        return new DefaultDocument(name, value);
    }

    /**
     * Creates a Document instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a Document instance
     * @throws NullPointerException when either name or value is null
     */
    static Document of(String name, Object value) {
        if (value instanceof Value) {
            return new DefaultDocument(name, Value.class.cast(value));
        }
        return new DefaultDocument(name, Value.of(value));
    }

    /**
     * The column's name
     *
     * @return name
     */
    String getName();

    /**
     * the column's value
     *
     * @return {@link Value}
     */
    Value getValue();

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param clazz {@link Value#get(Class)}
     * @param <T>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <T> T get(Class<T> clazz);

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param typeSupplier {@link Value#get(TypeSupplier)}
     * @param <T>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    <T> T get(TypeSupplier<T> typeSupplier);

    /**
     * Alias to {@link Value#get()}
     *
     * @return {@link Value#get()}
     */
    Object get();
}
