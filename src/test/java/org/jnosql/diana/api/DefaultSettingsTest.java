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

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultSettingsTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenInstanceIsNull() {
        Settings.of(null);
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
        assertEquals("value", settings.get("key"));
    }

    @Test
    public void shouldContainsKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertTrue(settings.containsKey("key"));
        assertFalse(settings.containsKey("key2"));
    }

    @Test
    public void shouldContainsValue() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertTrue(settings.containsValue("value"));
        assertFalse(settings.containsKey("value2"));
    }

    @Test
    public void shouldPutAnElement() {
        Settings settings = Settings.of();
        settings.put("key", "value");
        assertEquals(1, settings.size());
        assertTrue(settings.containsKey("key"));
    }

    @Test
    public void shouldRemove() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        settings.remove("key");
        assertTrue(settings.isEmpty());
    }

    @Test
    public void shouldGetKeys() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertThat(settings.keySet(), contains("key"));
    }

    @Test
    public void shouldGetValues() {
        Settings settings = Settings.of(singletonMap("key", "value"));
        assertThat(settings.values(), contains("value"));
    }
}
