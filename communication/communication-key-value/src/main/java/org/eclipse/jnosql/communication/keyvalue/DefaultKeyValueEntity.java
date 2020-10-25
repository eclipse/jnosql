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
package org.eclipse.jnosql.communication.keyvalue;

import jakarta.nosql.TypeSupplier;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.KeyValueEntity;

import java.util.Objects;

/**
 * The default implementation of {@link KeyValueEntity}
 */
final class DefaultKeyValueEntity implements KeyValueEntity {

    private final Object key;

    private final Value value;

    DefaultKeyValueEntity(Object key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value.get();
    }

    @Override
    public <K> K getKey(TypeSupplier<K> typeSupplier) {
        Objects.requireNonNull(typeSupplier, "typeSupplier is required");
        return Value.of(key).get(typeSupplier);
    }

    @Override
    public <K> K getKey(Class<K> clazz) {
        Objects.requireNonNull(clazz, "clazz is required");
        return Value.of(key).get(clazz);
    }

    @Override
    public <V> V getValue(TypeSupplier<V> typeSupplier) {
        Objects.requireNonNull(typeSupplier, "typeSupplier is required");
        return value.get(typeSupplier);
    }

    @Override
    public <V> V getValue(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz is required");
        return value.get(clazz);
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
        DefaultKeyValueEntity that = (DefaultKeyValueEntity) o;
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
}
