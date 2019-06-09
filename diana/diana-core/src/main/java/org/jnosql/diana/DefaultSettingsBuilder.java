/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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

import jakarta.nosql.Settings;
import jakarta.nosql.Settings.SettingsBuilder;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link SettingsBuilder}
 */
public class DefaultSettingsBuilder implements SettingsBuilder {

    private final Map<String, Object> settings = new HashMap<>();

    @Override
    public SettingsBuilder put(String key, Object value) {
        requireNonNull(key, "key is required");
        requireNonNull(value, "value is required");
        settings.put(key, value);
        return this;
    }

    @Override
    public SettingsBuilder putAll(Map<String, Object> settings) {
        requireNonNull(settings, "settings is required");
        settings.entrySet().forEach(this::put);
        return this;
    }

    @Override
    public Settings build() {
        return new DefaultSettings(settings);
    }


    private void put(Map.Entry<String, Object> entry) {
        put(entry.getKey(), entry.getValue());
    }


    @Override
    public String toString() {
        return "SettingsBuilder{" + "settings=" + settings +
                '}';
    }

}
