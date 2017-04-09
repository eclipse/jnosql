/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
     * @throws NullPointerException {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws UnsupportedOperationException {@link org.jnosql.diana.api.Value#get(Class)}
     */
    <T> T get(Class<T> clazz) throws NullPointerException, UnsupportedOperationException;

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @param typeSupplier {@link org.jnosql.diana.api.Value#get(Class)}
     * @param <T> {@link org.jnosql.diana.api.Value#get(Class)}
     * @return {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @throws NullPointerException {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws UnsupportedOperationException {@link org.jnosql.diana.api.Value#get(Class)}
     */
    <T> T get(TypeSupplier<T> typeSupplier) throws NullPointerException, UnsupportedOperationException;

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get()}
     * @return {@link org.jnosql.diana.api.Value#get()}
     */
    Object get();
}
