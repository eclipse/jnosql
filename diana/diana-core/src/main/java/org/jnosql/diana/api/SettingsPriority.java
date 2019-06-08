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
package org.jnosql.diana.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A utilitarian class that defines the priority to settings. It follows using Eclipse MicroProfile Configuration and JSR 382.
 * {@link System#getProperties()}, {@link System#getenv()} and {@link Settings}
 */
public final class SettingsPriority {
    private SettingsPriority() {
    }


    /**
     * Creates a {@link Settings} appending {@link System#getProperties()}, {@link System#getenv()}
     * with priority
     *
     * @param settings the settings
     * @return a new {@link Settings} instance
     * @throws NullPointerException when settins is null
     */
    public static Settings get(Map<String, Object> settings) {
        requireNonNull(settings, "settings is required");
        return getSettings(settings);
    }

    /**
     * Creates a {@link Settings} appending {@link System#getProperties()}, {@link System#getenv()}
     * with priority
     *
     * @param settings the settings
     * @return a new {@link Settings} instance
     * @throws NullPointerException when settins is null
     */
    public static Settings get(Settings settings) {
        requireNonNull(settings, "settings is required");
        return getSettings(settings.toMap());
    }


    /**
     * Finds a property from key using the priority
     *
     * @param key      the key
     * @param settings the settings
     * @return a property or {@link Optional#empty()}
     * @throws NullPointerException when there is null parameter
     */
    public static Optional<Object> get(String key, Settings settings) {
        requireNonNull(key, "key is required");
        requireNonNull(settings, "settings is required");
        String value = System.getProperty(key);
        if (value != null) {
            return Optional.of(value);
        }
        value = System.getenv().get(key);
        if (value != null) {
            return Optional.of(value);
        }
        return settings.get(key);
    }

    private static Settings getSettings(Map<String, Object> settings) {
        Map<String, Object> configurations = new HashMap<>(settings);
        configurations.putAll(System.getenv());
        System.getProperties().forEach((k, v) -> configurations.put(k.toString(), v));
        return Settings.of(configurations);
    }
}
