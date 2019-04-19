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
import java.util.Objects;

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
        Objects.requireNonNull(settings, "settings is required");
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
        Objects.requireNonNull(settings, "settings is required");
        return getSettings(settings.toMap());
    }

    private static Settings getSettings(Map<String, Object> settings) {
        Map<String, Object> configurations = new HashMap<>(settings);
        settings.putAll(System.getenv());
        System.getProperties().forEach((k, v) -> configurations.put(k.toString(), v));
        return Settings.of(configurations);
    }
}
