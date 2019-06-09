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
package org.jnosql.diana;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * The Settings builder {@link Settings}
 */
public class SettingsBuilder {

    private final Map<String, Object> settings = new HashMap<>();

    SettingsBuilder() {
    }

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

    private void put(Map.Entry<String, Object> entry) {
        put(entry.getKey(), entry.getValue());
    }

    public Settings build() {
        return new DefaultSettings(settings);
    }

    @Override
    public String toString() {
        return "SettingsBuilder{" + "settings=" + settings +
                '}';
    }

}

