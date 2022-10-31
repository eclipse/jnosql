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

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SettingsBuilderTest {


    @Test
    public void shouldReturnErrorWhenKeyIsNUll() {
        Assertions.assertThrows(NullPointerException.class, () -> Settings.builder().put(null, "value"));
    }

    @Test
    public void shouldReturnErrorWhenValueIsNUll() {
        Assertions.assertThrows(NullPointerException.class, () -> Settings.builder().put("key", null));
    }

    @Test
    public void shouldReturnErrorWhenMapHasNullKey() {

        Assertions.assertThrows(NullPointerException.class, () -> {
            Map<String, Object> map = Collections.singletonMap(null, "value");
            Settings.builder().putAll(map);
        });
    }

    @Test
    public void shouldReturnErrorWhenMapHasNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> {

            Map<String, Object> map = Collections.singletonMap("key", null);
            Settings.builder().putAll(map);
        });
    }

    @Test
    public void shouldCreateSettingsBuilder() {
        Settings settings = Settings.builder().put("key", "value").build();
        assertNotNull(settings);
        assertEquals("value", settings.get("key").get());
    }
}