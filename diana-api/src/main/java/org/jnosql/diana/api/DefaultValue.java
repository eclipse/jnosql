/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

final class DefaultValue implements Value {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    private final Object value;

    private DefaultValue(Object value) {
        this.value = value;
    }

    public static Value of(Object value) {
        Objects.requireNonNull(value);
        return new DefaultValue(value);
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return SERVICE_PROVIDER.read(clazz, value);
    }

    @Override
    public <T> T get(TypeReference<T> typeReference) throws NullPointerException, UnsupportedOperationException {
        return null;
    }

    public <T> List<T> getList(Class<T> clazz) {
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (List<T>) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(clazz, o))
                    .collect(collectingAndThen(toList(), Collections::unmodifiableList));
        }
        return singletonList(get(clazz));
    }

    public <T> Set<T> getSet(Class<T> clazz) {
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (Set<T>) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(clazz, o))
                    .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        }
        return Collections.singleton(get(clazz));
    }

    public <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) {

        if (Map.class.isInstance(value)) {
            Map mapValue = Map.class.cast(value);
            return (Map<K, V>) mapValue.keySet().stream()
                    .collect(Collectors.toMap(mapKeyElement(keyClass), mapValueElement(valueClass, mapValue)));
        }
        throw new UnsupportedOperationException("There is not supported convert" + value + " a not Map type.");
    }

    private <K> Function mapKeyElement(Class<K> keyClass) {
        return (keyElement) -> {
            if (SERVICE_PROVIDER.isCompatible(keyClass)) {
                return SERVICE_PROVIDER.read(keyClass, keyElement);
            }
            return keyElement;
        };
    }

    private <V> Function mapValueElement(Class<V> valueClass, Map mapValue) {
        return (keyElement) -> {
            Object valueElement = mapValue.get(keyElement);
            if (SERVICE_PROVIDER.isCompatible(valueClass)) {
                return SERVICE_PROVIDER.read(valueClass, valueElement);
            }
            return valueElement;
        };
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }
        Value that = (Value) o;
        return Objects.equals(value, that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultValue{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
