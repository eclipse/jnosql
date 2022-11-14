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

import jakarta.nosql.Settings;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class DefaultSettings implements Settings {

    private final Config config;

    private DefaultSettings(Config config) {
        this.config = config;
    }


    @Override
    public int size() {
        return (int) StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                .count();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(String key) {
        return get(key).isEmpty();
    }

    @Override
    public Optional<Object> get(String key) {
        Objects.requireNonNull(key, "key is required");
        return config.getOptionalValue(key, Object.class);
    }

    @Override
    public Optional<Object> get(Supplier<String> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return get(supplier.get());
    }

    @Override
    public Optional<Object> getSupplier(Iterable<Supplier<String>> suppliers) {
        Objects.requireNonNull(suppliers, "supplier is required");
        List<String> keys = StreamSupport.stream(suppliers.spliterator(), false)
                .map(Supplier::get).collect(Collectors.toUnmodifiableList());
        return get(keys);
    }

    @Override
    public Optional<Object> get(Iterable<String> keys) {
        Objects.requireNonNull(keys, "keys is required");

        return StreamSupport.stream(keys.spliterator(), false)
                .flatMap(k -> config.getOptionalValue(k, Object.class).stream())
                .findFirst();
    }

    @Override
    public List<Object> prefix(String prefix) {
        Objects.requireNonNull(prefix, "prefix is required");
        return StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                .filter(p -> p.startsWith(prefix))
                .map(p -> config.getValue(p, Object.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Object> prefix(Supplier<String> supplier) {
        Objects.requireNonNull(supplier, "supplier is required");
        return prefix(supplier.get());
    }

    @Override
    public List<Object> prefixSupplier(Iterable<Supplier<String>> suppliers) {
        Objects.requireNonNull(suppliers, "suppliers is required");
        Iterable<String> prefixes = StreamSupport.stream(suppliers.spliterator(), false)
                .map(Supplier::get)
                .collect(Collectors.toUnmodifiableList());
        return prefix(prefixes);
    }

    @Override
    public List<Object> prefix(Iterable<String> prefixes) {
        Objects.requireNonNull(prefixes, "prefixes is required");

        List<String> values = StreamSupport.stream(prefixes.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        Predicate<String> prefixCondition = values.stream()
                .map(prefix -> (Predicate<String>) property -> property.startsWith(prefix))
                .reduce(Predicate::or).orElse(e -> false);

        return StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                .filter(prefixCondition)
                .sorted()
                .map(p -> config.getValue(p, Object.class))
                .collect(Collectors.toList());
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(type, "type is required");
        return config.getOptionalValue(key, type);
    }

    @Override
    public <T> Optional<T> get(Supplier<String> supplier, Class<T> type) {
        Objects.requireNonNull(supplier, "supplier is required");
        Objects.requireNonNull(type, "type is required");
        return get(supplier.get(), type);
    }

    @Override
    public <T> T getOrDefault(String key, T defaultValue) {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(defaultValue, "defaultValue is required");
        Class<T> type = (Class<T>) defaultValue.getClass();
        return (T) get(key, type).orElse(defaultValue);
    }

    @Override
    public <T> T getOrDefault(Supplier<String> supplier, T defaultValue) {
        Objects.requireNonNull(supplier, "supplier is required");
        Objects.requireNonNull(defaultValue, "defaultValue is required");
        return getOrDefault(supplier.get(), defaultValue);
    }


    @Override
    public Set<String> keySet() {
        return StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Map<String, Object> toMap() {
        return keySet().stream().collect(Collectors.toMap(Function.identity(), k ->
                config.getValue(k, Object.class)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultSettings that = (DefaultSettings) o;
        return Objects.equals(config, that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(config);
    }

    @Override
    public String toString() {
        return "DefaultSettings{" +
                "config=" + config +
                '}';
    }

    static DefaultSettings of(Map<String, Object> params) {
        Objects.requireNonNull(params, "params is required");
        Config config = ConfigProvider.getConfig();
        params.entrySet().forEach(e -> System.setProperty(e.getKey(), e.getValue().toString()));
        return new DefaultSettings(config);
    }
}