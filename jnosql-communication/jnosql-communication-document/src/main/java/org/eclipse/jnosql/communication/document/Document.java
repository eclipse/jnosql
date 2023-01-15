/*
 *
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
 *
 */

package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Entry;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;

/**
 * A Document is a tuple (pair) that consists of the name and its respective value.
 * A {@link DocumentEntity} has one or more Documents.
 */
public interface Document extends Entry {


    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param type {@link Value#get(Class)}
     * @param <T>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <T> T get(Class<T> type) ;

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param supplier {@link Value#get(TypeSupplier)}
     * @param <T>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    <T> T get(TypeSupplier<T> supplier);


    /**
     * Alias to {@link Value#get()}
     *
     * @return {@link Value#get()}
     */
    Object get();

    /**
     * Creates a document instance
     *
     * @param name  - document's name
     * @param value - document's value
     * @param <V>   the value type
     * @return a document instance
     * @throws NullPointerException when there is any null parameter
     * @see Documents
     */
    static <V> Document of(String name, V value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new DefaultDocument(name, getValue(value));
    }

    private static Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        } else {
            return Value.of(value);
        }
    }
}
