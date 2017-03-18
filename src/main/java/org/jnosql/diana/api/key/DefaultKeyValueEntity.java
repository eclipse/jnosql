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

    public Value getValue() {
        return value;
    }

    @Override
    public Object get() {
        return value.get();
    }

    @Override
    public Object get(TypeSupplier typeSupplier) throws NullPointerException, UnsupportedOperationException {
        return value.get(typeSupplier);
    }

    @Override
    public Object get(Class clazz) throws NullPointerException, UnsupportedOperationException {
        return value.get(clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KeyValueEntity{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
