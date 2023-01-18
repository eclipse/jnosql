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
package org.eclipse.jnosql.communication.keyvalue;

import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.Objects;

/**
 * A bucket unit, it's a tuple that contains key its respective value.
 */
public class KeyValueEntity {

    private final Object key;

    private final Value value;

    KeyValueEntity(Object key, Value value) {
        this.key = key;
        this.value = value;
    }

    /**
     * the key
     *
     * @return the value
     */
    public Object key() {
        return key;
    }

    /**
     * The value
     *
     * @return the value
     * @see Value
     */
    public Object value() {
        return value.get();
    }

    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param supplier {@link Value#get(TypeSupplier)}
     * @param <K>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    public <K> K key(TypeSupplier<K> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return Value.of(key).get(supplier);
    }

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param type {@link Value#get(Class)}
     * @param <K>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    public <K> K key(Class<K> type) {
        Objects.requireNonNull(type, "type is required");
        return Value.of(key).get(type);
    }


    /**
     * Alias to {@link Value#get(TypeSupplier)}
     *
     * @param supplier {@link Value#get(TypeSupplier)}
     * @param <V>          {@link Value#get(TypeSupplier)}
     * @return {@link Value#get(TypeSupplier)}
     * @throws NullPointerException          see {@link Value#get(TypeSupplier)}
     * @throws UnsupportedOperationException see {@link Value#get(TypeSupplier)}
     */
    public <V> V value(TypeSupplier<V> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return value.get(supplier);
    }

    /**
     * Alias to {@link Value#get(Class)}
     *
     * @param type {@link Value#get(Class)}
     * @param <V>   {@link Value#get(Class)}
     * @return {@link Value#get(Class)}
     * @throws NullPointerException          see {@link Value#get(Class)}
     * @throws UnsupportedOperationException see {@link Value#get(Class)}
     */
    public <V> V value(Class<V> type) {
        Objects.requireNonNull(type, "type is required");
        return value.get(type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyValueEntity that = (KeyValueEntity) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "DefaultKeyValueEntity{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    /**
     * Creates a Key value instance
     *
     * @param key   the key
     * @param value the value
     * @param <K>   the key type
     * @param <V>   the value type
     * @return a {@link KeyValueEntity} instance
     * @throws NullPointerException when either key or value are null
     */
    public static <K, V> KeyValueEntity of(K key, V value) {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(value, "value is required");
        return new KeyValueEntity(getKey(key), getValue(value));
    }

    private static Object getKey(Object key) {
        if (key instanceof Value) {
            return Value.class.cast(key).get();
        }
        return key;
    }

    private static Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        }
        return Value.of(value);
    }

}
