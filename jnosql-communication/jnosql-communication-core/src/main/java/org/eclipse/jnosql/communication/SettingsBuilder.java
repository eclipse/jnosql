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
package org.eclipse.jnosql.communication;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * The default {@link Settings} builder
 */
public final class SettingsBuilder {

    private final Map<String, Object> settings = new HashMap<>();


    /**
     * Adds a new element in the builder
     *
     * @param key   the key to the settings
     * @param value the value from the respective settings
     * @return the settings builder with a new element
     * @throws NullPointerException when either key or value are null
     */
    public SettingsBuilder put(String key, Object value) {
        requireNonNull(key, "key is required");
        requireNonNull(value, "value is required");
        settings.put(key, value);
        return this;
    }

    /**
     * Adds a new element in the builder
     *
     * @param supplier the key to the settings
     * @param value    the value from the respective settings
     * @return the settings builder with a new element
     * @throws NullPointerException when either key or value are null
     */
    public SettingsBuilder put(Supplier<String> supplier, Object value) {
        requireNonNull(supplier, "supplier is required");
        requireNonNull(value, "value is required");
        return put(supplier.get(), value);
    }

    /**
     * Adds all elements in the builder
     *
     * @param settings the map with all elements
     * @return the settings builder with a new element
     * @throws NullPointerException when either the settings or the key or the value are null
     */
    public SettingsBuilder putAll(Map<String, Object> settings) {
        requireNonNull(settings, "settings is required");
        settings.entrySet().forEach(this::put);
        return this;
    }

    /**
     * Creates a {@link Settings} from the builder
     *
     * @return a {@link Settings} instance
     */
    public Settings build() {
        return DefaultSettings.of(settings);
    }


    private void put(Map.Entry<String, Object> entry) {
        put(entry.getKey(), entry.getValue());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SettingsBuilder that = (SettingsBuilder) o;
        return Objects.equals(settings, that.settings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(settings);
    }

    @Override
    public String toString() {
        return "SettingsBuilder{" + "settings=" + settings +
                '}';
    }

}
