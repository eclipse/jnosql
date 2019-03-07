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

package org.jnosql.diana.api.key;


import org.jnosql.diana.api.TypeSupplier;
import org.jnosql.diana.api.Value;

/**
 * A bucket unit, it's a tuple that contains key its respective value.
 *
 * @param <T> the key type
 */
@SuppressWarnings("unchecked")
public interface KeyValueEntity<T>  {


    /**
     * Creates a Key value instance
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the key type
     * @return a {@link KeyValueEntity} instance
     * @throws NullPointerException when either key or value are null
     */

    static <T> KeyValueEntity<T> of(T key, Value value) {
        return new DefaultKeyValueEntity(key, value);
    }

    /**
     * Creates a Key value instance
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the key type
     * @return a {@link KeyValueEntity} instance
     * @throws NullPointerException when either key or value are null
     */
    static <T> KeyValueEntity<T> of(T key, Object value) {
        if (value instanceof Value) {
            return new DefaultKeyValueEntity(key, Value.class.cast(value));
        }
        return of(key, Value.of(value));
    }

    /**
     * the key
     *
     * @return the value
     */
    T getKey();


    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param clazz {@link Value#get(Class)}
     * @param <K>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    <K> K getKey(Class<K> clazz);

    /**
     * The value
     *
     * @return the value
     * @see Value
     */
    Value getValue();

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get(Class)}
     *
     * @param clazz {@link org.jnosql.diana.api.Value#get(Class)}
     * @param <T>   {@link org.jnosql.diana.api.Value#get(Class)}
     * @return {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws NullPointerException          see {@link org.jnosql.diana.api.Value#get(Class)}
     * @throws UnsupportedOperationException see {@link org.jnosql.diana.api.Value#get(Class)}
     */
    <T> T get(Class<T> clazz);

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     *
     * @param typeSupplier {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @param <T>          {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @return {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link org.jnosql.diana.api.Value#get(TypeSupplier)}
     */
    <T> T get(TypeSupplier<T> typeSupplier);

    /**
     * Alias to {@link org.jnosql.diana.api.Value#get()}
     *
     * @return {@link org.jnosql.diana.api.Value#get()}
     */
    Object get();

}
