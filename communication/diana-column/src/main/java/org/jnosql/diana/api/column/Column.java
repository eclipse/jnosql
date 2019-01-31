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

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.TypeSupplier;
import org.jnosql.diana.api.Value;

import java.io.Serializable;

/**
 * A Column Family entity unit, it is a tuple (pair) that consists of a key-value pair,
 * where the key is mapped to a value.
 */
public interface Column extends Serializable {


    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    static Column of(String name, Value value) {
        return new DefaultColumn(name, value);
    }

    /**
     * Creates a column instance
     *
     * @param name  - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    static Column of(String name, Object value) {
        if (value instanceof Value) {
            return new DefaultColumn(name, Value.class.cast(value));
        }
        return new DefaultColumn(name, Value.of(value));
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
     * Alias to {@link org.jnosql.diana.api.Value#get(Class)}
     * @param clazz the clazz
     * @param <T> the type
     * @return {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws NullPointerException see {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws UnsupportedOperationException see {@link org.jnosql.diana.api.Value#get(Class)}
     */
    <T> T get(Class<T> clazz);

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @param typeSupplier {@link org.jnosql.diana.api.Value#get(Class)}
     * @param <T> {@link org.jnosql.diana.api.Value#get(Class)}
     * @return {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @throws NullPointerException  see {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws UnsupportedOperationException see {@link org.jnosql.diana.api.Value#get(Class)}
     */
    <T> T get(TypeSupplier<T> typeSupplier);

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get()}
     * @return {@link org.jnosql.diana.api.Value#get()}
     */
    Object get();
}
