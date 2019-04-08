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
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The interface represents the settings used in a configuration.
 *
 * @see Settings#of(Map[])
 */
public interface Settings {

    /**
     * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa. If the map is modified while an iteration over
     * the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined.
     * The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove,
     * Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
     *
     * @return a set view of the keys contained in this map
     */
    Set<String> keySet();

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     * @throws NullPointerException when key is null
     */
    Object get(String key);

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key  the key whose associated value is to be returned
     * @param type the type be used as {@link Value#get(Class)}
     * @param <T>  the type value
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     * @throws NullPointerException when there are null parameters
     */
    <T> T get(String key, Class<T> type);

    /**
     * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @param <T>          the type value
     * @return the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key
     */
    <T> T getOrDefault(Object key, T defaultValue);

    /**
     * @return Returns true if this map contains no key-value mappings.
     */
    boolean isEmpty();

    /**
     * @return Returns the number of key-value mappings in this map.
     */
    int size();

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     * @throws NullPointerException when key is null
     */
    boolean containsKey(String key);

    /**
     * Returns a Set view of the mappings contained in this map.
     *
     * @return a set view of the mappings contained in this map
     */
    Set<Map.Entry<String, Object>> entrySet();

    /**
     * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception.
     *
     * @param action the action
     */
    void forEach(BiConsumer<String, Object> action);

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
