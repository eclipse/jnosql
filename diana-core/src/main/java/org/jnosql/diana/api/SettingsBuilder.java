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

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * The Settings builder {@link Settings}
 */
public class SettingsBuilder {

    private final Map<String, Object> settings = new HashMap<>();

    /**
     * Adds a new element in the builder
     *
     * @param key   the key to the settings
     * @param value the value from the respective settings
     * @return the settings builder with a new element
     * @throws NullPointerException when either key or value are null
     */
    public SettingsBuilder add(String key, Object value) throws NullPointerException {
        requireNonNull(key, "key is required");
        requireNonNull(value, "value is required");
        settings.put(key, value);
        return this;
    }

    public Settings build() {
        return new DefaultSettings(settings);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SettingsBuilder{");
        sb.append("settings=").append(settings);
        sb.append('}');
        return sb.toString();
    }
}

