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
    public <K> K getKey(TypeSupplier<K> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return Value.of(key).get(supplier);
    }

    @Override
    public <K> K getKey(Class<K> type) {
        Objects.requireNonNull(type, "type is required");
        return Value.of(key).get(type);
    }

    @Override
    public <V> V getValue(TypeSupplier<V> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return value.get(supplier);
    }

    @Override
    public <V> V getValue(Class<V> type) {
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
