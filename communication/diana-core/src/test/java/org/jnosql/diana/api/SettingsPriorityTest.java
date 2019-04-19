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
package org.jnosql.diana.api;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SettingsPriorityTest {


    @Test
    public void shouldReturnErrorWhenParameterIsNull() {
        assertThrows(NullPointerException.class, () -> SettingsPriority.get((Map<String, Object>) null));
        assertThrows(NullPointerException.class, () -> SettingsPriority.get((Settings) null));
    }

    @Test
    public void shouldReturnValueFromSettings() {
        Settings settings = SettingsPriority.get(Collections.singletonMap("key", "value"));
        assertEquals("value", settings.get("key"));
    }

    @Test
    public void shouldReturnEnvAsHighPriority() {
        Map<String, String> env = System.getenv();
        String key = env.keySet().stream().findFirst().get();
        Settings settings = SettingsPriority.get(Collections.singletonMap(key, "value"));
        assertEquals(env.get(key), settings.get(key));
    }

    @Test
    public void shouldReturnJavaSystemAsSecondPriority() {
        Properties properties = System.getProperties();
        String key = properties.keySet().stream().findFirst().get().toString();
        Settings settings = SettingsPriority.get(Collections.singletonMap(key, "value"));
        assertEquals(properties.get(key), settings.get(key));
    }
}