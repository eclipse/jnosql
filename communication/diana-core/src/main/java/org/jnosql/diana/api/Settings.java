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

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The interface represents the settings used in a configuration.
 *
 * @see Settings#of(Map[])
 */
public interface Settings {

    Set<String> keySet();

    Object get(String key);

    <T> T get(String key, Class<T> type);

    <T> T getOrDefault(Object key, T defaultValue);

    boolean isEmpty();

    int size();

    boolean containsKey(String key);

    Set<Map.Entry<String, Object>> entrySet();

    /**
     * Creates a {@link SettingsBuilder}
     *
     * @return a {@link SettingsBuilder} instance
     */
    static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    /**
     * Creates a settings from maps
     *
     * @param settings the setting
     * @return the new {@link Settings} instance
     * @throws NullPointerException when either the parameter is null or there key or value null
     */
    static Settings of(Map<String, Object> settings) {

        requireNonNull(settings, "settings is required");
        SettingsBuilder builder = new SettingsBuilder();
        builder.putAll(settings);
        return builder.build();
    }

    /**
     * Creates a settings from maps
     *
     * @param settings the setting
     * @return the new {@link Settings} instance
     * @throws NullPointerException when either the parameter is null or there key or value null
     */
    @SafeVarargs
    static Settings of(Map<String, Object>... settings) {
        Stream.of(settings).forEach(s -> requireNonNull(s, "Settings is required"));

        SettingsBuilder builder = new SettingsBuilder();
        Stream.of(settings).forEach(builder::putAll);
        return builder.build();
    }

}
