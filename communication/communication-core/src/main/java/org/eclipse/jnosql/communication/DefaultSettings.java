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
package org.eclipse.jnosql.communication;

import jakarta.nosql.Settings;
import jakarta.nosql.Value;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;

final class DefaultSettings  implements Settings {

    private final Map<String, Object> configurations;

    DefaultSettings(Map<String, Object> configurations) {
        this.configurations = configurations.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> e.getValue()));
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
        return Optional.ofNullable(configurations.get(key));
    }

    @Override
    public Optional<Object> get(Collection<String> keys) {
        Objects.requireNonNull(keys, "keys is required");

        Predicate<Map.Entry<String, Object>> equals = keys.stream()
                .map(prefix -> (Predicate<Map.Entry<String, Object>>) e -> e.getKey().equals(prefix))
                .reduce((a, b) -> a.or(b)).orElse(e -> false);

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
    public List<Object> prefix(Collection<String> prefixes) {
        Objects.requireNonNull(prefixes, "prefixes is required");
        if (prefixes.isEmpty()) {
            return Collections.emptyList();
        }
        Predicate<Map.Entry<String, Object>> prefixCondition = prefixes.stream()
                .map(prefix -> (Predicate<Map.Entry<String, Object>>) e -> e.getKey().startsWith(prefix))
                .reduce((a, b) -> a.or(b)).orElse(e -> false);

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
    public Object getOrDefault(String key, Object defaultValue) {
        Objects.requireNonNull(key, "key is required");
        return get(key).orElse(defaultValue);
    }


    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(configurations.keySet());
    }

    @Override
    public Map<String, Object> toMap() {
        return unmodifiableMap(configurations);
    }


    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return Collections.unmodifiableSet(toMap().entrySet());
    }

    @Override
    public void forEach(BiConsumer<String, Object> action) {
        Objects.requireNonNull(action, "action is null");
        configurations.forEach(action);
    }

    @Override
    public void computeIfPresent(String key, BiConsumer<String, Object> action) {
        Objects.requireNonNull(action, "action is required");
        configurations.computeIfPresent(key, (k, v) -> {
            action.accept(k, v);
            return v;
        });
    }

    @Override
    public void computeIfAbsent(String key, Function<String, Object> action) {
        Objects.requireNonNull(action, "action is required");
        configurations.computeIfAbsent(key, action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSettings)) {
            return false;
        }
        DefaultSettings that = (DefaultSettings) o;
        return Objects.equals(configurations, that.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(configurations);
    }

    @Override
    public String toString() {
        return "DefaultSettings{" + "configurations=" + configurations +
                '}';
    }

}