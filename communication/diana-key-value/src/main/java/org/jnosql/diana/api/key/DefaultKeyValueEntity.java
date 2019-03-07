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

import java.util.Objects;

/**
 * The default implementation of {@link KeyValueEntity}
 */
final class DefaultKeyValueEntity<T> implements KeyValueEntity {

    private final T key;

    private final Value value;

    DefaultKeyValueEntity(T key, Value value) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }


    public T getKey() {
        return key;
    }

    @Override
    public Object getKey(TypeSupplier typeSupplier) {
        return Value.of(key).get(typeSupplier);
    }

    @Override
    public Object getKey(Class clazz) {
        return Value.of(key).get(clazz);
    }

    public Value getValue() {
        return value;
    }

    @Override
    public Object get() {
        return value.get();
    }

    @Override
    public Object get(TypeSupplier typeSupplier) {
        return value.get(typeSupplier);
    }

    @Override
    public Object get(Class clazz) {
        return value.get(clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "DefaultKeyValueEntity{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
