/*
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
 */
package org.eclipse.jnosql.mapping.graph;


import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Property;

import java.util.NoSuchElementException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Mapping API
 * @param <V>
 */
final class DefaultProperty<V> implements Property<V> {

    private final String key;

    private final V value;

    private DefaultProperty(String key, V value) {
        this.key = requireNonNull(key, "key is required");
        this.value = requireNonNull(value, "value is required");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultProperty)) {
            return false;
        }
        DefaultProperty<?> that = (DefaultProperty<?>) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return  "DefaultProperty{" + "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public V value() throws NoSuchElementException {
        return value;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public Element element() {
        throw new UnsupportedOperationException("The method does not support element");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("The method does not support remove");
    }

    /**
     * Creates an instance of {@link Property}
     *
     * @param key   the key
     * @param value the value
     * @param <T>   the type of value
     * @return a {@link Property} instance
     * @throws NullPointerException when either key and value are null
     */
    public static <T> Property<T> of(String key, T value) {
        return new DefaultProperty<>(key, value);
    }
}
