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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultSettingsTest {


    @Test
    public void shouldReturnNPEWhenInstanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Settings.of((Map<String, Object>) null));

    }

    @Test
    public void shouldReturnNewInstance() {
        Settings settings = Settings.of();
        assertTrue(settings.isEmpty());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldCreatefromMap() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        assertEquals(1, settings.size());
        assertEquals("value", settings.get("key").get());
    }

    @Test
    public void shouldContainsKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertTrue(settings.containsKey("key"));
        assertFalse(settings.containsKey("key2"));
    }


    @Test
    public void shouldGetKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertThat(settings.keySet(), contains("key"));
    }

    @Test
    public void shouldGetEntrySet() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertEquals(settings.entrySet().stream().findFirst().get().getKey(), "key");
    }

    @Test
    public void shouldSize() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertEquals(1, settings.size());
        settings = Settings.of(Collections.emptyMap());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldIsEmpty() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        settings = Settings.of(Collections.emptyMap());
        assertTrue(settings.isEmpty());
    }

    @Test
    public void shouldGetValueClass() {
        Settings settings = Settings.of(singletonMap("key", "12"));

        Integer value = settings.get("key", Integer.class).get();
        assertEquals(12, value);
        assertFalse(settings.get("key2", Integer.class).isPresent());
    }

    @Test
    public void shouldInterateUsingForEach() {
        Settings settings = Settings.of(singletonMap("key", "12"));
        List<Map.Entry<String, Object>> references = new ArrayList<>();
        settings.forEach((k, v) -> references.add(new AbstractMap.SimpleEntry<>(k, v)));

        assertFalse(references.isEmpty());
        Map.Entry<String, Object> entry = references.get(0);
        Assertions.assertEquals("key", entry.getKey());
        Assertions.assertEquals("12", entry.getValue());
    }

    @Test
    public void shouldComputeIfPresent() {
        Settings settings = Settings.of(singletonMap("key", "12"));
        List<Map.Entry<String, Object>> references = new ArrayList<>();
        settings.computeIfPresent("key", (k, v) -> references.add(new AbstractMap.SimpleEntry<>(k, v)));
        assertFalse(references.isEmpty());
        Map.Entry<String, Object> entry = references.get(0);
        Assertions.assertEquals("key", entry.getKey());
        Assertions.assertEquals("12", entry.getValue());
    }

    @Test
    public void shouldComputeIAbsent() {
        Settings settings = Settings.of(singletonMap("key", "12"));
        settings.computeIfAbsent("non", (k) -> "no key");
        Assertions.assertEquals("no key", settings.get("non").get());
    }
}
