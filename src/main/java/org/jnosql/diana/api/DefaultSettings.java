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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class DefaultSettings implements Settings {

    private final Map<String, Object> configurations;

    DefaultSettings(Map<String, Object> configurations) {
        this.configurations = new HashMap<>(configurations);
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
    public boolean containsKey(Object key) {
        return configurations.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return configurations.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return configurations.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return configurations.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return configurations.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {
        this.configurations.putAll(map);
    }

    @Override
    public void clear() {
        configurations.clear();
    }

    @Override
    public Set<String> keySet() {
        return configurations.keySet();
    }

    @Override
    public Collection<Object> values() {
        return configurations.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return configurations.entrySet();
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
        final StringBuilder sb = new StringBuilder("DefaultSettings{");
        sb.append("configurations=").append(configurations);
        sb.append('}');
        return sb.toString();
    }
}
