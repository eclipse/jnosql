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
package org.jnosql.diana.api;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import static java.util.Optional.ofNullable;

class DefaultSettings implements Settings {

    private final Map<String, Object> configurations;

    DefaultSettings(Map<String, Object> configurations) {
        this.configurations = configurations;
    }


    @Override
    public int size() {
        return configurations.size();
    }

    @Override
    public boolean isEmpty() {
        return configurations.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return configurations.containsKey(key);
    }

    @Override
    public Object get(String key) {
        return configurations.get(key);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(type, "type is required");
        Object value = configurations.get(key);
        return ofNullable(value).map(Value::of).map(v -> v.get(type)).orElse(null);
    }

    @Override
    public <T> T getOrDefault(Object key, T defaultValue) {
        Objects.requireNonNull(key, "key is required");
        Object value = configurations.getOrDefault(key, defaultValue);
        return (T) value;
    }


    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(configurations.keySet());
    }


    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(configurations.entrySet());
    }

    @Override
    public void forEach(BiConsumer<String, Object> action) {
        Objects.requireNonNull(action, "action is null");
        configurations.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSettings)) {
            return false;
        }
        DefaultSettings that = (DefaultSettings) o;
        return Objects.equals(configurations, that.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(configurations);
    }

    @Override
    public String toString() {
        return "DefaultSettings{" + "configurations=" + configurations +
                '}';
    }
}
