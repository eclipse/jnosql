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
import jakarta.nosql.Value;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.unmodifiableMap;

public final class MemorySettings  implements Settings {

    private final Map<String, Object> configurations;

    MemorySettings(Map<String, Object> configurations) {
        this.configurations = configurations.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
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
    public boolean containsKey(String key) {
        return configurations.containsKey(key);
    }

    @Override
    public Optional<Object> get(String key) {
        Objects.requireNonNull(key, "key is required");
        return Optional.ofNullable(configurations.get(key));
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

        Predicate<Map.Entry<String, Object>> equals =
                StreamSupport.stream(keys.spliterator(), false)
                        .map(prefix -> (Predicate<Map.Entry<String, Object>>) e -> e.getKey().equals(prefix))
                        .reduce(Predicate::or).orElse(e -> false);

        return configurations.entrySet().stream()
                .filter(equals)
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public List<Object> prefix(String prefix) {
        Objects.requireNonNull(prefix, "prefix is required");
        return configurations.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
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
        Predicate<Map.Entry<String, Object>> prefixCondition = values.stream()
                .map(prefix -> (Predicate<Map.Entry<String, Object>>) e -> e.getKey().startsWith(prefix))
                .reduce(Predicate::or).orElse(e -> false);

        return configurations.entrySet().stream()
                .filter(prefixCondition)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        Objects.requireNonNull(key, "key is required");
        Objects.requireNonNull(type, "type is required");
        return get(key).map(Value::of).map(v -> v.get(type));
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
        return Collections.unmodifiableSet(configurations.keySet());
    }

    @Override
    public Map<String, Object> toMap() {
        return unmodifiableMap(configurations);
    }



}
