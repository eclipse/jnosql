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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemorySettingsTest {


    @Test
    public void shouldReturnNPEWhenInstanceIsNull() {
        assertThrows(NullPointerException.class, () -> MemorySettings.of(null));

    }

    @Test
    public void shouldReturnNewInstance() {
        Settings settings = MemorySettings.of(emptyMap());
        assertTrue(settings.isEmpty());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldCreateFromMap() {
        Settings settings = MemorySettings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        assertEquals(1, settings.size());
        assertEquals("value", settings.get("key").get());
    }

    @Test
    public void shouldContainsKeys() {
        Settings settings = MemorySettings.of(singletonMap("key", "value"));
        assertTrue(settings.containsKey("key"));
        assertFalse(settings.containsKey("key2"));
    }


    @Test
    public void shouldGetKeys() {
        Settings settings = MemorySettings.of(singletonMap("key", "value"));
        assertThat(settings.keySet()).contains("key");
    }


    @Test
    public void shouldSize() {
        Settings settings = MemorySettings.of(singletonMap("key", "value"));
        assertEquals(1, settings.size());
        settings = MemorySettings.of(emptyMap());
        assertEquals(0, settings.size());
    }

    @Test
    public void shouldIsEmpty() {
        Settings settings = MemorySettings.of(singletonMap("key", "value"));
        assertFalse(settings.isEmpty());
        settings = MemorySettings.of(emptyMap());
        assertTrue(settings.isEmpty());
    }

    @Test
    public void shouldGet() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Optional<Object> value = settings.get("key");
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldGetSupplier() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Optional<Object> value = settings.get(() -> "key");
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldNPEGet() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((Supplier<String>) null));
    }

    @Test
    public void shouldGetIterable() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Optional<Object> value = settings.get(Collections.singleton("key"));
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldGetIterableSupplier() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Optional<Object> value = settings.getSupplier(Collections.singleton(() -> "key"));
        Assertions.assertNotNull(value);
        Assertions.assertEquals("12", value.get());
    }

    @Test
    public void shouldNPEGetIterable() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        Assertions.assertThrows(NullPointerException.class, () -> settings.get((Iterable<String>) null));
        Assertions.assertThrows(NullPointerException.class, () -> settings.getSupplier(null));
    }

    @Test
    public void shouldGetValueClass() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));

        Integer value = settings.get("key", Integer.class).get();
        assertEquals(Integer.valueOf(12), value);
        assertFalse(settings.get("key2", Integer.class).isPresent());
    }

    @Test
    public void shouldGetValueClassSupplier() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));

        Integer value = settings.get(() -> "key", Integer.class).get();
        assertEquals(Integer.valueOf(12), value);
        assertFalse(settings.get(() -> "key2", Integer.class).isPresent());
    }


    @Test
    public void shouldGetOrDefault() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        assertEquals("12", settings.getOrDefault("key", "13"));
        assertEquals("13", settings.getOrDefault("key-1", "13"));
    }

    @Test
    public void shouldGetOrDefaultSupplier() {
        Settings settings = MemorySettings.of(singletonMap("key", "12"));
        assertEquals("12", settings.getOrDefault(() -> "key", "13"));
        assertEquals("13", settings.getOrDefault(() -> "key-1", "13"));
    }

    @Test
    public void shouldReturnErrorWhenPrefixIsNull() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "host.2",
                "host-2", "host.3", "host-3");
        Settings settings = MemorySettings.of(params);
        assertThrows(NullPointerException.class, () -> settings.prefix((String) null));
    }

    @Test
    public void shouldFindPrefix() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "host.2",
                "host-2", "host.3", "host-3");
        Settings settings = MemorySettings.of(params);

        List<Object> hosts = settings.prefix("host");
        assertThat(hosts)
                .hasSize(4)
                .contains("host", "host-1", "host-2", "host-3");
    }

    @Test
    public void shouldFindPrefixSupplier() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "host.2",
                "host-2", "host.3", "host-3");

        Settings settings = MemorySettings.of(params);

        List<Object> hosts = settings.prefix(() -> "host");
        assertThat(hosts)
                .hasSize(4)
                .contains("host", "host-1", "host-2", "host-3");
    }

    @Test
    public void shouldFindPrefixWithOrder() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "host.2",
                "host-2", "host.3", "host-3");

        Settings settings = MemorySettings.of(params);

        List<Object> hosts = settings.prefix("host");
        assertThat(hosts).hasSize(4).contains("host", "host-1", "host-2", "host-3");
    }


    @Test
    public void shouldReturnErrorWhenPrefixesIsNull() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "host.2",
                "host-2", "host.3", "host-3");

        Settings settings = MemorySettings.of(params);
        assertThrows(NullPointerException.class, () -> settings.prefix((Collection<String>) null));
    }

    @Test
    public void shouldFindPrefixes() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "server", "server", "server-1", "server-1");

        Settings settings = MemorySettings.of(params);

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        Assertions.assertEquals(4, hosts.size());
        assertThat(hosts).contains("host", "host-1", "server", "server-1");
    }

    @Test
    public void shouldFindPrefixesSupplier() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "server", "server", "server-1", "server-1");

        Settings settings = MemorySettings.of(params);


        List<Object> hosts = settings.prefixSupplier(Arrays.asList(() -> "host", () -> "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }

    @Test
    public void shouldFindPrefixesSort() {
        Map<String, Object> params = Map.of("host", "host",
                "host.1", "host-1", "server", "server", "server-1", "server-1");

        Settings settings = MemorySettings.of(params);

        List<Object> hosts = settings.prefix(Arrays.asList("host", "server"));
        assertThat(hosts).hasSize(4).contains("host", "host-1", "server", "server-1");
    }
}