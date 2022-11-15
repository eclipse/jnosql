/*
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
 */
package org.eclipse.jnosql.mapping.config;

import jakarta.nosql.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MicroProfileSettingsTest {
    @BeforeEach
    public void beforeEach() {
        System.clearProperty("key");
        System.clearProperty("host");
        System.clearProperty("host.1");
        System.clearProperty("host.2");
        System.clearProperty("host.3");
        System.clearProperty("server");
        System.clearProperty("server.1");
        System.clearProperty("server.2");
    }

    @Test
    public void shouldReturnNPEWhenInstanceIsNull() {
        assertThrows(NullPointerException.class, () -> Settings.of((Map<String, Object>) null));

    }

    @Test
    public void shouldReturnNewInstance() {
        Settings settings = MicroProfileSettings.INSTANCE;
        Assertions.assertNotNull(settings);
    }

    @Test
    public void shouldCreateFromMap() {
        Settings settings = MicroProfileSettings.INSTANCE;
        assertFalse(settings.isEmpty());
    }

    @Test
    public void shouldContainsKeys() {
        setProperty(singletonMap("key", "value"));
        Settings settings = MicroProfileSettings.INSTANCE;
        assertTrue(settings.containsKey("key"));
        assertFalse(settings.containsKey("key2"));
    }


    @Test
    public void shouldGetKeys() {
        setProperty(singletonMap("key", "value"));
        Settings settings = MicroProfileSettings.INSTANCE;
        assertThat(settings.keySet()).contains("key");
    }


    @Test
    public void shouldSize() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertTrue(settings.size() >= 1);

    }

    @Test
    public void shouldIsEmpty() {
        Settings settings = MicroProfileSettings.INSTANCE;
        assertFalse(settings.isEmpty());
    }

    @Test
    public void shouldGet() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Optional<Object> value = settings.get("key");
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldGetSupplier() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Optional<Object> value = settings.get(() -> "key");
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldNPEGet() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((Supplier<String>) null));
    }

    @Test
    public void shouldGetIterable() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Optional<Object> value = settings.get(Collections.singleton("key"));
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldGetIterableSupplier() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Optional<Object> value = settings.getSupplier(Collections.singleton(() -> "key"));
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldNPEGetIterable() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((Iterable<String>) null));
        Assertions.assertThrows(NullPointerException.class, () -> settings.getSupplier(null));
    }

    @Test
    public void shouldGetValueClass() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;

        Integer value = settings.get("key", Integer.class).get();
        assertEquals(Integer.valueOf(12), value);
        assertFalse(settings.get("key2", Integer.class).isPresent());
    }

    @Test
    public void shouldGetValueClassSupplier() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;

        Integer value = settings.get(() -> "key", Integer.class).get();
        assertEquals(Integer.valueOf(12), value);
        assertFalse(settings.get(() -> "key2", Integer.class).isPresent());
    }

    @Test
    public void shouldGetOrDefault() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        assertEquals("12", settings.getOrDefault("key", "13"));
        assertEquals("13", settings.getOrDefault("key-1", "13"));
    }

    @Test
    public void shouldGetOrDefaultSupplier() {
        setProperty(singletonMap("key", "12"));
        Settings settings = MicroProfileSettings.INSTANCE;
        assertEquals("12", settings.getOrDefault(() -> "key", "13"));
        assertEquals("13", settings.getOrDefault(() -> "key-1", "13"));
    }

    @Test
    public void shouldReturnErrorWhenPrefixIsNull() {

        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "host.2", "host-2",
                "host.3", "host-3");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;

        assertThrows(NullPointerException.class, () -> settings.prefix((String) null));
    }

    @Test
    public void shouldFindPrefix() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "host.2", "host-2",
                "host.3", "host-3");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;

        List<Object> hosts = settings.prefix("host");
        assertThat(hosts)
                .hasSize(4)
                .contains("host", "host-1", "host-2", "host-3");
    }

    @Test
    public void shouldFindPrefixSupplier() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "host.2", "host-2",
                "host.3", "host-3");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;

        List<Object> hosts = settings.prefix(() -> "host");
        assertThat(hosts)
                .hasSize(4)
                .contains("host", "host-1", "host-2", "host-3");
    }

    @Test
    public void shouldFindPrefixWithOrder() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "host.2", "host-2",
                "host.3", "host-3");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;
        List<Object> hosts = settings.prefix("host");
        assertThat(hosts).hasSize(4).contains("host", "host-1", "host-2", "host-3");
    }


    @Test
    public void shouldReturnErrorWhenPrefixesIsNull() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "host.2", "host-2",
                "host.3", "host-3");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;
        assertThrows(NullPointerException.class, () -> settings.prefix((Collection<String>) null));

    }

    @Test
    public void shouldFindPrefixes() {

        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "server", "server",
                "server.1", "server-1");
        setProperty(params);
        Settings settings = MicroProfileSettings.INSTANCE;

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }

    @Test
    public void shouldFindPrefixesSupplier() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "server", "server",
                "server.1", "server-1");
        setProperty(params);

        Settings settings = MicroProfileSettings.INSTANCE;
        List<Object> hosts = settings.prefixSupplier(Arrays.asList(() -> "host", () -> "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }

    @Test
    public void shouldFindPrefixesSort() {
        Map<String, Object> params = Map.of(
                "host", "host",
                "host.1", "host-1",
                "server", "server",
                "server.1", "server-1");
        setProperty(params);

        Settings settings = MicroProfileSettings.INSTANCE;

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }

    private void setProperty(Map<String, Object> params) {
        params.forEach((k, v) -> System.setProperty(k, v.toString()));
    }
}